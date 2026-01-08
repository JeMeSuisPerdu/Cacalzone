package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.Commande;
import pizzas.Commande.Etat;
import pizzas.CompteClient;
import pizzas.InformationPersonnelle;
import pizzas.Ingredient;
import pizzas.Menu;
import pizzas.Pizza;
import pizzas.ServicePizzaiolo;
import pizzas.TypePizza;

/**
 * Classe de test pour le ServicePizzaiolo.
 *
 * <p>Vérifie les fonctionnalités de gestion du menu (pizzas, ingrédients),
 * les règles métier (interdictions) et le traitement des commandes.
 * </p>
 */
class TestServicePizzaiolo {

  private ServicePizzaiolo service;
  private Menu menu;

  /**
   * Initialisation du contexte avant chaque test.
   *
   * <p>Crée un nouveau menu vide et le service associé pour garantir
   * l'indépendance des tests.
   * </p>
   */
  @BeforeEach
  void setUp() {
    menu = new Menu();
    service = new ServicePizzaiolo(menu);
  }

  /**
   * Vérifie la création d'une nouvelle pizza valide.
   *
   * <p>Crée une pizza et vérifie que l'objet retourné n'est pas null
   * et qu'il est bien présent dans la liste du menu.
   * </p>
   */
  @Test
  void testCreerPizzaSucces() {
    Pizza p = service.creerPizza("Royale", TypePizza.Viande);
    assertNotNull(p, "La pizza doit être créée");
    assertTrue(menu.getPizzas().contains(p),
        "La pizza doit être dans le menu");
  }

  /**
   * Vérifie l'interdiction de créer des doublons de pizzas.
   *
   * <p>Crée une pizza "Royale", puis tente d'en créer une autre avec le même nom
   * (insensible à la casse). La seconde création doit échouer (retourner null).
   * </p>
   */
  @Test
  void testCreerPizzaDoublon() {
    service.creerPizza("Royale", TypePizza.Viande);
    Pizza doublon = service.creerPizza("royale", TypePizza.Vegetarienne);

    assertNull(doublon, "La création d'un doublon doit renvoyer null");
    assertEquals(1, menu.getPizzas().size(),
        "Le menu ne doit contenir qu'une seule pizza");
  }

  /**
   * Vérifie la création d'un ingrédient valide.
   *
   * <p>Crée un ingrédient avec un prix positif. Vérifie que la méthode renvoie 0
   * (succès) et que l'ingrédient est ajouté au menu.
   * </p>
   */
  @Test
  void testCreerIngredientSucces() {
    int res = service.creerIngredient("Olive", 0.5);
    assertEquals(0, res, "La création doit réussir");
    assertEquals(1, menu.getIngredients().size());
  }

  /**
   * Vérifie le rejet des ingrédients invalides.
   *
   * <p>Tente de créer un ingrédient avec un nom vide (code -1) puis avec un
   * prix négatif (code -3). Vérifie que la liste des ingrédients reste vide.
   * </p>
   */
  @Test
  void testCreerIngredientInvalide() {
    assertEquals(-1, service.creerIngredient("", 0.5), "Nom vide interdit");
    assertEquals(-3, service.creerIngredient("Or", -10.0),
        "Prix négatif interdit");
    assertTrue(menu.getIngredients().isEmpty());
  }

  /**
   * Vérifie l'ajout d'une interdiction sur un ingrédient.
   *
   * <p>Crée un ingrédient et lui interdit le type VEGETARIENNE.
   * Vérifie que l'opération réussit et que la restriction est bien enregistrée
   * dans l'objet ingrédient stocké dans le menu.
   * </p>
   */
  @Test
  void testInterdireIngredient() {
    service.creerIngredient("Jambon", 2.0);
    boolean res = service.interdireIngredient("Jambon",
        TypePizza.Vegetarienne);

    assertTrue(res, "L'interdiction doit être prise en compte");

    Ingredient jambon = menu.getIngredients().iterator().next();
    assertTrue(jambon.getTypesPizzaInterdits()
        .contains(TypePizza.Vegetarienne));
  }

  /**
   * Vérifie l'ajout d'un ingrédient valide à une pizza.
   *
   * <p>Crée une pizza et un ingrédient sans restriction. Ajoute l'ingrédient
   * à la pizza et vérifie que l'opération réussit (code 0) et que la pizza
   * contient bien l'ingrédient.
   * </p>
   */
  @Test
  void testAjouterIngredientPizza() {
    Pizza p = service.creerPizza("Test", TypePizza.Viande);
    service.creerIngredient("Tomate", 1.0);

    int res = service.ajouterIngredientPizza(p, "Tomate");
    assertEquals(0, res, "L'ajout doit réussir");
    assertTrue(p.getIngredients().stream()
        .anyMatch(i -> i.getNom().equals("Tomate")));
  }

  /**
   * Vérifie l'interdiction d'ajouter un ingrédient non compatible.
   *
   * <p>Crée une pizza VEGETARIENNE et un ingrédient interdit pour ce type.
   * Tente d'ajouter l'ingrédient. Vérifie que l'opération échoue (code -3)
   * et que la pizza reste vide.
   * </p>
   */
  @Test
  void testAjouterIngredientInterdit() {
    Pizza p = service.creerPizza("Végé", TypePizza.Vegetarienne);
    service.creerIngredient("Jambon", 2.0);
    service.interdireIngredient("Jambon", TypePizza.Vegetarienne);

    int res = service.ajouterIngredientPizza(p, "Jambon");
    assertEquals(-3, res,
        "L'ajout d'un ingrédient interdit doit échouer (-3)");
    assertTrue(p.getIngredients().isEmpty());
  }

  /**
   * Vérifie la récupération et le traitement des commandes en attente.
   *
   * <p>Prépare une commande validée dans le menu. Appelle la méthode
   * commandeNonTraitees. Vérifie que la commande est retournée et que
   * son état passe à TRAITEE.
   * </p>
   *
   * @throws Exception ne doit pas être levée ici.
   */
  @Test
  void testCommandeNonTraitees() throws Exception {
    InformationPersonnelle info = new InformationPersonnelle("A", "B");
    CompteClient c = new CompteClient("cli@ent.fr", "mdp", info);
    Commande cmd = new Commande(c);

    Pizza p = new Pizza("P", TypePizza.Viande);
    p.setPrix(10.0);
    cmd.ajouterPizza(p, 1);

    cmd.valider();
    menu.ajouterCommande(cmd);

    List<Commande> atraiter = service.commandeNonTraitees();

    assertEquals(1, atraiter.size());
    assertTrue(atraiter.contains(cmd));
    assertEquals(Etat.TRAITEE, cmd.getEtat(),
        "La commande doit passer à l'état TRAITEE");
  }

  /**
   * Vérifie le calcul du bénéfice total sur l'ensemble des commandes.
   *
   * <p>Simule une commande traitée de 2 pizzas. Le prix de vente est fixé à 10.0,
   * le coût (ingrédients) est nul pour simplifier le test (bénéfice = 10.0).
   * Vérifie que le bénéfice total est bien de 20.0 (2 pizzas * 10.0).
   * </p>
   *
   * @throws Exception ne doit pas être levée ici.
   */
  @Test
  void testBeneficeToutesCommandes() throws Exception {
    Pizza p = new Pizza("Benef", TypePizza.Viande);
    p.setPrix(10.0);

    InformationPersonnelle info = new InformationPersonnelle("A", "B");
    CompteClient c = new CompteClient("cli@ent.fr", "mdp", info);
    Commande cmd = new Commande(c);
    cmd.ajouterPizza(p, 2);

    cmd.valider();
    cmd.traiter();
    menu.ajouterCommande(cmd);

    menu.getPizzas().add(p);

    assertEquals(20.0, service.beneficeToutesCommandes(), 0.01);
  }
}