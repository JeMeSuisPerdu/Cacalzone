package pizzas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

/**
 * Class qui représente une commande de pizzas effectuée par un client.
 */
public class Commande implements Serializable {

	
    /**
     * États possibles d'une commande.
     */
    public enum Etat { 
        /** La commande est en cours de création par le client. */
        CREEE, 
        /** La commande est validée par le client et envoyée au pizzaïolo. */
        VALIDEE, 
        /** Le pizzaïolo a traité la commande (pizzas prêtes). */
        TRAITEE,
        /** La commande est annulée et ne sera pas prise en compte pour les calculs du ServiceClient */
        ANNULEE
    }

    /** Liste des pizzas associées à leur quantité dans la commande. */
    private Map<Pizza, Integer> pizzas = new HashMap<>();
    
    /** État actuel de la commande, initialisé à CREEE. */
    private Etat etat = Etat.CREEE;
    
    /** Le client ayant passé la commande. */
    private final CompteClient client;

    private LocalDateTime date = LocalDateTime.now();

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
     */
    public void traiter() {
        if (this.etat == Etat.VALIDEE) {
            this.etat = Etat.TRAITEE;
        }
    }
    
    /**
     * Permet d'annuler la commande si elle n'est pas à l'état TRAITEE.
     * Vide la liste des pizzas de la commande actuelle.
     * Met la commande à annuler (utile pour les méthodes dans ServiceClient).
     */
    public void annuler() {
        if (this.etat == Etat.CREEE || this.etat == Etat.VALIDEE) {
            this.pizzas.clear();
            this.etat = Etat.ANNULEE;
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

	public String getDate() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		return date.format(format);
	}
}