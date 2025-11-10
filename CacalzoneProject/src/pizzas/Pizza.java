package pizzas;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import pizzas.Ingredient;

// A compléter
/**
 * Classe Pizza définit par un nom et un type de pizza.
 *
 * @author Anis LAFRAD
 *
 */
public class Pizza {
  // --------------- ATTRIBUTS ---------------
  private String nom;
  private TypePizza typePizza;
  // Liste d'ingrédients
  private Set<Ingredient> ingredients = new HashSet<>();
  private Double prix;
  private String photo;
  
  // --------------- CONSTRUCTEUR ---------------
  public Pizza(String nom, TypePizza typePizza) {
    super();
    this.nom = nom;
    this.typePizza = typePizza;
  }
  
  // --------------- GETTERS // SETTERS ---------------
  public String getNom() {
    return nom;
  }
  
  public void setNom(String nom) {
    this.nom = nom;
  }
  
  public TypePizza getUntype() {
    return typePizza;
  }
  
  public void setUntype(TypePizza typePizza) {
    this.typePizza = typePizza;
  }
  
  public Set<Ingredient> getIngredients() {
    return ingredients;
  }
  
  public void setIngredients(Set<Ingredient> ingredients) {
    this.ingredients = ingredients;
  }
  
  public Double getPrix() {
    return prix;
  }
  
  public void setPrix(Double prix) {
    if (prix >= this.prixMinimalPizza()) {
      this.prix = prix;
    } else {
      System.out.println(
          "ERREUR : Prix minimal Pizza doit-être supérieur ou égal à : "
              + this.prixMinimalPizza());
    }
  }
  public String getPhoto() {
    return photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }
  // --------------- METHODES UTILITAIRES ---------------
  
  /**
   * Méthode utilitaire qui permet de verifier si le prix d'une pizza correspond
   * au minimum. Minimum : Somme des prix des ingrédients + 40 %. Le prix est
   * ensuite arrondi au dixième de centimes.
   * 
   * @param prix
   * @return prix_Min
   */
  public Double prixMinimalPizza() {
    Double sommePrix = 0.0;
    // Parcours la liste d'ingrédients pour faire la somme de ceux-ci
    for (Iterator<Ingredient> it = ingredients.iterator(); it.hasNext();) {
      Ingredient i = it.next();
      sommePrix += i.getPrix();
    }
    // (Prix : somme prix ingrédients + 40%) + conversion en centime grâce à
    // *100
    Double prix_40pourcent = (sommePrix * 1.4) * 100;
    // Arrondit au supérieur à la dizaine de centimes :
    Double prix_Min = Math.ceil(prix_40pourcent / 10.0) * 10.0 / 100;
    
    return prix_Min;
  }


  
  
}
