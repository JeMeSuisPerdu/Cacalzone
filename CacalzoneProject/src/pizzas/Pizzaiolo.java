package pizzas;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Pizzaiolo implements InterPizzaiolo {
    private Menu menu;
    
    // ----------------------------- CONSTRUCTEUR ---------------------------

    public Pizzaiolo(Menu menu) {
		super();
		this.menu = menu;
	}

	//---------------------METHODES IMPLEMENTEES DE INTERPIZZAIOLO-------------------------
    
    @Override
    public int creerIngredient(String nom, double prix) {
        return menu.creerIngredient(nom, prix); 
    }

    @Override
    public int changerPrixIngredient(String nom, double prix) {
    	if (Menu.nomValide(nom)) { return -1; }
	    if (prix <= 0) { return -2; }
	    
	    Ingredient ingredient = menu.ingredientExiste(nom); // récupère l'objet Ingredient
	    if (ingredient == null) return -3;
	    
	    ingredient.setPrix(prix);
	    return 0;
    }

    @Override
    public boolean interdireIngredient(String nomIngredient, TypePizza type) {
    	Ingredient ingredient = menu.ingredientExiste(nomIngredient);
	    if (ingredient == null) return false;
	    
	    ingredient.addTypePizzaInterdit(type);
	    return true;
    }

    @Override
    public Pizza creerPizza(String nom, TypePizza type) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int ajouterIngredientPizza(Pizza pizza, String nomIngredient) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int retirerIngredientPizza(Pizza pizza, String nomIngredient) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Set<String> verifierIngredientsPizza(Pizza pizza) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean ajouterPhoto(Pizza pizza, String file) throws IOException {
    	if (!menu.getPizzas().contains(pizza) || file.isBlank()) return false;
    	
    	// Extensions d'images possibles.
	    String[] exts = {".png", ".jpg", ".jpeg", ".webp"};
	    boolean estFichierValide = false;
	    
	    for (String ext : exts) {
	        if (file.endsWith(ext)) { // Vérifie que le fichier est bien une photo.
	            estFichierValide = true;
	            break;
	        }
	    }
	    
	    if (!estFichierValide == true) return false;
	    
	    // Vérifie que le fichier existe vraiment.
	    java.io.File f = new java.io.File(file);
	    if (!f.isFile()) return false;
	    
	    pizza.setPhoto(file);
	    return true;
    }

    @Override
    public double getPrixPizza(Pizza pizza) {
    	return (menu.getPizzas().contains(pizza)) ? pizza.getPrix() : -1;
    }

    @Override
    public boolean setPrixPizza(Pizza pizza, double prix) {
    	if (prix >= pizza.getPrixMinimalPizza() && menu.getPizzas().contains(pizza)) {
			pizza.setPrix(prix);
			return true;
		} else return false;
    }

    @Override
    public double calculerPrixMinimalPizza(Pizza pizza) {
    	return (menu.getPizzas().contains(pizza)) 
    			? pizza.getPrixMinimalPizza() 
    			: -1;
    }

    @Override
    public Set<Pizza> getPizzas() {
        return menu.getPizzas();
    }

    @Override
    public Set<InformationPersonnelle> ensembleClients() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Commande> commandesDejaTraitees() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Commande> commandeNonTraitees() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Commande> commandesTraiteesClient(InformationPersonnelle client) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<Pizza, Double> beneficeParPizza() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public double beneficeCommandes(Commande commande) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double beneficeToutesCommandes() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Map<InformationPersonnelle, Integer> nombrePizzasCommandeesParClient() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<InformationPersonnelle, Double> beneficeParClient() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int nombrePizzasCommandees(Pizza pizza) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<Pizza> classementPizzasParNombreCommandes() {
        // TODO Auto-generated method stub
        return null;
    }

}
