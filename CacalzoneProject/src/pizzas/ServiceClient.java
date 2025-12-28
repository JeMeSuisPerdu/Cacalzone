package pizzas;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Service gérant la logique des clients en utilisant le Menu.
 */
public class ServiceClient implements InterClient {
    private final Menu menu;
    private CompteClient clientConnecte;
    private TypePizza filtreType;
    private double filtrePrixMax = Double.MAX_VALUE;
    private Set<String> ingredientsExclus = new HashSet<>();
    
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

    @Override 
    public void ajouterPizza(Pizza p, int n, Commande c) throws CommandeException {
        if(c == null || p == null) {
            throw new CommandeException();
        }
        c.ajouterPizza(p, n); 
    }

    @Override 
    public void annulerCommande(Commande c) {
        if (c != null) {
            c.annuler();
        }
    }

    @Override 
    public List<Commande> getCommandesEncours() {
        if (clientConnecte == null) return new ArrayList<>();
        
        List<Commande> enCours = new ArrayList<>();
        for (Commande c : clientConnecte.getHistoriqueCommandes()) {
            if (c.getEtat() == Commande.Etat.VALIDEE) {
                enCours.add(c);
            }
        }
        return enCours;
    }
    
    @Override 
    public List<Commande> getCommandePassees() { 
        return (clientConnecte != null) ? clientConnecte.getHistoriqueCommandes() : new ArrayList<>(); 
    }
    
    @Override 
    public void ajouterFiltre(TypePizza t) {
        this.filtreType = t;
    }
    
    @Override 
    public void ajouterFiltre(String... ingredients) {
        for (String s : ingredients) {
            this.ingredientsExclus.add(s.toLowerCase());
        }
    }
    
    @Override 
    public void ajouterFiltre(double p) {
        this.filtrePrixMax = p;
    }
    
    @Override 
    public Set<Pizza> selectionPizzaFiltres() {
        Set<Pizza> resultat = new HashSet<>();
        for (Pizza p : menu.getPizzas()) {
            if (p.getPrix() > filtrePrixMax) continue;
            
            if (filtreType != null && p.getTypePizza() != filtreType) continue;
            
            boolean contientExclu = false;
            for (Ingredient ing : p.getIngredients()) {
                if (ingredientsExclus.contains(ing.getNom().toLowerCase())) {
                    contientExclu = true;
                    break;
                }
            }
            
            if (!contientExclu) {
                resultat.add(p);
            }
        }
        return resultat;
    }
    
    @Override 
    public void supprimerFiltres() {
        this.filtreType = null;
        this.filtrePrixMax = Double.MAX_VALUE;
        this.ingredientsExclus.clear();
    }
    
    @Override 
    public Set<Evaluation> getEvaluationsPizza(Pizza p) {
        return (p != null) ? p.getEvaluations() : new HashSet<>();
    }

    @Override 
    public double getNoteMoyenne(Pizza p) {
        if (p == null || p.getEvaluations().isEmpty()) return 0.0;
        
        double somme = 0;
        for (Evaluation e : p.getEvaluations()) {
            somme += e.getNote();
        }
        return somme / p.getEvaluations().size();
    }

    @Override 
    public boolean ajouterEvaluation(Pizza p, int note, String commentaire) {
        if (clientConnecte == null || p == null) return false;
        Evaluation eval = new Evaluation(note, commentaire, clientConnecte.getEmail());
        p.getEvaluations().add(eval);
        return true;
    }
    
}