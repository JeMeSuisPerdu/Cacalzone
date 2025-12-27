package pizzas;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import pizzas.Ingredient;

// A compléter
/**
 * Classe Pizza qui regroupe les informations nécessaires à la création d'une pizza.
 * Elle dispose de deux méthodes utilitaires ainsi que d'une relation vers ingrédient.
 * Représente une pizza composée d'un nom, d'un type, d'un prix, d'une photo
 * et d'un ensemble d'ingrédients.
 *
 */
public class Pizza {
  // --------------- ATTRIBUTS ---------------
  /** Nom de la pizza. */
  private String nom;

  /** Type de pizza (base, taille, catégorie, etc.). */
  private TypePizza typePizza;

  /** Liste des ingrédients constituant la pizza. */
  private Set<Ingredient> ingredients = new HashSet<>();

  /** Prix final de la pizza. */
  private Double prix;

  /** Photo de la pizza (URL ou chemin local). */
  private String photo = "";
  
  // --------------- CONSTRUCTEUR ---------------
  /**
   * Constructeur qui permet de créer une pizza à partir de son nom et de son type.
   *
   * @param nom nom de la pizza
   * @param typePizza type de la pizza
   */
  public Pizza(String nom, TypePizza typePizza) {
    super();
    this.nom = nom;
    this.typePizza = typePizza;
  }
  
  // --------------- GETTERS // SETTERS ---------------

  /** 
   * Retourne le nom de la pizza.
   * 
   * @return nom
   */
  public String getNom() {
    return nom;
  }
  
  /**
   * Modifie le nom de la pizza.
   * 
   * @param nom nouveau nom
   */
  public void setNom(String nom) {
    this.nom = nom;
  }
  
  /**
   * Retourne le type de pizza.
   * 
   * @return typePizza
   */
  public TypePizza getTypePizza() {
    return typePizza;
  }
  
  /**
   * Modifie le type de la pizza.
   * 
   * @param typePizza nouveau type
   */
  public void setTypePizza(TypePizza typePizza) {
    this.typePizza = typePizza;
  }
  
  /**
   * Retourne la liste des ingrédients associés à la pizza.
   * 
   * @return ingredients
   */
  public Set<Ingredient> getIngredients() {
    return ingredients;
  }
  
  /**
   * Modifie l’ensemble des ingrédients de la pizza.
   * 
   * @param ingredients nouvel ensemble d’ingrédients
   */
  public void setIngredients(Set<Ingredient> ingredients) {
    this.ingredients = ingredients;
  }
  
  /**
   * Retourne le prix de la pizza ou le prix minimal
   * si l'attribut prix est null
   * 
   * @return prix
   */
  public Double getPrix() {
    return (this.prix != null) 
	  ? this.prix 
   	  : this.getPrixMinimalPizza();
  }
  
  /**
   * Définit le prix de la pizza si celui-ci est supérieur ou égal au prix minimal calculé.
   * 
   * @param prix prix souhaité
   * @return true si le prix a été changé, 
	false si le prix donné est inférieur au prix minimal
   */
  public boolean setPrix(Double prix) {
	if (prix > this.getPrixMinimalPizza()) {
	  this.prix = prix;
	  return true;
	} else return false;
  }

  /**
   * Retourne le chemin ou lien vers la photo de la pizza.
   * 
   * @return photo
   */
  public String getPhoto() {
    return photo;
  }

  /**
   * Définit la photo de la pizza.
   * 
   * @param photo chemin ou lien de l’image
   */
  public void setPhoto(String photo) {
    this.photo = photo;
  }

  // --------------- METHODES UTILITAIRES ---------------
  
  /**
   * Méthode utilitaire qui permet de vérifier si le prix minimal d'une pizza est respecté.  
   * Le minimum est calculé ainsi :  
   * - Somme du prix des ingrédients  
   * - + 40 %  
   * - Arrondi à la dizaine de centimes supérieure  
   *
   * @return prix minimal de la pizza
   */
  public Double getPrixMinimalPizza() {
    Double sommePrix = 0.0;
    for (Iterator<Ingredient> it = ingredients.iterator(); it.hasNext();) {
      Ingredient i = it.next();
      sommePrix += i.getPrix();
    }
    Double prix40pourcent = (sommePrix * 1.4) * 100;
    Double prixMin = Math.ceil(prix40pourcent / 10.0) * 10.0 / 100;
    
    return prixMin;
  }
  
  /**
   * Méthode utilitaire qui permet de vérifier si un ingrédient peut être ajouté à la pizza.  
   * L’ajout est effectué seulement si :  
   * - l’ingrédient n’est pas déjà présent dans la liste  
   * - l’ingrédient n’est pas interdit pour le type de pizza  
   *
   * @param i ingrédient à ajouter
   * @return true si l’ajout est autorisé et effectué, false sinon
   */
  public Boolean ajouterIngredient(Ingredient i) {
    if (!this.ingredients.contains(i) && !i.getTypesPizzaInterdits().contains(this.getTypePizza())) {
      this.ingredients.add(i);
      return true;
    }
    return false; 
  } 
  
}
