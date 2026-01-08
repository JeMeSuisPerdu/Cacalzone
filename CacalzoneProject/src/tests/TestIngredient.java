package tests;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.Ingredient;
import pizzas.TypePizza;

/**
 * Classe de test pour la classe Ingredient.
 *
 * <p>Vérifie le bon fonctionnement des données de base (nom, prix)
 * et la gestion des restrictions (types de pizzas interdits).
 * </p>
 */
class TestIngredient {

  private Ingredient ingredient;

  /**
   * Initialisation d'un ingrédient de base avant chaque test.
   *
   * <p>Crée un ingrédient "Champignon" avec un prix de 1.50€ pour servir
   * de base aux tests unitaires.
   * </p>
   */
  @BeforeEach
  void setUp() {
    ingredient = new Ingredient("Champignon", 1.50);
  }

  /**
   * Vérifie l'état initial de l'objet après construction.
   *
   * <p>S'assure que le nom et le prix correspondent aux paramètres du
   * constructeur et que la liste des interdictions est initialisée
   * (non null) mais vide.
   * </p>
   */
  @Test
  void testConstructeur() {
    assertAll("Vérification de l'état initial",
        () -> assertEquals("Champignon", ingredient.getNom(),
            "Le nom doit correspondre à celui du constructeur"),
        () -> assertEquals(1.50, ingredient.getPrix(),
            "Le prix doit correspondre"),
        () -> assertNotNull(ingredient.getTypesPizzaInterdits(),
            "La liste des interdits ne doit pas être null"),
        () -> assertTrue(ingredient.getTypesPizzaInterdits().isEmpty(),
            "La liste des interdits doit être vide au départ")
    );
  }

  /**
   * Vérifie la modification du nom de l'ingrédient.
   *
   * <p>Utilise le setter pour changer le nom en "Cèpe" et vérifie
   * que le getter renvoie bien la nouvelle valeur.
   * </p>
   */
  @Test
  void testSetNom() {
    ingredient.setNom("Cèpe");
    assertEquals("Cèpe", ingredient.getNom(),
        "Le setter doit modifier le nom correctement");
  }

  /**
   * Vérifie la modification du prix de l'ingrédient.
   *
   * <p>Utilise le setter pour changer le prix à 2.0€ et vérifie
   * que le getter renvoie bien la nouvelle valeur.
   * </p>
   */
  @Test
  void testSetPrix() {
    ingredient.setPrix(2.0);
    assertEquals(2.0, ingredient.getPrix(),
        "Le setter doit modifier le prix correctement");
  }

  /**
   * Vérifie l'ajout d'une restriction de type de pizza.
   *
   * <p>Ajoute l'interdiction pour le type VIANDE. Vérifie que la liste
   * n'est plus vide et contient bien ce type.
   * </p>
   */
  @Test
  void testAddTypePizzaInterdit() {
    ingredient.addTypePizzaInterdit(TypePizza.Viande);

    assertFalse(ingredient.getTypesPizzaInterdits().isEmpty(),
        "La liste ne doit plus être vide");
    assertTrue(ingredient.getTypesPizzaInterdits().contains(TypePizza.Viande),
        "La liste doit contenir le type ajouté");
  }

  /**
   * Vérifie que les doublons ne sont pas ajoutés à la liste des restrictions.
   *
   * <p>Tente d'ajouter deux fois le même type d'interdiction (VIANDE).
   * Vérifie que la taille de la collection reste à 1, car c'est un Set.
   * </p>
   */
  @Test
  void testAddTypePizzaInterditDoublon() {
    ingredient.addTypePizzaInterdit(TypePizza.Viande);
    ingredient.addTypePizzaInterdit(TypePizza.Viande);

    assertEquals(1, ingredient.getTypesPizzaInterdits().size(),
        "Les doublons ne doivent pas être ajoutés (Set)");
  }

  /**
   * Vérifie la suppression d'une restriction existante.
   *
   * <p>Ajoute d'abord une interdiction (VEGETARIENNE), vérifie sa présence,
   * puis la retire. Vérifie que la liste est bien revenue à vide.
   * </p>
   */
  @Test
  void testRemoveTypePizzaInterdit() {
    ingredient.addTypePizzaInterdit(TypePizza.Vegetarienne);
    assertTrue(ingredient.getTypesPizzaInterdits()
        .contains(TypePizza.Vegetarienne));

    ingredient.removeTypePizzaInterdit(TypePizza.Vegetarienne);

    assertFalse(ingredient.getTypesPizzaInterdits()
        .contains(TypePizza.Vegetarienne),
        "L'interdiction doit avoir été retirée");
    assertTrue(ingredient.getTypesPizzaInterdits().isEmpty(),
        "La liste doit être vide après suppression");
  }

  /**
   * Vérifie le remplacement complet de la liste des restrictions.
   *
   * <p>Crée un nouvel ensemble contenant VIANDE et REGIONALE, puis l'affecte
   * à l'ingrédient via le setter. Vérifie que la liste de l'ingrédient
   * correspond exactement au nouvel ensemble.
   * </p>
   */
  @Test
  void testSetTypesPizzaInterdits() {
    Set<TypePizza> nouveauxInterdits = new HashSet<>();
    nouveauxInterdits.add(TypePizza.Viande);
    nouveauxInterdits.add(TypePizza.Regionale);

    ingredient.setTypesPizzaInterdits(nouveauxInterdits);

    assertEquals(nouveauxInterdits, ingredient.getTypesPizzaInterdits(),
        "Le setter doit remplacer la liste complète");
    assertEquals(2, ingredient.getTypesPizzaInterdits().size());
  }
}