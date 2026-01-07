package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.*;

/**
 * Classe de tests unitaires dédiée à la validation du comportement
 * de la classe {@link Commande}.
 * <p>
 * Ces tests permettent de s'assurer que :
 * <ul>
 *   <li>une commande est correctement initialisée</li>
 *   <li>les règles métier liées aux états sont respectées</li>
 *   <li>le calcul du prix total est fiable</li>
 *   <li>les modifications sont interdites après validation</li>
 * </ul>
 * <p>
 * L'ensemble des scénarios testés représente des cas d'usage réels
 * d'un système de commande de pizzas.
 *
 * @author Safiatou
 */
public class TestCommande {

    /** Commande testée dans les différents scénarios */
    private Commande commande;

    /** Compte client associé à la commande */
    private CompteClient client;

    /** Première pizza utilisée pour les tests */
    private Pizza pizza1;

    /** Seconde pizza utilisée pour les tests */
    private Pizza pizza2;

    /**
     * Méthode exécutée avant chaque test.
     * <p>
     * Elle garantit que chaque test démarre avec un environnement
     * propre et indépendant :
     * <ul>
     *   <li>création d'un client avec ses informations personnelles</li>
     *   <li>création d'une nouvelle commande associée à ce client</li>
     *   <li>préparation de pizzas standards pour les scénarios</li>
     * </ul>
     * Cette approche améliore la fiabilité et la reproductibilité des tests.
     */
    @BeforeEach
    public void setUp() {
        // Création des informations personnelles du client
        InformationPersonnelle infoSD =
                new InformationPersonnelle("Diallo", "Safiatou");

        // Création du compte client
        client = new CompteClient("sd@ubo.fr", "diallo2024", infoSD);

        // Initialisation d'une nouvelle commande liée au client
        commande = new Commande(client);

        // Création de pizzas utilisées pour alimenter le panier
        pizza1 = new Pizza("Marguerite", TypePizza.Vegetarienne);
        pizza2 = new Pizza("Reine", TypePizza.Viande);
    }

    /**
     * Teste l'état initial d'une commande.
     * <p>
     * Lors de sa création, une commande doit obligatoirement être
     * dans l'état {@code CREEE}, ce qui permet :
     * <ul>
     *   <li>l'ajout de pizzas</li>
     *   <li>la modification du panier</li>
     * </ul>
     * Ce test garantit que la logique métier est respectée dès
     * l'instanciation de l'objet.
     */
    @Test
    public void testEtatInitial() {
        assertEquals(Commande.Etat.CREEE, commande.getEtat(),
                "L'état initial doit impérativement être CREEE");
    }

    /**
     * Vérifie la cohérence du calcul du prix total d'une commande.
     * <p>
     * Le montant final doit être égal à :
     * <pre>
     * prix unitaire de la pizza × quantité commandée
     * </pre>
     * Ce test s'assure que le moteur de calcul ne génère aucune erreur
     * financière, même lors de l'ajout de plusieurs unités d'un produit.
     *
     * @throws CommandeException si l'ajout de la pizza est refusé
     */
    @Test
    public void testAjoutPizzaEtPrix() throws CommandeException {
        double prixP1 = pizza1.getPrix();

        // Ajout de deux pizzas identiques dans la commande
        commande.ajouterPizza(pizza1, 2);

        // Vérification précise du prix total avec une tolérance monétaire
        assertEquals(prixP1 * 2, commande.getPrixTotal(), 0.01,
                "Le prix total doit être : quantité × prix pizza");
    }

    /**
     * Teste le changement d'état d'une commande vers l'état {@code VALIDEE}.
     * <p>
     * Une commande ne peut être validée que si :
     * <ul>
     *   <li>elle contient au moins une pizza</li>
     *   <li>elle est encore à l'état {@code CREEE}</li>
     * </ul>
     * Ce test confirme que la validation est autorisée
     * lorsque les conditions métier sont réunies.
     *
     * @throws CommandeException si l'ajout de la pizza échoue
     */
    @Test
    public void testChangementEtat() throws CommandeException {
        commande.ajouterPizza(pizza1, 1);

        assertTrue(commande.valider(),
                "La commande de SD doit pouvoir être validée car elle n'est pas vide");

        assertEquals(Commande.Etat.VALIDEE, commande.getEtat());
    }

    /**
     * Vérifie l'interdiction de modification d'une commande validée.
     * <p>
     * Une fois qu'une commande est passée à l'état {@code VALIDEE} :
     * <ul>
     *   <li>aucune pizza ne peut être ajoutée</li>
     *   <li>l'intégrité de la commande est protégée</li>
     * </ul>
     * Le système doit donc lever une exception pour empêcher
     * toute tentative de modification.
     *
     * @throws CommandeException exception attendue lors de l'ajout
     */
    @Test
    public void testAjoutPizzaApresValidation() throws CommandeException {
        commande.ajouterPizza(pizza1, 1);
        commande.valider();

        // Vérification que le système bloque correctement les modifications
        assertThrows(CommandeException.class, () -> {
            commande.ajouterPizza(pizza2, 1);
        }, "Le système doit empêcher toute modification après validation");
    }
}
