package pizzas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Représente une commande de pizzas passée par un client.
 */
public class Commande implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Enumération pour les différents états possibles d'une commande.
     */
    public enum Etat {
        CREEE,     
        VALIDEE,   
        TRAITEE    
    }

    /**
     * Liste des pizzas incluses dans cette commande.
     */
    private final List<Pizza> pizzas;

    /**
     * L'état actuel de la commande.
     */
    private Etat etat;

    /**
     * L'email du client qui a passé la commande.
     */
    private final String emailClient;

    /**
     * Constructeur pour créer une nouvelle commande.
     * une nouvelle commande est toujours dans l'état CREEE.
     *
     * @param emailClient l'email du client qui passe la commande
     * @throws IllegalArgumentException si l'email du client est null ou vide
     */
    public Commande(String emailClient) throws IllegalArgumentException {
        if (emailClient == null || emailClient.trim().isEmpty()) {
            throw new IllegalArgumentException("L'email du client ne peut pas être null ou vide.");
        }
        this.emailClient = emailClient;
        this.pizzas = new ArrayList<>();
        this.etat = Etat.CREEE;
    }

    /**
     * Ajoute une pizza à la commande si elle est dans l'état CREEE.
     *
     * @param pizza la pizza à ajouter
     * @return vrai si la pizza a été ajoutée, false sinon 
     * @throws CommandeException si la commande n'est pas dans l'état CREEE
     */
    public boolean ajouterPizza(Pizza pizza) throws CommandeException {
        if (this.etat != Etat.CREEE) {
            throw new CommandeException();
        }
        if (pizza == null) {
            return false;
        }
        return this.pizzas.add(pizza);
    }

    /**
     * Valide la commande, change l'état de CREEE à VALIDEE.
     *
     * @return vrai si la commande a été validée, false sinon
     */
    public boolean valider() {
        if (this.etat == Etat.CREEE) {
            this.etat = Etat.VALIDEE;
            return true;
        }
        return false;
    }

    /**
     * Marque la commande comme traitée, change l'état de VALIDEE à TRAITEE.
     * Cette méthode sera utilisée par le Pizzaïolo.
     *
     * @return vrai si la commande a été traitée, false sinon 
     */
    public boolean traiter() {
        if (this.etat == Etat.VALIDEE) {
            this.etat = Etat.TRAITEE;
            return true;
        }
        return false;
    }

    /**
     * Calcule le prix total de la commande.
     * Nécessite que la classe Pizza ait une méthode getPrixDeVente().
     *
     * @return le prix total de la commande
     */
    public double getPrixTotal() {
        double total = 0.0;
        for (Pizza pizza : this.pizzas) {
            
        }
        return total;
    }

    // --- Les Getters ---

    /**
     * Renvoie la liste des pizzas de la commande.
     *
     * @return la liste des pizzas
     */
    public List<Pizza> getPizzas() {
        return new ArrayList<>(this.pizzas); 
    }

    /**
     * Renvoie l'état actuel de la commande.
     *
     * @return l'état de la commande
     */
    public Etat getEtat() {
        return etat;
    }

    /**
     * Renvoie l'email du client qui a passé la commande.
     *
     * @return l'email du client
     */

    public String getEmailClient() {
        return emailClient;
    }

    // --- LES METHODES POUR PERMETTRE LA COMPARAISON ET L'AFFICHAGE ---

    @Override
    public int hashCode() {
        return Objects.hash(etat, pizzas, emailClient);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Commande other = (Commande) obj;
        return etat == other.etat && Objects.equals(pizzas, other.pizzas)
                && Objects.equals(emailClient, other.emailClient);
    }

    @Override
    public String toString() {
        return "Commande [etat=" + etat + ", emailClient=" + emailClient + ", nbPizzas=" + pizzas.size() + "]";
    }
}

























