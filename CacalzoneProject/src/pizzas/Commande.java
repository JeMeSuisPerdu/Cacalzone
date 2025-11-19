package pizzas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Représente une commande de pizzas passée par un client.
 * Une commande contient une liste de pizzas avec leurs quantités
 * ainsi qu’un état permettant de suivre son avancement.
 */
public class Commande implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Enumération pour les différents états possibles d'une commande.
   * CREEE : la commande vient d’être créée et peut être modifiée  
   * VALIDEE : la commande a été confirmée et ne peut plus être modifiée  
   * TRAITEE : la commande a été préparée par le pizzaïolo  
   */
  public enum Etat {
    CREEE,     
    VALIDEE,   
    TRAITEE
  }

  /**
   * Map des pizzas et de leurs quantités dans cette commande.
   * La clé représente une pizza, la valeur représente la quantité commandée.
   */
  private Map<Pizza, Integer> pizzas;

  /**
   * L'état actuel de la commande.
   */
  private Etat etat;

  /**
   * L'email du client qui a passé la commande.
   */
  private final Client client;

  /**
   * Constructeur pour créer une nouvelle commande.
   * Une nouvelle commande est toujours dans l'état CREEE.
   *
   * @param emailClient l'email du client qui passe la commande
   * @throws IllegalArgumentException si l'email du client est null ou vide
   */
  public Commande(Client client) throws IllegalArgumentException {
    this.client = client;
    this.pizzas = new HashMap<>();
    this.etat = Etat.CREEE;
  }

  /**
   * Ajoute une pizza à la commande si elle est dans l'état CREEE.
   * Si la pizza existe déjà, la quantité est modifiée.
   *
   * @param pizza la pizza à ajouter ou à modifier
   * @param quantite la quantité souhaitée pour cette pizza
   * @return vrai si la pizza a été ajoutée ou mise à jour, false sinon 
   * @throws CommandeException si la commande n'est pas dans l'état CREEE
   */
  public boolean ajouterPizza(Pizza pizza, int quantite) throws CommandeException {
    if (this.etat != Etat.CREEE) {
      throw new CommandeException();
    }
    if (pizza == null) {
      return false;
    }
    if (this.pizzas.containsKey(pizza)) {
      this.pizzas.replace(pizza, quantite);
    } else {
      this.pizzas.put(pizza, quantite);
    }
    return true;
  }

  /**
   * Valide la commande et change son état de CREEE à VALIDEE.
   * Une fois validée, la commande ne peut plus être modifiée.
   *
   * @return vrai si la commande a été validée, false sinon
   */
  public boolean valider() {
    if (this.etat == Etat.CREEE) {
      this.etat = Etat.VALIDEE;
      return true;
    }
    return false;
  }

  /**
   * Marque la commande comme traitée et change l'état de VALIDEE à TRAITEE.
   * Cette méthode est normalement utilisée par le pizzaïolo.
   *
   * @return vrai si la commande a été traitée, false sinon 
   */
  public boolean traiter() {
    if (this.etat == Etat.VALIDEE) {
      this.etat = Etat.TRAITEE;
      return true;
    }
    return false;
  }

  /**
   * Calcule le prix total de la commande en multipliant
   * le prix de chaque pizza par sa quantité associée.
   *
   * @return le prix total de la commande
   */
  public double getPrixTotal() {
    return this.pizzas.entrySet().stream()
      .mapToDouble(i -> i.getValue() * i.getKey().getPrix()).sum();
  }

  // --- Les Getters ---

  /**
   * Renvoie un Map des pizzas contenues dans la commande,
   * associées à leurs quantités.
   *
   * @return Map<Pizza Quantite>
   */
  public Map<Pizza, Integer> getPizzas() {
    return this.pizzas;
  }

  /**
   * Renvoie l'état actuel de la commande.
   *
   * @return l'état de la commande
   */
  public Etat getEtat() {
    return etat;
  }

  /**
   * Renvoie le client qui a passé la commande.
   *
   * @return le client
   */
  public Client getClient() {
    return this.client;
  }

  // --- LES METHODES POUR PERMETTRE LA COMPARAISON ET L'AFFICHAGE ---

  /**
   * Calcule le hashcode de la commande en fonction de son état,
   * de ses pizzas et de l'email du client.
   *
   * @return un entier représentant le hashcode
   */
  @Override
  public int hashCode() {
    return Objects.hash(etat, pizzas, this.client.nom);
  }

  /**
   * Compare deux commandes en vérifiant : l'état,la liste des pizzas et leurs quantités,
   * l'email du client.
   *
   * @param obj l'objet à comparer
   * @return true si les deux commandes sont identiques, false sinon
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Commande other = (Commande) obj;
    return etat == other.etat && Objects.equals(pizzas, other.pizzas)
        && Objects.equals(this.client.nom, other.this.client.nom);
  }

  /**
   * Retourne une représentation textuelle de la commande,
   * incluant son état, l'email du client et le nombre de pizzas.
   *
   * @return chaîne de caractères descriptive
   */
  @Override
  public String toString() {
    return "Commande [etat=" + etat + ", emailClient=" + this.client.nom 
        + ", nbPizzas=" + pizzas.size() + "]";
  }
}
