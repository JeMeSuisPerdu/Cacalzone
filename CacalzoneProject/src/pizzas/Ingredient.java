package pizzas;

import java.util.HashSet;
import java.util.Set;

/**
 * Représente un ingrédient pouvant être utilisé dans la composition d'une pizza.
 * Un ingrédient possède un nom, un prix et une liste de types de pizzas pour lesquels
 * il est interdit.
 */
public class Ingredient {
  
  // --------------- ATTRIBUTS ---------------

  /** Nom de l'ingrédient. */
  String nom;

  /** Prix (unitaire) de l'ingrédient. */
  Double prix;

  /** Ensemble des types de pizzas sur lesquels cet ingrédient est interdit. */
  Set<TypePizza> typePizzaInterdit;

  // --------------- CONSTRUCTEUR ---------------

  /**
   * Constructeur permettant de créer un ingrédient.
   *
   * @param nom nom de l'ingrédient
   * @param prix prix de l'ingrédient
   * @param typesPizzaInterdits ensemble des types de pizzas pour lesquels ingrédient = interdit
   */
  public Ingredient(String nom, Double prix, Set<TypePizza> typesPizzaInterdits) {
    super();
    this.nom = nom;
    this.prix = prix;
    this.typePizzaInterdit = new HashSet<>(typesPizzaInterdits);
  }
  
  // --------------- GETTERS // SETTERS ---------------

  /**
   * Retourne le nom de l'ingrédient.
   * 
   * @return nom de l'ingrédient
   */
  public String getNom() {
    return nom;
  }

  /**
   * Modifie le nom de l'ingrédient.
   * 
   * @param nom nouveau nom
   */
  public void setNom(String nom) {
    this.nom = nom;
  }

  /**
   * Retourne le prix de l'ingrédient.
   * 
   * @return prix de l'ingrédient
   */
  public Double getPrix() {
    return prix;
  }

  /**
   * Modifie le prix de l'ingrédient.
   * 
   * @param prix nouveau prix
   */
  public void setPrix(Double prix) {
    this.prix = prix;
  }

  /**
   * Retourne l'ensemble des types de pizzas pour lesquels cet ingrédient est interdit.
   * 
   * @return ensemble des types interdits
   */
  public Set<TypePizza> getTypePizzaInterdit() {
    return typePizzaInterdit;
  }

  /**
   * Modifie la liste des types de pizzas pour lesquels cet ingrédient est interdit.
   * 
   * @param typePizzaInterdit nouvel ensemble de types interdits
   */
  public void setTypePizzaInterdit(Set<TypePizza> typePizzaInterdit) {
    this.typePizzaInterdit = typePizzaInterdit;
  }
  
}
