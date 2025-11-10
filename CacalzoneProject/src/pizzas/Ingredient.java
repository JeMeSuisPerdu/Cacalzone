package pizzas;

public class Ingredient {
	//--------------- ATTRIBUTS ---------------
	String nom;
	Double prix;
	
	//--------------- CONSTRUCTEUR ---------------
	public Ingredient(String nom, Double prix) {
		super();
		this.nom = nom;
		this.prix = prix;
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
	
}
