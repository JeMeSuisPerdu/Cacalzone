package pizzas;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Représente un ingrédient élémentaire pouvant entrer dans la composition d'une pizza.
 *
 * <p>Un ingrédient est caractérisé par son nom commercial et son coût unitaire.
 * Il possède également une logique de restriction : il contient une liste de
 * {@link TypePizza} pour lesquels son ajout est interdit (par exemple, de la
 * viande sera interdite sur une pizza de type végétarienne).</p>
 */
public class Ingredient implements Serializable {

  /**
   * Identifiant de sérialisation utilisé pour garantir la compatibilité
   * des versions de classe lors de la sauvegarde et du chargement des données.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Le nom commercial de l'ingrédient (ex: "Mozzarella", "Jambon").
   */
  String nom;

  /**
   * Le prix unitaire de l'ingrédient en euros.
   * Ce prix est utilisé pour calculer le coût de revient des pizzas.
   */
  Double prix;

  /**
   * L'ensemble des types de pizzas avec lesquels cet ingrédient est incompatible.
   * Si un type est présent dans cet ensemble, l'ingrédient ne pourra pas être
   * ajouté à une pizza de ce type.
   */
  Set<TypePizza> typesPizzaInterdits = new HashSet<>();

  /**
   * Construit un nouvel ingrédient avec un nom et un prix définis.
   *
   * <p>La liste des types de pizzas interdits est initialisée vide par défaut.
   * Elle pourra être remplie ultérieurement via les méthodes dédiées.</p>
   *
   * @param nom Le nom commercial de l'ingrédient.
   * @param prix Le coût unitaire de l'ingrédient.
   */
  public Ingredient(String nom, Double prix) {
    super();
    this.nom = nom;
    this.prix = prix;
  }

  /**
   * Ajoute une restriction pour un type de pizza donné.
   *
   * <p>Après cet appel, l'ingrédient sera considéré comme interdit pour toutes
   * les pizzas du type spécifié.</p>
   *
   * @param typePizzaInterdit Le type de pizza avec lequel l'ingrédient devient incompatible.
   */
  public void addTypePizzaInterdit(TypePizza typePizzaInterdit) {
    this.typesPizzaInterdits.add(typePizzaInterdit);
  }

  /**
   * Lève une restriction existante pour un type de pizza donné.
   *
   * <p>Après cet appel, l'ingrédient sera de nouveau autorisé pour les pizzas
   * du type spécifié.</p>
   *
   * @param typePizzaInterdit Le type de pizza pour lequel l'ingrédient redevient autorisé.
   */
  public void removeTypePizzaInterdit(TypePizza typePizzaInterdit) {
    this.typesPizzaInterdits.remove(typePizzaInterdit);
  }

  /**
   * Récupère le nom de l'ingrédient.
   *
   * @return Le nom sous forme de chaîne de caractères.
   */
  public String getNom() {
    return nom;
  }

  /**
   * Modifie le nom commercial de l'ingrédient.
   *
   * @param nom Le nouveau nom à attribuer à l'ingrédient.
   */
  public void setNom(String nom) {
    this.nom = nom;
  }

  /**
   * Récupère le prix unitaire de l'ingrédient.
   *
   * @return Le prix en euros.
   */
  public Double getPrix() {
    return prix;
  }

  /**
   * Modifie le prix unitaire de l'ingrédient.
   *
   * <p>Attention, cette modification impactera le calcul du prix minimal
   * de toutes les pizzas utilisant cet ingrédient.</p>
   *
   * @param prix Le nouveau prix unitaire.
   */
  public void setPrix(Double prix) {
    this.prix = prix;
  }

  /**
   * Récupère l'ensemble des restrictions associées à cet ingrédient.
   *
   * @return Un Set contenant tous les types de pizzas interdits pour cet ingrédient.
   */
  public Set<TypePizza> getTypesPizzaInterdits() {
    return typesPizzaInterdits;
  }

  /**
   * Remplace la liste complète des restrictions de l'ingrédient.
   *
   * @param typesPizzaInterdits Le nouvel ensemble des types de pizzas interdits.
   */
  public void setTypesPizzaInterdits(Set<TypePizza> typesPizzaInterdits) {
    this.typesPizzaInterdits = typesPizzaInterdits;
  }
}