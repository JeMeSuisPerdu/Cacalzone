package pizzas;

import java.util.HashSet;
import java.util.Set;

public class Ingredient {
	//--------------- ATTRIBUTS ---------------
	String nom;
	Double prix;
	Set<TypePizza> typePizzaInterdit;
	//--------------- CONSTRUCTEUR ---------------
	public Ingredient(String nom, Double prix,Set<TypePizza> typesPizzaInterdits) {
		super();
		this.nom = nom;
		this.prix = prix;
		this.typePizzaInterdit = new HashSet<>(typesPizzaInterdits);
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

  public Set<TypePizza> getTypePizzaInterdit() {
    return typePizzaInterdit;
  }

  public void setTypePizzaInterdit(Set<TypePizza> typePizzaInterdit) {
    this.typePizzaInterdit = typePizzaInterdit;
  }
	
}
