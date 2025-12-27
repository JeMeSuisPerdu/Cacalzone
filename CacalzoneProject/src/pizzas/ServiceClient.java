package pizzas;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;

/**
 * Service gérant la logique des clients en utilisant le Menu.
 */
public class ServiceClient implements InterClient {
    private final Menu menu;
    private CompteClient clientConnecte;

    public ServiceClient(Menu menu) {
        this.menu = menu;
    }

    @Override
    public int inscription(String email, String mdp, InformationPersonnelle info) {
        if (email == null || mdp == null || info == null) return -3;
        if (menu.trouverUtilisateur(email) != null) return -1;
        
        CompteClient nouveau = new CompteClient(email, mdp, info);
        menu.ajouterUtilisateur(nouveau);
        return 0;
    }

    @Override
    public boolean connexion(String email, String mdp) {
        Utilisateur u = menu.authentifier(email, mdp);
        if (u instanceof CompteClient) {
            this.clientConnecte = (CompteClient) u;
            return true;
        }
        return false;
    }

    @Override
    public void deconnexion() throws NonConnecteException {
        if (clientConnecte == null) throw new NonConnecteException();
        this.clientConnecte = null;
    }

    @Override
    public Commande debuterCommande() throws NonConnecteException {
        if (clientConnecte == null) throw new NonConnecteException();
        return new Commande(clientConnecte); 
    }

    @Override
    public void validerCommande(Commande cmd) throws NonConnecteException, CommandeException {
        if (clientConnecte == null) throw new NonConnecteException();
        if (cmd.valider()) {
            menu.ajouterCommande(cmd);
            clientConnecte.ajouterCommande(cmd);
        }
    }

    @Override
    public Set<Pizza> getPizzas() {
        return menu.getPizzas();
    }

    @Override public void ajouterPizza(Pizza p, int n, Commande c) throws CommandeException {
    	if(c.equals(null) || p.equals(null)) {
    		throw new CommandeException();
    	}
    	c.ajouterPizza(p, n); 
    }
    
    @Override public void annulerCommande(Commande c) {}
    @Override public List<Commande> getCommandesEncours() { return null; }
    @Override public List<Commande> getCommandePassees() { return clientConnecte.getHistoriqueCommandes(); }
    @Override public void ajouterFiltre(TypePizza t) {}
    @Override public void ajouterFiltre(String... i) {}
    @Override public void ajouterFiltre(double p) {}
    @Override public Set<Pizza> selectionPizzaFiltres() { return null; }
    @Override public void supprimerFiltres() {}
    @Override public Set<Evaluation> getEvaluationsPizza(Pizza p) { return null; }
    @Override public double getNoteMoyenne(Pizza p) { return 0; }
    @Override public boolean ajouterEvaluation(Pizza p, int n, String c) { return false; }
}