package pizzas;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Représente un ingrédient pouvant être utilisé dans la composition d'une pizza.
 * Un ingrédient possède un nom, un prix et une liste de types de pizzas pour lesquels
 * il est interdit.
 */
public class Ingredient implements Serializable {
  
  // --------------- ATTRIBUTS ---------------

  /** Nom de l'ingrédient. */
  String nom;

  /** Prix (unitaire) de l'ingrédient. */
  Double prix;

  /** Ensemble des types de pizzas sur lesquels cet ingrédient est interdit. */
  Set<TypePizza> typesPizzaInterdits = new HashSet<>();;

  // --------------- CONSTRUCTEUR ---------------

  /**
   * Constructeur permettant de créer un ingrédient.
   *
   * @param nom nom de l'ingrédient
   * @param prix prix de l'ingrédient
   * @param typesPizzaInterdits ensemble des types de pizzas pour lesquels ingrédient = interdit
   */
  public Ingredient(String nom, Double prix) {
    super();
    this.nom = nom;
    this.prix = prix;
  }
  
  // ----------------- METHODES---------------------
  
  /**
   * Ajoute un type de pizza pour lequel 
   * cet ingrédient est interdit.
   * 
   * @param typePizzaInterdit nouveau type interdit
   */
  public void addTypePizzaInterdit(TypePizza typePizzaInterdit) {
    this.typesPizzaInterdits.add(typePizzaInterdit);
  }
  
  // --------------- GETTERS // SETTERS ---------------

  /**
   * Renvoie le nom de l'ingrédient.
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
   * Renvoie le prix de l'ingrédient.
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
   * Renvoie l'ensemble des types de pizzas pour lesquels cet ingrédient est interdit.
   * 
   * @return ensemble des types interdits
   */
  public Set<TypePizza> getTypesPizzaInterdits() {
    return typesPizzaInterdits;
  }

  /**
   * Modifie la liste des types de pizzas pour lesquels cet ingrédient est interdit.
   * 
   * @param typesPizzaInterdits nouvel ensemble de types interdits
   */
  public void setTypesPizzaInterdits(Set<TypePizza> typesPizzaInterdits) {
    this.typesPizzaInterdits = typesPizzaInterdits;
  }
  
}
