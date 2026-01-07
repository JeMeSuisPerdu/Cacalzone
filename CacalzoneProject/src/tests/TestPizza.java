package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.Ingredient;
import pizzas.Pizza;
import pizzas.TypePizza;

/**
 * Classe de test pour la classe Pizza.
 * Vérifie le comportement des constructeurs, getters, setters
 * et des méthodes métiers (calcul prix, ajout ingrédients).
 */
class TestPizza {

    private Pizza pizza;
    private Ingredient ing1;
    private Ingredient ing2;

    @BeforeEach
    void setUp() {
        // Initialisation avant chaque test
        pizza = new Pizza("Reine", TypePizza.Viande);
        ing1 = new Ingredient("Tomate", 1.0);
        ing2 = new Ingredient("Mozzarella", 2.0);
    }

    @Test
    void testConstructeurEtGetters() {
        assertEquals("Reine", pizza.getNom(), "Le nom doit être 'Reine'");
        assertEquals(TypePizza.Viande, pizza.getTypePizza(), "Le type doit être VIANDE");
        assertTrue(pizza.getIngredients().isEmpty(), "La liste d'ingrédients doit être vide au départ");
        assertTrue(pizza.getEvaluations().isEmpty(), "La liste d'évaluations doit être vide au départ");
        assertEquals(0.0, pizza.getPrix(), "Le prix minimal sans ingrédient doit être 0.0");
        assertEquals("", pizza.getPhoto(), "La photo doit être vide par défaut");
    }

    @Test
    void testSetNomValide() {
        pizza.setNom("Royale");
        assertEquals("Royale", pizza.getNom());
    }

    @Test
    void testSetNomInvalide() {
        // Teste si l'exception est bien levée pour un nom null
        assertThrows(IllegalArgumentException.class, () -> {
            pizza.setNom(null);
        }, "Le nom ne doit pas être null");

        // Teste si l'exception est bien levée pour un nom vide
        assertThrows(IllegalArgumentException.class, () -> {
            pizza.setNom("   ");
        }, "Le nom ne doit pas être vide");
    }

    @Test
    void testSetTypePizza() {
        pizza.setTypePizza(TypePizza.Vegetarienne);
        assertEquals(TypePizza.Vegetarienne, pizza.getTypePizza());
    }

    @Test
    void testAjouterIngredientValide() {
        boolean ajout = pizza.ajouterIngredient(ing1);
        assertTrue(ajout, "L'ajout d'un ingrédient valide doit renvoyer true");
        assertEquals(1, pizza.getIngredients().size());
        assertTrue(pizza.getIngredients().contains(ing1));
    }

    @Test
    void testAjouterIngredientDoublon() {
        pizza.ajouterIngredient(ing1);
        boolean ajoutDoublon = pizza.ajouterIngredient(ing1);
        
        assertFalse(ajoutDoublon, "L'ajout d'un doublon doit renvoyer false");
        assertEquals(1, pizza.getIngredients().size(), "La taille de la liste ne doit pas changer");
    }

    @Test
    void testAjouterIngredientInterdit() {
        // On configure un ingrédient pour qu'il soit interdit aux pizzas VIANDE
        Ingredient jambon = new Ingredient("Jambon", 3.0);
        jambon.addTypePizzaInterdit(TypePizza.Vegetarienne);
        
        // On change le type de la pizza pour tester l'interdiction
        pizza.setTypePizza(TypePizza.Vegetarienne);
        
        boolean ajout = pizza.ajouterIngredient(jambon);
        
        assertFalse(ajout, "L'ajout d'un ingrédient interdit doit renvoyer false");
        assertTrue(pizza.getIngredients().isEmpty());
    }

    @Test
    void testCalculPrixMinimal() {
        // Cas du sujet : Ingrédients = 7.30€
        // 40% de plus -> 10.22€
        // Arrondi au décime supérieur -> 10.30€
        
        // On "triche" un peu sur les prix des ingrédients pour tomber sur 7.30
        Ingredient i1 = new Ingredient("Base", 7.00);
        Ingredient i2 = new Ingredient("Epice", 0.30);
        
        pizza.ajouterIngredient(i1);
        pizza.ajouterIngredient(i2);
        
        assertEquals(10.30, pizza.getPrixMinimalPizza(), 0.001, "Le calcul du prix minimal est incorrect");
    }
    
    @Test
    void testGetPrix() {
        // Par défaut, getPrix renvoie le prix minimal
        Ingredient i1 = new Ingredient("Ing1", 10.0);
        pizza.ajouterIngredient(i1);
        // 10 + 40% = 14.0. Arrondi = 14.0.
        assertEquals(14.0, pizza.getPrix());
        
        // Si on fixe un prix manuel
        pizza.setPrix(16.0);
        assertEquals(16.0, pizza.getPrix());
    }

    @Test
    void testSetPrixValide() {
        Ingredient i1 = new Ingredient("Ing1", 10.0); // Min = 14.0
        pizza.ajouterIngredient(i1);
        
        boolean changement = pizza.setPrix(15.0); // 15 >= 14
        assertTrue(changement, "setPrix doit accepter un prix supérieur au minimum");
        assertEquals(15.0, pizza.getPrix());
    }

    @Test
    void testSetPrixInvalide() {
        Ingredient i1 = new Ingredient("Ing1", 10.0); // Min = 14.0
        pizza.ajouterIngredient(i1);
        
        boolean changement = pizza.setPrix(12.0); // 12 < 14
        assertFalse(changement, "setPrix doit refuser un prix inférieur au minimum");
        // Le prix doit rester le prix minimal (ou le précédent prix valide)
        assertEquals(14.0, pizza.getPrix());
    }

    @Test
    void testSetPhoto() {
        pizza.setPhoto("img/reine.png");
        assertEquals("img/reine.png", pizza.getPhoto());
    }
    
    @Test
    void testGetEvaluations() {
        assertNotNull(pizza.getEvaluations());
        // On ne teste pas l'ajout ici car il n'y a pas de méthode addEvaluation dans Pizza
        // (C'est généralement géré par le ServiceClient ou via getEvaluations().add())
    }
}