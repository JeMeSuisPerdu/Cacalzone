package pizzas;

import java.util.HashSet;
import java.util.Set;

public class Ingredient {
	//--------------- ATTRIBUTS ---------------
	String nom;
	Double prix;
	Set<TypePizza> typePizza;
	//--------------- CONSTRUCTEUR ---------------
	public Ingredient(String nom, Double prix,Set<TypePizza> typePizza) {
		super();
		this.nom = nom;
		this.prix = prix;
		this.typePizza = new HashSet<>(typePizza);
	}
	
	//--------------- GETTERS // SETTERS ---------------
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public Double getPrix() {
		return prix;
	}
	public void setPrix(Double prix) {
		this.prix = prix;
	}

  public Set<TypePizza> getTypePizza() {
    return typePizza;
  }

  public void setTypePizza(Set<TypePizza> typePizza) {
    this.typePizza = typePizza;
  }
	
}
