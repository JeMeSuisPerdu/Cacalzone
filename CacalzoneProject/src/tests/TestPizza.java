package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.Ingredient;
import pizzas.Pizza;
import pizzas.TypePizza;

/**
 * Classe de test pour la classe Pizza.
 *
 * <p>Vérifie le comportement des constructeurs, getters, setters
 * et des méthodes métiers (calcul prix, ajout ingrédients).
 * </p>
 */
class TestPizza {

  private Pizza pizza;
  private Ingredient ing1;
  
  /**
   * Initialisation des objets avant chaque test.
   *
   * <p>Crée une pizza "Reine" de type VIANDE et deux ingrédients de base
   * (Tomate et Mozzarella) pour les tests.
   * </p>
   */
  @BeforeEach
  void setUp() {
    pizza = new Pizza("Reine", TypePizza.Viande);
    ing1 = new Ingredient("Tomate", 1.0);
    new Ingredient("Mozzarella", 2.0);
  }

  /**
   * Vérifie l'état initial de la pizza après construction.
   *
   * <p>S'assure que le nom et le type sont corrects, que les listes
   * (ingrédients, évaluations) sont vides, que le prix est à 0.0
   * et que la photo est vide par défaut.
   * </p>
   */
  @Test
  void testConstructeurEtGetters() {
    assertEquals("Reine", pizza.getNom(), "Le nom doit être 'Reine'");
    assertEquals(TypePizza.Viande, pizza.getTypePizza(),
        "Le type doit être VIANDE");
    assertTrue(pizza.getIngredients().isEmpty(),
        "La liste d'ingrédients doit être vide au départ");
    assertTrue(pizza.getEvaluations().isEmpty(),
        "La liste d'évaluations doit être vide au départ");
    assertEquals(0.0, pizza.getPrix(),
        "Le prix minimal sans ingrédient doit être 0.0");
    assertEquals("", pizza.getPhoto(), "La photo doit être vide par défaut");
  }

  /**
   * Vérifie la modification du nom avec une valeur valide.
   */
  @Test
  void testSetNomValide() {
    pizza.setNom("Royale");
    assertEquals("Royale", pizza.getNom());
  }

  /**
   * Vérifie que la modification du nom est refusée pour des valeurs invalides.
   *
   * <p>Tente de modifier le nom avec null puis avec une chaîne ne contenant
   * que des espaces. Une IllegalArgumentException doit être levée dans
   * les deux cas.
   * </p>
   */
  @Test
  void testSetNomInvalide() {
    assertThrows(IllegalArgumentException.class, () -> {
      pizza.setNom(null);
    }, "Le nom ne doit pas être null");

    assertThrows(IllegalArgumentException.class, () -> {
      pizza.setNom("   ");
    }, "Le nom ne doit pas être vide");
  }

  /**
   * Vérifie la modification du type de la pizza.
   */
  @Test
  void testSetTypePizza() {
    pizza.setTypePizza(TypePizza.Vegetarienne);
    assertEquals(TypePizza.Vegetarienne, pizza.getTypePizza());
  }

  /**
   * Vérifie l'ajout d'un ingrédient valide.
   *
   * <p>Ajoute un ingrédient compatible. Vérifie que la méthode renvoie true
   * et que l'ingrédient est présent dans la liste.
   * </p>
   */
  @Test
  void testAjouterIngredientValide() {
    boolean ajout = pizza.ajouterIngredient(ing1);
    assertTrue(ajout, "L'ajout d'un ingrédient valide doit renvoyer true");
    assertEquals(1, pizza.getIngredients().size());
    assertTrue(pizza.getIngredients().contains(ing1));
  }

  /**
   * Vérifie la gestion des doublons d'ingrédients.
   *
   * <p>Tente d'ajouter deux fois le même ingrédient. La deuxième tentative
   * doit renvoyer false et la taille de la liste ne doit pas augmenter.
   * </p>
   */
  @Test
  void testAjouterIngredientDoublon() {
    pizza.ajouterIngredient(ing1);
    boolean ajoutDoublon = pizza.ajouterIngredient(ing1);

    assertFalse(ajoutDoublon, "L'ajout d'un doublon doit renvoyer false");
    assertEquals(1, pizza.getIngredients().size(),
        "La taille de la liste ne doit pas changer");
  }

  /**
   * Vérifie le refus d'ajouter un ingrédient interdit pour le type de pizza.
   *
   * <p>Configure un ingrédient avec une restriction, change le type de la pizza
   * pour correspondre à cette restriction, et tente l'ajout.
   * L'opération doit échouer.
   * </p>
   */
  @Test
  void testAjouterIngredientInterdit() {
    Ingredient jambon = new Ingredient("Jambon", 3.0);
    jambon.addTypePizzaInterdit(TypePizza.Vegetarienne);

    pizza.setTypePizza(TypePizza.Vegetarienne);

    boolean ajout = pizza.ajouterIngredient(jambon);

    assertFalse(ajout,
        "L'ajout d'un ingrédient interdit doit renvoyer false");
    assertTrue(pizza.getIngredients().isEmpty());
  }

  /**
   * Vérifie le calcul du prix minimal selon les règles métier.
   *
   * <p>Ajoute des ingrédients pour un total de 7.30€.
   * Applique la formule : (Somme * 1.4) arrondi au décime supérieur.
   * 7.30 * 1.4 = 10.22 -> arrondi à 10.30.
   * </p>
   */
  @Test
  void testCalculPrixMinimal() {
    Ingredient i1 = new Ingredient("Base", 7.00);
    Ingredient i2 = new Ingredient("Epice", 0.30);

    pizza.ajouterIngredient(i1);
    pizza.ajouterIngredient(i2);

    assertEquals(10.30, pizza.getPrixMinimalPizza(), 0.001,
        "Le calcul du prix minimal est incorrect");
  }

  /**
   * Vérifie le fonctionnement de getPrix.
   *
   * <p>Vérifie que getPrix renvoie le prix minimal calculé par défaut,
   * puis renvoie le prix manuel si celui-ci a été défini via setPrix.
   * </p>
   */
  @Test
  void testGetPrix() {
    Ingredient i1 = new Ingredient("Ing1", 10.0);
    pizza.ajouterIngredient(i1);
    // 10 + 40% = 14.0. Arrondi = 14.0.
    assertEquals(14.0, pizza.getPrix());

    pizza.setPrix(16.0);
    assertEquals(16.0, pizza.getPrix());
  }

  /**
   * Vérifie la définition d'un prix manuel valide.
   *
   * <p>Tente de fixer un prix supérieur au prix minimal.
   * L'opération doit réussir.
   * </p>
   */
  @Test
  void testSetPrixValide() {
    Ingredient i1 = new Ingredient("Ing1", 10.0); // Min = 14.0
    pizza.ajouterIngredient(i1);

    boolean changement = pizza.setPrix(15.0); // 15 >= 14
    assertTrue(changement,
        "setPrix doit accepter un prix supérieur au minimum");
    assertEquals(15.0, pizza.getPrix());
  }

  /**
   * Vérifie le refus d'un prix manuel invalide.
   *
   * <p>Tente de fixer un prix inférieur au prix minimal calculé.
   * L'opération doit échouer et le prix doit rester inchangé (valeur minimale).
   * </p>
   */
  @Test
  void testSetPrixInvalide() {
    Ingredient i1 = new Ingredient("Ing1", 10.0); // Min = 14.0
    pizza.ajouterIngredient(i1);

    boolean changement = pizza.setPrix(12.0); // 12 < 14
    assertFalse(changement,
        "setPrix doit refuser un prix inférieur au minimum");
    assertEquals(14.0, pizza.getPrix());
  }

  /**
   * Vérifie la modification de la photo.
   */
  @Test
  void testSetPhoto() {
    pizza.setPhoto("img/reine.png");
    assertEquals("img/reine.png", pizza.getPhoto());
  }

  /**
   * Vérifie que la liste des évaluations est bien instanciée.
   */
  @Test
  void testGetEvaluations() {
    assertNotNull(pizza.getEvaluations());
  }
}