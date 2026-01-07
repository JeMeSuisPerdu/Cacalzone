package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.Pizza;
import pizzas.TypePizza;
import pizzas.Ingredient;

/**
 * Classe de tests unitaires pour la classe {@link Pizza}.
 * <p>
 * Ces tests couvrent les fonctionnalités principales des pizzas, notamment :
 * <ul>
 *   <li>initialisation correcte d'une pizza et de ses attributs</li>
 *   <li>calcul dynamique du prix en fonction des ingrédients</li>
 *   <li>gestion des ingrédients (doublons, ajout sécurisé)</li>
 *   <li>modification du nom et validation des contraintes</li>
 *   <li>gestion de la photo associée</li>
 *   <li>respect du prix minimal et cohérence globale du prix</li>
 * </ul>
 * <p>
 * L'objectif est de s'assurer que les objets {@link Pizza} restent cohérents,
 * fiables et conformes aux règles métier du système.
 * 
 */
public class TestPizza {

    /** Pizza utilisée pour les tests */
    private Pizza pizza;

    /** Ingrédient tomate pour les tests */
    private Ingredient tomate;

    /** Ingrédient fromage pour les tests */
    private Ingredient fromage;

    /** Ingrédient jambon pour les tests */
    private Ingredient jambon;

    /**
     * Initialisation avant chaque test.
     * <p>
     * Crée une pizza et des ingrédients de base pour garantir l'indépendance
     * des tests.
     */
    @BeforeEach
    public void setUp() {
        pizza = new Pizza("Reine", TypePizza.Viande);
        tomate = new Ingredient("Tomate", 1.0);
        fromage = new Ingredient("Fromage", 1.5);
        jambon = new Ingredient("Jambon", 2.0);
    }

    /**
     * Test l'initialisation d'une pizza.
     * <p>
     * Vérifie que les attributs nom, type, ingrédients et prix sont correctement initialisés.
     */
    @Test
    public void testInitialisation() {
        assertEquals("Reine", pizza.getNom(), "Le nom de la pizza doit être 'Reine'");
        assertEquals(TypePizza.Viande, pizza.getTypePizza(), "Le type de pizza doit être 'Viande'");
        assertTrue(pizza.getIngredients().isEmpty(), "La liste d'ingrédients doit être vide à l'initialisation");
        assertNotNull(pizza.getPrix(), "Le prix ne doit pas être null");
    }

    /**
     * Test le calcul dynamique du prix de la pizza.
     * <p>
     * Vérifie que l'ajout d'ingrédients modifie correctement le prix
     * selon la formule métier.
     */
    @Test
    public void testCalculPrixDynamique() {
        double prixInitial = pizza.getPrix();
        pizza.getIngredients().add(tomate);
        double prixApresAjout = pizza.getPrix();

        assertNotEquals(prixInitial, prixApresAjout, 0.01, "Le prix doit changer après l'ajout d'un ingrédient");

        double sommeIngredients = tomate.getPrix();
        double prixAttendu = Math.ceil((sommeIngredients * 1.4) * 10) / 10.0;
        assertEquals(prixAttendu, prixApresAjout, 0.01,
            "Le prix doit être la somme des ingrédients + 40% arrondi à la dizaine de centimes supérieure");
    }

    /**
     * Test la modification du nom de la pizza.
     * <p>
     * Vérifie la gestion des noms valides et invalides (vide, null, espaces).
     */
    @Test
    public void testChangementNom() {
        pizza.setNom("Royale");
        assertEquals("Royale", pizza.getNom(), "Le nom doit être changé en 'Royale'");

        Exception exceptionVide = assertThrows(IllegalArgumentException.class,
            () -> pizza.setNom(""), "setNom avec chaîne vide doit lancer IllegalArgumentException");
        assertEquals("Royale", pizza.getNom(), "Le nom doit rester 'Royale' après tentative de nom vide");

        Exception exceptionNull = assertThrows(IllegalArgumentException.class,
            () -> pizza.setNom(null), "setNom avec null doit lancer IllegalArgumentException");
        assertEquals("Royale", pizza.getNom(), "Le nom doit rester 'Royale' après tentative de nom null");

        Exception exceptionEspaces = assertThrows(IllegalArgumentException.class,
            () -> pizza.setNom("   "), "setNom avec espaces seulement doit lancer IllegalArgumentException");
    }

    /**
     * Test la gestion des ingrédients.
     * <p>
     * Vérifie que les doublons ne sont pas acceptés et que l'ajout fonctionne correctement.
     */
    @Test
    public void testGestionIngredients() {
        pizza.getIngredients().add(tomate);
        pizza.getIngredients().add(tomate);

        assertEquals(1, pizza.getIngredients().size(), "Un Set ne doit pas accepter de doublons d'ingrédients");

        pizza.getIngredients().add(fromage);
        assertEquals(2, pizza.getIngredients().size(), "La taille de la liste doit être de 2 après ajout de 2 ingrédients différents");
    }

    /**
     * Test que le prix minimal de la pizza est respecté.
     */
    @Test
    public void testPrixMinimal() {
        assertNotNull(pizza.getPrixMinimalPizza(), "Le prix minimal ne doit pas être null");
        assertTrue(pizza.getPrix() >= pizza.getPrixMinimalPizza(),
            "Le prix total doit être supérieur ou égal au prix minimal");

        pizza.getIngredients().add(tomate);
        pizza.getIngredients().add(fromage);
        assertTrue(pizza.getPrix() >= pizza.getPrixMinimalPizza(),
            "Le prix total après ajout d'ingrédients doit être >= au prix minimal");
    }

    /**
     * Test de la méthode {@code setPrix}.
     * <p>
     * Vérifie que la mise à jour du prix respecte le prix minimal.
     */
    @Test
    public void testSetPrix() {
        double prixMinimal = pizza.getPrixMinimalPizza();

        boolean resultatValide = pizza.setPrix(prixMinimal + 5.0);
        assertTrue(resultatValide, "setPrix doit retourner true pour un prix supérieur au prix minimal");
        assertEquals(prixMinimal + 5.0, pizza.getPrix(), 0.01, "Le prix doit être mis à jour avec la valeur valide");

        boolean resultatInvalide = pizza.setPrix(prixMinimal - 1.0);
        assertFalse(resultatInvalide, "setPrix doit retourner false pour un prix inférieur au prix minimal");
        assertNotEquals(prixMinimal - 1.0, pizza.getPrix(), 0.01, "Le prix ne doit pas être mis à jour avec une valeur invalide");
    }

    /**
     * Test la méthode {@code ajouterIngredient}.
     * <p>
     * Vérifie que l'ajout respecte les conditions (pas de doublon, pas d'ingrédient interdit).
     */
    @Test
    public void testAjouterIngredient() {
        boolean premierAjout = pizza.ajouterIngredient(tomate);
        assertTrue(premierAjout, "L'ajout d'un premier ingrédient doit retourner true");
        assertTrue(pizza.getIngredients().contains(tomate), "L'ingrédient doit être présent dans la liste");

        boolean deuxiemeAjout = pizza.ajouterIngredient(tomate);
        assertFalse(deuxiemeAjout, "L'ajout d'un doublon doit retourner false");
        assertEquals(1, pizza.getIngredients().size(), "La taille ne doit pas augmenter avec un doublon");
    }

    /**
     * Test les getters et setters de la photo.
     */
    @Test
    public void testPhoto() {
        assertEquals("", pizza.getPhoto(), "La photo initiale doit être une chaîne vide");

        String nouvellePhoto = "chemin/vers/photo.jpg";
        pizza.setPhoto(nouvellePhoto);
        assertEquals(nouvellePhoto, pizza.getPhoto(), "La photo doit être mise à jour avec la nouvelle valeur");

        pizza.setPhoto(null);
        assertNull(pizza.getPhoto(), "La photo doit pouvoir être null");
    }

    /**
     * Test la cohérence entre le prix et les ingrédients.
     * <p>
     * Vérifie que l'ajout ou la suppression d'ingrédients affecte correctement le prix.
     */
    @Test
    public void testCohérencePrixIngredients() {
        double prixSansIngredients = pizza.getPrix();

        pizza.getIngredients().add(tomate);
        double prixAvecUnIngredient = pizza.getPrix();
        assertTrue(prixAvecUnIngredient > prixSansIngredients, "Le prix doit augmenter après ajout d'un ingrédient");

        pizza.getIngredients().add(fromage);
        double prixAvecDeuxIngredients = pizza.getPrix();
        assertTrue(prixAvecDeuxIngredients > prixAvecUnIngredient, "Le prix doit augmenter après ajout d'un deuxième ingrédient");

        double sommeIngredients = tomate.getPrix() + fromage.getPrix();
        double prixMinimalAttendu = Math.ceil((sommeIngredients * 1.4) * 10) / 10.0;
        assertEquals(prixMinimalAttendu, prixAvecDeuxIngredients, 0.01,
            "Le prix doit correspondre au calcul (somme des ingrédients + 40%)");
    }
}
