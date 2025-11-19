package pizzas;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Classe Pizzaiolo implémentant l'interface InterPizzaiolo.  
 * Le pizzaiolo est responsable de la gestion des ingrédients, des pizzas,
 * de la modification de leurs prix, de l’ajout de photos ainsi que
 * du suivi et de l'analyse des commandes.
 */
public class Pizzaiolo implements InterPizzaiolo {
	
	//---------------------METHODES IMPLEMENTEES DE INTERPIZZAIOLO-------------------------

	/**
	 * Crée un ingrédient à partir de son nom et de son prix.
	 *
	 * @param nom nom de l'ingrédient
	 * @param prix prix de l'ingrédient
	 * @return un code d'état indiquant si la création a réussi
	 */
	@Override
	public int creerIngredient(String nom, double prix) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Modifie le prix d'un ingrédient existant.
	 *
	 * @param nom nom de l'ingrédient à modifier
	 * @param prix nouveau prix
	 * @return code d'état indiquant si le changement a bien été effectué
	 */
	@Override
	public int changerPrixIngredient(String nom, double prix) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Interdit un ingrédient pour un type de pizza donné.
	 *
	 * @param nomIngredient nom de l'ingrédient
	 * @param type type de pizza auquel l'ingrédient est interdit
	 * @return true si l'opération a réussi
	 */
	@Override
	public boolean interdireIngredient(String nomIngredient, TypePizza type) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Crée une nouvelle pizza avec son nom et son type.
	 *
	 * @param nom nom de la pizza
	 * @param type type de pizza
	 * @return la pizza créée
	 */
	@Override
	public Pizza creerPizza(String nom, TypePizza type) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Ajoute un ingrédient à une pizza existante.
	 *
	 * @param pizza pizza cible
	 * @param nomIngredient nom de l'ingrédient à ajouter
	 * @return code d'état indiquant si l'ajout a réussi
	 */
	@Override
	public int ajouterIngredientPizza(Pizza pizza, String nomIngredient) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Retire un ingrédient d'une pizza.
	 *
	 * @param pizza pizza cible
	 * @param nomIngredient nom de l'ingrédient à retirer
	 * @return code d'état indiquant si le retrait a réussi
	 */
	@Override
	public int retirerIngredientPizza(Pizza pizza, String nomIngredient) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Vérifie la liste des ingrédients d'une pizza.
	 *
	 * @param pizza pizza à inspecter
	 * @return ensemble des noms d’ingrédients présents
	 */
	@Override
	public Set<String> verifierIngredientsPizza(Pizza pizza) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Ajoute une photo à une pizza.
	 *
	 * @param pizza pizza cible
	 * @param file chemin du fichier image
	 * @return true si la photo a été ajoutée
	 * @throws IOException si le fichier ne peut être lu ou traité
	 */
	@Override
	public boolean ajouterPhoto(Pizza pizza, String file) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Renvoie le prix actuel d'une pizza.
	 *
	 * @param pizza pizza dont on veut le prix
	 * @return prix de vente
	 */
	@Override
	public double getPrixPizza(Pizza pizza) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Modifie le prix de vente d'une pizza.
	 *
	 * @param pizza pizza cible
	 * @param prix nouveau prix
	 * @return true si la modification a réussi
	 */
	@Override
	public boolean setPrixPizza(Pizza pizza, double prix) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Calcule le prix minimal d'une pizza d'après ses ingrédients.
	 *
	 * @param pizza pizza cible
	 * @return prix minimal
	 */
	@Override
	public double calculerPrixMinimalPizza(Pizza pizza) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Renvoie l'ensemble des pizzas existantes.
	 *
	 * @return ensemble des pizzas
	 */
	@Override
	public Set<Pizza> getPizzas() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Renvoie l'ensemble des clients enregistrés.
	 *
	 * @return ensemble des informations personnelles des clients
	 */
	@Override
	public Set<InformationPersonnelle> ensembleClients() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Renvoie la liste des commandes déjà traitées.
	 *
	 * @return liste des commandes traitées
	 */
	@Override
	public List<Commande> commandesDejaTraitees() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Renvoie la liste des commandes encore non traitées.
	 *
	 * @return liste des commandes non traitées
	 */
	@Override
	public List<Commande> commandeNonTraitees() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Renvoie la liste des commandes déjà traitées pour un client donné.
	 *
	 * @param client informations du client
	 * @return liste des commandes traitées de ce client
	 */
	@Override
	public List<Commande> commandesTraiteesClient(InformationPersonnelle client) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Calcule le bénéfice réalisé par pizza.
	 *
	 * @return une map associant pizza → bénéfice généré
	 */
	@Override
	public Map<Pizza, Double> beneficeParPizza() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Calcule le bénéfice généré par une commande.
	 *
	 * @param commande commande à analyser
	 * @return bénéfice de cette commande
	 */
	@Override
	public double beneficeCommandes(Commande commande) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Calcule le bénéfice total sur toutes les commandes.
	 *
	 * @return bénéfice global
	 */
	@Override
	public double beneficeToutesCommandes() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Renvoie pour chaque client le nombre de pizzas commandées.
	 *
	 * @return map client → nombre de pizzas commandées
	 */
	@Override
	public Map<InformationPersonnelle, Integer> nombrePizzasCommandeesParClient() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Renvoie pour chaque client le bénéfice généré.
	 *
	 * @return map client → bénéfice total
	 */
	@Override
	public Map<InformationPersonnelle, Double> beneficeParClient() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Renvoie le nombre total de commandes contenant une pizza donnée.
	 *
	 * @param pizza pizza à analyser
	 * @return nombre de commandes contenant cette pizza
	 */
	@Override
	public int nombrePizzasCommandees(Pizza pizza) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Renvoie un classement des pizzas selon le nombre de fois où elles ont été commandées.
	 *
	 * @return liste de pizzas triées de la plus vendue à la moins vendue
	 */
	@Override
	public List<Pizza> classementPizzasParNombreCommandes() {
		// TODO Auto-generated method stub
		return null;
	}

}
