package pizzas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Représente une commande de pizzas effectuée par un client.
 * Une commande suit un cycle de vie : créée, validée, puis traitée.
 */
public class Commande implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * États possibles d'une commande.
     */
    public enum Etat { 
        /** La commande est en cours de création par le client. */
        CREEE, 
        /** La commande est validée par le client et envoyée au pizzaïolo. */
        VALIDEE, 
        /** Le pizzaïolo a traité la commande (pizzas prêtes). */
        TRAITEE 
    }

    /** Liste des pizzas associées à leur quantité dans la commande. */
    private Map<Pizza, Integer> pizzas = new HashMap<>();
    
    /** État actuel de la commande, initialisé à CREEE. */
    private Etat etat = Etat.CREEE;
    
    /** Le client ayant passé la commande. */
    private final CompteClient client;

    /**
     * Constructeur de la commande.
     * @param client Le client propriétaire de la commande.
     */
    public Commande(CompteClient client) {
        this.client = client;
    }

    /**
     * Ajoute une pizza à la commande si celle-ci est encore en cours de création.
     * * @param pizza La pizza à ajouter.
     * @param quantite Le nombre de pizzas de ce type.
     * @return true si l'ajout est réussi, false sinon.
     * @throws CommandeException si l'état de la commande ne permet plus de modification.
     */
    public boolean ajouterPizza(Pizza pizza, int quantite) throws CommandeException {
        if (this.etat != Etat.CREEE) {
            throw new CommandeException();
        }
        if (pizza == null || quantite <= 0) return false;
        
        this.pizzas.put(pizza, this.pizzas.getOrDefault(pizza, 0) + quantite);
        return true;
    }

    /**
     * Valide la commande. Une fois validée, elle est envoyée au pizzaïolo.
     * * @return true si la validation a réussi (commande non vide), false sinon.
     */
    public boolean valider() {
        if (this.etat == Etat.CREEE && !pizzas.isEmpty()) {
            this.etat = Etat.VALIDEE;
            return true;
        }
        return false;
    }

    /**
     * Marque la commande comme traitée par le pizzaïolo.
     * Cette action intervient après la lecture de la commande par le service pizzaïolo.
     */
    public void traiter() {
        if (this.etat == Etat.VALIDEE) {
            this.etat = Etat.TRAITEE;
        }
    }
    
    /**
     * Calcule le prix total de la commande en sommant le prix de chaque pizza.
     * * @return Le prix total de la commande.
     */
    public double getPrixTotal() {
        return pizzas.entrySet().stream()
                .mapToDouble(e -> e.getKey().getPrix() * e.getValue())
                .sum();
    }

    /** @return L'état actuel de la commande. */
    public Etat getEtat() { return etat; }

    /** @return Le client ayant passé la commande. */
    public CompteClient getClient() { return client; }

    /** @return La map des pizzas et leurs quantités. */
    public Map<Pizza, Integer> getPizzas() { return pizzas; }
}