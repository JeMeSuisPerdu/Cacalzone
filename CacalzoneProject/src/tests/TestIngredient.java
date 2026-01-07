package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.Ingredient;
import pizzas.TypePizza;

/**
 * Classe de test pour la classe Ingredient.
 * Vérifie le bon fonctionnement des données de base (nom, prix)
 * et la gestion des restrictions (types de pizzas interdits).
 */
class TestIngredient {

    private Ingredient ingredient;

    @BeforeEach
    void setUp() {
        // Initialisation d'un ingrédient de base avant chaque test
        ingredient = new Ingredient("Champignon", 1.50);
    }

    @Test
    void testConstructeur() {
        assertAll("Vérification de l'état initial",
            () -> assertEquals("Champignon", ingredient.getNom(), "Le nom doit correspondre à celui du constructeur"),
            () -> assertEquals(1.50, ingredient.getPrix(), "Le prix doit correspondre"),
            () -> assertNotNull(ingredient.getTypesPizzaInterdits(), "La liste des interdits ne doit pas être null"),
            () -> assertTrue(ingredient.getTypesPizzaInterdits().isEmpty(), "La liste des interdits doit être vide au départ")
        );
    }

    @Test
    void testSetNom() {
        ingredient.setNom("Cèpe");
        assertEquals("Cèpe", ingredient.getNom(), "Le setter doit modifier le nom correctement");
    }

    @Test
    void testSetPrix() {
        ingredient.setPrix(2.0);
        assertEquals(2.0, ingredient.getPrix(), "Le setter doit modifier le prix correctement");
    }

    @Test
    void testAddTypePizzaInterdit() {
        ingredient.addTypePizzaInterdit(TypePizza.Viande);
        
        assertFalse(ingredient.getTypesPizzaInterdits().isEmpty(), "La liste ne doit plus être vide");
        assertTrue(ingredient.getTypesPizzaInterdits().contains(TypePizza.Viande), "La liste doit contenir le type ajouté");
    }

    @Test
    void testAddTypePizzaInterditDoublon() {
        // Ajout deux fois du même type
        ingredient.addTypePizzaInterdit(TypePizza.Viande);
        ingredient.addTypePizzaInterdit(TypePizza.Viande);
        
        assertEquals(1, ingredient.getTypesPizzaInterdits().size(), "Les doublons ne doivent pas être ajoutés (Set)");
    }

    @Test
    void testRemoveTypePizzaInterdit() {
        // On ajoute d'abord pour pouvoir retirer
        ingredient.addTypePizzaInterdit(TypePizza.Vegetarienne);
        assertTrue(ingredient.getTypesPizzaInterdits().contains(TypePizza.Vegetarienne));
        
        // Action : Suppression
        ingredient.removeTypePizzaInterdit(TypePizza.Vegetarienne);
        
        // Vérification
        assertFalse(ingredient.getTypesPizzaInterdits().contains(TypePizza.Vegetarienne), "L'interdiction doit avoir été retirée");
        assertTrue(ingredient.getTypesPizzaInterdits().isEmpty(), "La liste doit être vide après suppression");
    }

    @Test
    void testSetTypesPizzaInterdits() {
        Set<TypePizza> nouveauxInterdits = new HashSet<>();
        nouveauxInterdits.add(TypePizza.Viande);
        nouveauxInterdits.add(TypePizza.Regionale); // Supposons que ce type existe ou un autre du enum
        
        ingredient.setTypesPizzaInterdits(nouveauxInterdits);
        
        assertEquals(nouveauxInterdits, ingredient.getTypesPizzaInterdits(), "Le setter doit remplacer la liste complète");
        assertEquals(2, ingredient.getTypesPizzaInterdits().size());
    }
}