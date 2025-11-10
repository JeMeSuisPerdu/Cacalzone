package pizzas;

import java.util.List;
import java.util.Set;

public class Client implements InterClient {
	
	//--------------------METHODES IMPLEMENTEES DE INTERCLIENT------------------------
	@Override
	public int inscription(String email, String mdp, InformationPersonnelle info) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean connexion(String email, String mdp) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void deconnexion() throws NonConnecteException {
		// TODO Auto-generated method stub

	}

	@Override
	public Commande debuterCommande() throws NonConnecteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void ajouterPizza(Pizza pizza, int nombre, Commande cmd) throws NonConnecteException, CommandeException {
		cmd.ajouterPizza(pizza);
	}

	@Override
	public void validerCommande(Commande cmd) throws NonConnecteException, CommandeException {
		// TODO Auto-generated method stub

	}

	@Override
	public void annulerCommande(Commande cmd) throws NonConnecteException, CommandeException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Commande> getCommandesEncours() throws NonConnecteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Commande> getCommandePassees() throws NonConnecteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Pizza> getPizzas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void ajouterFiltre(TypePizza type) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ajouterFiltre(String... ingredients) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ajouterFiltre(double prixMaximum) {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<Pizza> selectionPizzaFiltres() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void supprimerFiltres() {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<Evaluation> getEvaluationsPizza(Pizza pizza) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getNoteMoyenne(Pizza pizza) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean ajouterEvaluation(Pizza pizza, int note, String commentaire)
			throws NonConnecteException, CommandeException {
		// TODO Auto-generated method stub
		return false;
	}

}
