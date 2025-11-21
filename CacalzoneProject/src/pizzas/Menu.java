package pizzas;

import java.util.Iterator;
import java.util.Set;

public class Menu {
	private Set<Pizza> pizzas;
	private Set<Ingredient> ingredients;
	
	public Menu(Set<Pizza> pizzas, Set<Ingredient> ingredients) {
		super();
		this.pizzas = pizzas;
		this.ingredients = ingredients;
	}

	// renvoie l'ingredient si l'ingredient est déjà présent dans la collection, sinon renvoie null
	public Ingredient ingredientExiste(String nom) {
		Iterator it = this.ingredients.iterator();
	    Ingredient ingredientExistant;
	    
	    while (it.hasNext()) {
	    	ingredientExistant = (Ingredient) it.next();
	    	if (nom.equals(ingredientExistant.getNom())) { return ingredientExistant; }
	    }
	    
	    return null;
	}
	
	// renvoie true si ne nom contient des lettres
	public static boolean nomValide(String nom) {
		return (nom != null && !nom.isBlank());
	}
	
	public int creerIngredient(String nom, double prix) {
		if (Menu.nomValide(nom)) { return -1; }
	    if (prix <= 0) { return -3; }
	    if (this.ingredientExiste(nom) != null) return -2;
	    
	    Ingredient nouvelIngredient = new Ingredient(nom, prix);
	    this.ingredients.add(nouvelIngredient);
	    return 0;
    }

	public Set<Pizza> getPizzas() {
		return pizzas;
	}

	public void setPizzas(Set<Pizza> pizzas) {
		this.pizzas = pizzas;
	}

	public Set<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(Set<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}
	
}
