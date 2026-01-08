package pizzas;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service gérant la logique métier réservée aux clients.
 *
 * <p>Cette classe fait l'interface entre le menu (données) et l'utilisateur client.
 * Elle permet l'inscription, la connexion, la gestion du profil, la passation
 * de commandes, l'application de filtres de recherche et la gestion des évaluations.</p>
 */
public class ServiceClient implements InterClient {

  /**
   * Référence vers le menu centralisant les données de l'application.
   */
  private final Menu menu;

  /**
   * Le client actuellement authentifié sur ce service.
   * Si cette valeur est null, le service est considéré comme "non connecté".
   */
  private CompteClient clientConnecte;

  /**
   * Filtre optionnel sur le type de pizza (ex: VEGETARIENNE).
   * Utilisé pour la recherche de pizzas.
   */
  private TypePizza filtreType;

  /**
   * Filtre sur le prix maximum.
   * Initialisé à la valeur maximale possible (Double.MAX_VALUE) par défaut.
   */
  private double filtrePrixMax = Double.MAX_VALUE;

  /**
   * Liste des noms d'ingrédients recherchés.
   * Utilisé pour filtrer les pizzas contenant au moins un de ces ingrédients.
   */
  private Set<String> filtreIngredients = new HashSet<>();

  /**
   * La commande actuellement en cours de préparation par le client.
   * Elle est stockée ici tant qu'elle n'est pas validée.
   */
  private Commande commandeEnCours;

  /**
   * Constructeur du service client.
   *
   * <p>Initialise le service avec une référence vers le menu partagé.</p>
   *
   * @param menu Le menu contenant les données de la pizzeria.
   */
  public ServiceClient(Menu menu) {
    this.menu = menu;
  }

  /**
   * Inscrit un nouveau client dans le système.
   *
   * <p>Cette méthode vérifie la validité des paramètres et l'unicité de l'email.
   * Si tout est correct, un nouveau compte client est créé et ajouté au menu.</p>
   *
   * @param email L'adresse email du client (identifiant unique).
   * @param mdp Le mot de passe du client.
   * @param info Les informations personnelles du client.
   * @return 0 si l'inscription réussit, -1 si l'email existe déjà,-3 si un des paramètres est null.
   */
  @Override
  public int inscription(String email, String mdp,
      InformationPersonnelle info) {
    if (email == null || mdp == null || info == null) {
      return -3;
    }
    if (menu.trouverUtilisateur(email) != null) {
      return -1;
    }

    CompteClient nouveau = new CompteClient(email, mdp, info);
    menu.ajouterUtilisateur(nouveau);
    return 0;
  }

  /**
   * Authentifie un utilisateur et initialise la session client.
   *
   * <p>Vérifie que les identifiants correspondent à un utilisateur existant
   * et que cet utilisateur est bien un client (et non un pizzaïolo).</p>
   *
   * @param email L'email de connexion.
   * @param mdp Le mot de passe de connexion.
   * @return true si la connexion réussit, false sinon.
   */
  @Override
  public boolean connexion(String email, String mdp) {
    Utilisateur u = menu.authentifier(email, mdp);
    if (u instanceof CompteClient) {
      this.clientConnecte = (CompteClient) u;
      return true;
    }
    return false;
  }

  /**
   * Déconnecte l'utilisateur actuel.
   *
   * <p>Réinitialise la référence du client connecté à null.</p>
   *
   * @throws NonConnecteException Si aucun client n'est actuellement connecté.
   */
  @Override
  public void deconnexion() throws NonConnecteException {
    if (clientConnecte == null) {
      throw new NonConnecteException();
    }
    this.clientConnecte = null;
  }

  /**
   * Initialise une nouvelle commande pour le client connecté.
   *
   * <p>Cette méthode crée une nouvelle instance de Commande associée au client
   * courant et la stocke comme commande en cours.</p>
   *
   * @return L'objet Commande nouvellement créé.
   * @throws NonConnecteException Si aucun client n'est connecté.
   */
  @Override
  public Commande debuterCommande() throws NonConnecteException {
    if (clientConnecte == null) {
      throw new NonConnecteException();
    }
    this.commandeEnCours = new Commande(clientConnecte);
    return this.commandeEnCours;
  }

  /**
   * Valide une commande et l'envoie en cuisine.
   *
   * <p>Si la commande passée en paramètre est null, la commande en cours interne
   * est utilisée. La commande est validée, ajoutée à l'historique global du menu
   * et à l'historique personnel du client. La commande en cours est ensuite
   * réinitialisée.</p>
   *
   * @param cmd La commande à valider (peut être null).
   * @throws NonConnecteException Si aucun client n'est connecté.
   * @throws CommandeException Si la commande est vide, inexistante ou impossible à valider.
   */
  @Override
  public void validerCommande(Commande cmd)
      throws NonConnecteException, CommandeException {
    if (clientConnecte == null) {
      throw new NonConnecteException();
    }

    Commande commande = (cmd != null) ? cmd : this.commandeEnCours;

    if (commande == null) {
      throw new CommandeException("ERREUR : Commande vide ou inexistante.");
    }

    if (commande.valider()) {
      menu.ajouterCommande(commande);
      clientConnecte.ajouterCommande(commande);
      this.commandeEnCours = null;
    }
  }

  /**
   * Récupère la liste de toutes les pizzas disponibles dans le menu.
   *
   * @return Un ensemble contenant toutes les pizzas.
   */
  @Override
  public Set<Pizza> getPizzas() {
    return menu.getPizzas();
  }

  /**
   * Ajoute une pizza à une commande spécifique.
   *
   * @param p La pizza à ajouter.
   * @param n La quantité souhaitée.
   * @param c La commande concernée.
   * @throws CommandeException Si la commande ou la pizza est null, ou commande immodifiable.
   */
  @Override
  public void ajouterPizza(Pizza p, int n, Commande c)
      throws CommandeException {
    if (c == null || p == null) {
      throw new CommandeException("ERREUR : Pizza ou Commande inexistante.");
    }
    c.ajouterPizza(p, n);
  }

  /**
   * Annule une commande existante.
   *
   * <p>Cette méthode appelle la méthode d'annulation de l'objet Commande,
   * ce qui vide son contenu et change son état.</p>
   *
   * @param c La commande à annuler (si null, aucune action n'est effectuée).
   */
  @Override
  public void annulerCommande(Commande c) {
    if (c != null) {
      c.annuler();
    }
  }

  /**
   * Récupère la liste des commandes validées par le client.
   *
   * <p>Bien que nommée "en cours", cette méthode filtre actuellement les commandes
   * ayant l'état VALIDEE dans l'historique du client.</p>
   *
   * @return Une liste de commandes validées.
   */
  @Override
  public List<Commande> getCommandesEncours() {
    if (clientConnecte == null) {
      return new ArrayList<>();
    }

    List<Commande> enCours = new ArrayList<>();
    for (Commande c : clientConnecte.getHistoriqueCommandes()) {
      if (c.getEtat() == Commande.Etat.VALIDEE) {
        enCours.add(c);
      }
    }
    return enCours;
  }

  /**
   * Récupère l'historique complet des commandes du client.
   *
   * @return La liste de toutes les commandes passées par le client.
   */
  @Override
  public List<Commande> getCommandePassees() {
    if (clientConnecte != null) {
      return clientConnecte.getHistoriqueCommandes();
    }
    return new ArrayList<>();
  }

  /**
   * Définit un filtre sur le type de pizza.
   *
   * @param t Le type de pizza à rechercher.
   */
  @Override
  public void ajouterFiltre(TypePizza t) {
    this.filtreType = t;
  }

  /**
   * Définit un filtre sur les ingrédients.
   *
   * <p>Remplace les filtres d'ingrédients existants par ceux fournis en paramètre.
   * Les noms d'ingrédients sont stockés en minuscules pour la recherche.</p>
   *
   * @param ingredients La liste des noms d'ingrédients à filtrer.
   */
  @Override
  public void ajouterFiltre(String... ingredients) {
    this.filtreIngredients.clear();
    for (String s : ingredients) {
      this.filtreIngredients.add(s.toLowerCase());
    }
  }

  /**
   * Définit un filtre sur le prix maximum.
   *
   * @param p Le prix maximum accepté.
   */
  @Override
  public void ajouterFiltre(double p) {
    this.filtrePrixMax = p;
  }

  /**
   * Sélectionne les pizzas correspondant aux critères de filtrage actuels.
   *
   * <p>Cette méthode parcourt toutes les pizzas du menu et sélectionne celles
   * qui respectent simultanément :
   * 1. Le prix maximum.
   * 2. Le type de pizza (si défini).
   * 3. La présence d'au moins un des ingrédients filtrés (si la liste des
   * filtres ingrédients n'est pas vide).</p>
   *
   * @return Un ensemble de pizzas correspondant aux critères.
   */
  @Override
  public Set<Pizza> selectionPizzaFiltres() {
    Set<Pizza> resultat = new HashSet<>();
    for (Pizza p : menu.getPizzas()) {
      if (p.getPrix() > filtrePrixMax) {
        continue;
      }

      if (filtreType != null && p.getTypePizza() != filtreType) {
        continue;
      }

      boolean contientIngredientFiltre = false;

      if (this.filtreIngredients.isEmpty()) {
        contientIngredientFiltre = true;
      } else {
        for (Ingredient ing : p.getIngredients()) {
          if (this.filtreIngredients.contains(ing.getNom().toLowerCase())) {
            contientIngredientFiltre = true;
            break;
          }
        }
      }

      if (contientIngredientFiltre) {
        resultat.add(p);
      }
    }
    return resultat;
  }

  /**
   * Réinitialise tous les filtres de recherche.
   *
   * <p>Le type est remis à null, le prix max à MAX_VALUE et la liste
   * des ingrédients est vidée.</p>
   */
  @Override
  public void supprimerFiltres() {
    this.filtreType = null;
    this.filtrePrixMax = Double.MAX_VALUE;
    this.filtreIngredients.clear();
  }

  /**
   * Récupère les évaluations associées à une pizza donnée.
   *
   * @param p La pizza dont on veut les avis.
   * @return Un ensemble d'évaluations.
   */
  @Override
  public Set<Evaluation> getEvaluationsPizza(Pizza p) {
    if (p != null) {
      return p.getEvaluations();
    }
    return new HashSet<>();
  }

  /**
   * Calcule la note moyenne d'une pizza.
   *
   * <p>Si la pizza n'a aucune évaluation, la moyenne retournée est 0.0.</p>
   *
   * @param p La pizza concernée.
   * @return La moyenne arithmétique des notes.
   */
  @Override
  public double getNoteMoyenne(Pizza p) {
    if (p == null || p.getEvaluations().isEmpty()) {
      return 0.0;
    }

    double somme = 0;
    for (Evaluation e : p.getEvaluations()) {
      somme += e.getNote();
    }
    return somme / p.getEvaluations().size();
  }

  /**
   * Ajoute une nouvelle évaluation pour une pizza.
   *
   * <p>L'opération nécessite qu'un client soit connecté. L'évaluation est créée
   * avec l'email du client connecté et ajoutée à la liste des avis de la pizza.</p>
   *
   * @param p La pizza à évaluer.
   * @param note La note attribuée.
   * @param commentaire Le commentaire associé.
   * @return true si l'ajout a réussi, false si aucun client n'est connecté ou si la pizza est null.
   */
  @Override
  public boolean ajouterEvaluation(Pizza p, int note, String commentaire) {
    if (clientConnecte == null || p == null) {
      return false;
    }
    Evaluation eval = new Evaluation(note, commentaire,
        clientConnecte.getEmail());
    p.getEvaluations().add(eval);
    return true;
  }

  /**
   * Récupère l'objet Menu géré par ce service.
   *
   * @return L'objet Menu.
   */
  public Menu getMenu() {
    return menu;
  }

  /**
   * Recherche une pizza dans le menu par son nom.
   *
   * @param nom Le nom exact de la pizza recherchée.
   * @return La Pizza correspondante ou null si non trouvée.
   */
  public Pizza getPizza(String nom) {
    for (Pizza p : this.menu.getPizzas()) {
      if (p.getNom().equals(nom)) {
        return p;
      }
    }
    return null;
  }

  /**
   * Récupère la commande en cours de construction par le client.
   *
   * @return L'objet Commande actif (non encore validé).
   */
  public Commande getCommandeActive() {
    return this.commandeEnCours;
  }

  /**
   * Récupère le compte du client actuellement connecté au service.
   *
   * @return Le client connecté, ou null si aucun utilisateur n'est authentifié.
   */
  public CompteClient getClient() {
    return this.clientConnecte;
  }

}