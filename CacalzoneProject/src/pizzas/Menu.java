package pizzas;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

/**
 * Le Menu centralise toutes les données.
 */
public class Menu implements Serializable {

    private Set<Pizza> pizzas = new HashSet<>();
    private Set<Ingredient> ingredients = new HashSet<>();
    private List<Utilisateur> utilisateurs = new ArrayList<>();
    private List<Commande> commandesGlobales = new ArrayList<>();

    public Menu() {
        utilisateurs.add(new ComptePizzaiolo("chef@pizza.fr", "admin", 
                new InformationPersonnelle("Mario", "Chef")));
    }

    public Utilisateur authentifier(String email, String mdp) {
        for (Utilisateur u : utilisateurs) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getMotDePasse().equals(mdp)) {
                return u;
            }
        }
        return null;
    }

    public Utilisateur trouverUtilisateur(String email) {
        for (Utilisateur u : utilisateurs) {
            if (u.getEmail().equalsIgnoreCase(email)) return u;
        }
        return null;
    }

    public void ajouterUtilisateur(Utilisateur u) {
        this.utilisateurs.add(u);
    }

    public void ajouterCommande(Commande c) {
        this.commandesGlobales.add(c);
    }

    public Set<Pizza> getPizzas() { return pizzas; }
    public Set<Ingredient> getIngredients() { return ingredients; }
    public List<Commande> getCommandesGlobales() { return commandesGlobales; }
    public List<Utilisateur> getUtilisateurs() { return utilisateurs; }
}