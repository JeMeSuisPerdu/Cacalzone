package pizzas;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Représente une commande de pizzas effectuée par un client au sein de la pizzeria.
 *
 * <p>Cette classe gère le cycle de vie complet d'une commande, depuis sa création
 * jusqu'à son traitement par le pizzaïolo, en passant par sa validation.
 * Elle stocke la liste des pizzas commandées ainsi que leurs quantités respectives.
 * Elle permet également de calculer le coût total de la commande et de suivre
 * son état (CRÉÉE, VALIDÉE, TRAITÉE, ANNULÉE).</p>
 */
public class Commande implements Serializable {

  /**
   * Identifiant de sérialisation utilisé pour garantir la compatibilité
   * des versions de classe lors de la sauvegarde et du chargement des données.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Énumération définissant les différents états possibles d'une commande
   * au cours de son cycle de vie.
   */
  public enum Etat {
    /**
     * La commande est en cours de composition par le client.
     * Elle peut encore être modifiée (ajout de pizzas).
     */
    CREEE,

    /**
     * La commande a été validée par le client.
     * Elle est désormais en attente de préparation et ne peut plus être modifiée.
     */
    VALIDEE,

    /**
     * La commande a été préparée par le pizzaïolo.
     * Elle est considérée comme terminée.
     */
    TRAITEE,

    /**
     * La commande a été annulée par le client avant son traitement.
     * Elle est conservée dans l'historique mais ignorée dans les statistiques de vente.
     */
    ANNULEE
  }

  /**
   * Structure de données associant chaque pizza commandée à la quantité souhaitée.
   * La clé est l'objet Pizza et la valeur est un entier représentant le nombre d'unités.
   */
  private Map<Pizza, Integer> pizzas = new HashMap<>();

  /**
   * L'état courant de la commande.
   * Il est initialisé à {@link Etat#CREEE} lors de l'instanciation de l'objet.
   */
  private Etat etat = Etat.CREEE;

  /**
   * Le compte du client propriétaire de cette commande.
   * Ce lien permet d'associer la commande à l'historique d'un utilisateur spécifique.
   */
  private final CompteClient client;

  /**
   * La date et l'heure exactes de la création de la commande.
   * Initialisé automatiquement à l'instant présent lors de la construction de l'objet.
   */
  private LocalDateTime date = LocalDateTime.now();

  /**
   * Constructeur principal de la classe Commande.
   *
   * <p>Initialise une nouvelle commande vide pour un client donné.
   * L'état initial est défini à {@code CREEE} et la date est fixée à l'instant T.</p>
   *
   * @param client Le compte du client qui passe la commande. Ne doit pas être null.
   */
  public Commande(CompteClient client) {
    this.client = client;
  }

  /**
   * Ajoute une ou plusieurs pizzas à la commande courante.
   *
   * <p>Cette opération n'est autorisée que si la commande est encore dans l'état {@code CREEE}.
   * Si la pizza est déjà présente dans la commande, la quantité spécifiée est ajoutée
   * à la quantité existante.</p>
   *
   * @param pizza    La pizza à ajouter à la liste.
   * @param quantite Le nombre de pizzas de ce type à ajouter (doit être strictement positif).
   * @return {@code true} si l'ajout a été effectué avec succès, {@code false} si params invalide.

   * @throws CommandeException Si la commande n'est plus dans l'état {@code CREEE}.
   */
  public boolean ajouterPizza(Pizza pizza, int quantite) throws CommandeException {
    if (this.etat != Etat.CREEE) {
      throw new CommandeException("ERREUR : Il faut créer une commande avant.");
    }
    if (pizza == null || quantite <= 0) {
      return false;
    }

    this.pizzas.put(pizza, this.pizzas.getOrDefault(pizza, 0) + quantite);
    return true;
  }

  /**
   * Valide la commande pour l'envoyer en préparation.
   *
   * <p>Cette méthode fait passer l'état de la commande de {@code CREEE} à {@code VALIDEE}.
   * Cette transition n'est possible que si la commande contient au moins une pizza.
   * Une fois validée, la commande devient visible pour le pizzaïolo.</p>
   *
   * @return {@code true} si la commande a été validée avec succès, {@code false}  commande vide .
   */
  public boolean valider() {
    if (this.etat == Etat.CREEE && !pizzas.isEmpty()) {
      this.etat = Etat.VALIDEE;
      return true;
    }
    return false;
  }

  /**
   * Marque la commande comme ayant été traitée par le pizzaïolo.
   *
   * <p>Cette méthode fait passer l'état de la commande de {@code VALIDEE} à {@code TRAITEE}.
   * Elle est généralement appelée par le service du pizzaïolo une fois la préparation terminée.</p>
   */
  public void traiter() {
    if (this.etat == Etat.VALIDEE) {
      this.etat = Etat.TRAITEE;
    }
  }

  /**
   * Annule la commande courante.
   *
   * <p>Cette action est possible tant que la commande n'a pas été traitée (donc si elle est
   * {@code CREEE} ou {@code VALIDEE}). L'annulation vide la liste des pizzas associées
   * et passe l'état à {@code ANNULEE}.</p>
   */
  public void annuler() {
    if (this.etat == Etat.CREEE || this.etat == Etat.VALIDEE) {
      this.pizzas.clear();
      this.etat = Etat.ANNULEE;
    }
  }

  /**
   * Calcule le montant total de la commande.
   *
   * <p>Le calcul s'effectue en sommant le prix unitaire de chaque pizza multiplié par
   * la quantité commandée. Le prix utilisé est le prix défini dans l'objet Pizza au moment
   * du calcul.</p>
   *
   * @return Le prix total de la commande en euros (double).
   */
  public double getPrixTotal() {
    return pizzas.entrySet().stream()
      .mapToDouble(e -> e.getKey().getPrix() * e.getValue()).sum();
  }

  /**
   * Récupère l'état actuel de la commande.
   *
   * @return Une valeur de l'énumération {@link Etat} représentant le statut de la commande.
   */
  public Etat getEtat() {
    return etat;
  }

  /**
   * Récupère le client associé à cette commande.
   *
   * @return L'objet {@link CompteClient} représentant l'utilisateur ayant passé commande.
   */
  public CompteClient getClient() {
    return client;
  }

  /**
   * Accède au contenu détaillé de la commande.
   *
   * @return Une {@link Map} où chaque clé est une {@link Pizza} et chaque valeur est une quantité.
   */
  public Map<Pizza, Integer> getPizzas() {
    return pizzas;
  }

  /**
   * Fournit la date de création de la commande formatée en chaîne de caractères.
   *
   * <p>Le format utilisé est "jj-MM-aaaa HH:mm:ss" (jour-mois-année heure:minute:seconde),
   * ce qui est adapté pour l'affichage dans l'interface utilisateur.</p>
   *
   * @return La date formatée sous forme de {@code String}.
   */
  public String getDate() {
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    return date.format(format);
  }
}