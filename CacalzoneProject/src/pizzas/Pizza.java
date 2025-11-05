package pizzas;

// A compléter
/**
 * Classe Pizza définit par un nom et un type de pizza
 *
 * @author Anis LAFRAD
 *
 */
public class Pizza {
	//--------------- ATTRIBUTS ---------------
	String nom;
	TypePizza untype;
	//--------------- CONSTRUCTEUR ---------------
	public Pizza(String nom, TypePizza untype) {
		super();
		this.nom = nom;
		this.untype = untype;
	}
	//--------------- GETTERS // SETTERS ---------------
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public TypePizza getUntype() {
		return untype;
	}
	public void setUntype(TypePizza untype) {
		this.untype = untype;
	}
	
  
}
