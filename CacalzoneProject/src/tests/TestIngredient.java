package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.Ingredient;

/**
 * Classe de tests unitaires pour la classe {@link Ingredient}.
 * <p>
 * Ces tests vérifient le bon fonctionnement des ingrédients du catalogue,
 * notamment :
 * <ul>
 *   <li>la création correcte d'un ingrédient</li>
 *   <li>la mise à jour sécurisée du nom et du prix</li>
 *   <li>la protection contre les valeurs invalides (prix négatif, nom vide ou null)</li>
 * </ul>
 * <p>
 * L'objectif est de garantir que les objets {@link Ingredient} restent
 * cohérents et sécurisés face à des modifications valides ou invalides.
 * 
 * @author Safiatou
 */
public class TestIngredient {

    /** Ingrédient utilisé pour les tests */
    private Ingredient ingredient;

    /** Nom initial de l'ingrédient */
    private final String NOM_INITIAL = "Tomate";

    /** Prix initial de l'ingrédient */
    private final double PRIX_INITIAL = 0.90;

    /**
     * Initialisation avant chaque test.
     * <p>
     * Crée un ingrédient de base pour garantir l'indépendance
     * et la reproductibilité des tests.
     */
    @BeforeEach
    public void setUp() {
        ingredient = new Ingredient(NOM_INITIAL, PRIX_INITIAL);
    }

    /**
     * Vérifie que le constructeur initialise correctement le nom et le prix.
     */
    @Test
    public void testConstructeurEtGetters() {
        assertEquals(NOM_INITIAL, ingredient.getNom(),
                "Le nom doit correspondre à celui du constructeur");
        assertEquals(PRIX_INITIAL, ingredient.getPrix(),
                "Le prix doit correspondre à celui du constructeur");
    }

    /**
     * Vérifie que la mise à jour du prix avec une valeur valide fonctionne correctement.
     */
    @Test
    public void testSetPrixValide() {
        double nouveauPrix = 1.50;
        ingredient.setPrix(nouveauPrix);
        assertEquals(nouveauPrix, ingredient.getPrix(),
                "Le prix devrait être mis à jour");
    }

    /**
     * Vérifie que le nom de l'ingrédient peut être modifié correctement.
     */
    @Test
    public void testSetNom() {
        String nouveauNom = "Sauce Tomate Bio";
        ingredient.setNom(nouveauNom);
        assertEquals(nouveauNom, ingredient.getNom(),
                "Le nom devrait pouvoir être modifié");
    }

    /**
     * Vérifie que le système protège contre un prix négatif.
     * <p>
     * Si un prix invalide est fourni, l'ancien prix doit être conservé.
     */
    @Test
    public void testSetPrixNegatif() {
        // Tentative d'injection d'un prix invalide
        ingredient.setPrix(-5.0);

        assertNotEquals(-5.0, ingredient.getPrix(),
                "La sécurité doit empêcher un prix négatif");
        assertEquals(PRIX_INITIAL, ingredient.getPrix(),
                "En cas de valeur négative, l'ancien prix doit être conservé");
    }

    /**
     * Vérifie que le système protège contre des noms invalides.
     * <p>
     * Les noms vides ou null ne doivent pas être acceptés.
     */
    @Test
    public void testSetNomInvalide() {
        // Tentative de nom vide
        ingredient.setNom("");
        assertNotEquals("", ingredient.getNom(),
                "Le système ne doit pas accepter de nom vide");

        // Tentative de nom null
        ingredient.setNom(null);
        assertNotNull(ingredient.getNom(),
                "Le nom ne doit jamais passer à null");
    }
}
