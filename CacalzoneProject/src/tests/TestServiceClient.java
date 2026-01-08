package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.Commande;
import pizzas.Commande.Etat;
import pizzas.InformationPersonnelle;
import pizzas.Ingredient;
import pizzas.Menu;
import pizzas.NonConnecteException;
import pizzas.Pizza;
import pizzas.ServiceClient;
import pizzas.TypePizza;

/**
 * Classe de test pour le ServiceClient.
 *
 * <p>Couvre les scénarios d'inscription, connexion, cycle de vie d'une commande
 * et filtrage des pizzas.
 * </p>
 */
class TestServiceClient {

  private ServiceClient service;
  private Menu menu;
  private Pizza p1;
  private Pizza p2;

  /**
   * Initialisation du contexte avant chaque test.
   *
   * <p>Instancie le menu et le service client. Ajoute également deux pizzas
   * (une avec viande à 12€, une végétarienne à 14€) au menu pour permettre
   * le test des fonctionnalités de filtrage.
   * </p>
   */
  @BeforeEach
  void setUp() {
    menu = new Menu();
    service = new ServiceClient(menu);

    p1 = new Pizza("Reine", TypePizza.Viande);
    p1.ajouterIngredient(new Ingredient("Jambon", 2.0));
    p1.setPrix(12.0);
    menu.getPizzas().add(p1);

    p2 = new Pizza("4 Fromages", TypePizza.Vegetarienne);
    p2.ajouterIngredient(new Ingredient("Mozzarella", 2.0));
    p2.setPrix(14.0);
    menu.getPizzas().add(p2);
  }

  /**
   * Vérifie le succès d'une inscription valide.
   *
   * <p>Tente d'inscrire un utilisateur avec un email unique.
   * Vérifie que la méthode renvoie 0 (succès) et que l'utilisateur
   * est bien ajouté au menu.
   * </p>
   */
  @Test
  void testInscriptionSucces() {
    InformationPersonnelle infos = new InformationPersonnelle("Test", "User");
    int res = service.inscription("new@client.fr", "1234", infos);

    assertEquals(0, res, "L'inscription doit réussir");
    assertNotNull(menu.trouverUtilisateur("new@client.fr"),
        "L'utilisateur doit être dans le menu");
  }

  /**
   * Vérifie l'échec de l'inscription si l'email existe déjà.
   *
   * <p>Inscrit un premier utilisateur, puis tente d'en inscrire un second
   * avec le même email. Vérifie que la méthode renvoie -1.
   * </p>
   */
  @Test
  void testInscriptionDoublon() {
    InformationPersonnelle infos = new InformationPersonnelle("Test", "User");
    service.inscription("doublon@client.fr", "1234", infos);

    int res = service.inscription("doublon@client.fr", "0000", infos);
    assertEquals(-1, res,
        "L'inscription doit échouer pour un email existant");
  }

  /**
   * Vérifie les mécanismes de connexion.
   *
   * <p>Teste la connexion avec le bon mot de passe (doit renvoyer true)
   * et avec un mauvais mot de passe (doit renvoyer false).
   * </p>
   */
  @Test
  void testConnexion() {
    InformationPersonnelle infos = new InformationPersonnelle("Test", "User");
    service.inscription("login@client.fr", "secret", infos);

    boolean success = service.connexion("login@client.fr", "secret");
    assertTrue(success, "La connexion doit réussir avec le bon mot de passe");

    boolean fail = service.connexion("login@client.fr", "wrong");
    assertFalse(fail,
        "La connexion doit échouer avec le mauvais mot de passe");
  }

  /**
   * Vérifie l'impossibilité de débuter une commande sans être connecté.
   *
   * <p>Appelle la méthode debuterCommande sans connexion préalable.
   * Une NonConnecteException doit être levée.
   * </p>
   */
  @Test
  void testDebuterCommandeNonConnecte() {
    assertThrows(NonConnecteException.class, () -> {
      service.debuterCommande();
    }, "Doit lever une exception si non connecté");
  }

  /**
   * Vérifie la création d'une commande pour un utilisateur connecté.
   *
   * <p>Connecte un utilisateur puis débute une commande.
   * Vérifie que l'objet commande retourné est non null et correspond
   * à la commande active du service.
   * </p>
   *
   * @throws NonConnecteException ne doit pas être levée ici.
   */
  @Test
  void testDebuterCommandeConnecte() throws NonConnecteException {
    InformationPersonnelle infos = new InformationPersonnelle("A", "B");
    service.inscription("c@c.fr", "mdp", infos);
    service.connexion("c@c.fr", "mdp");

    Commande c = service.debuterCommande();
    assertNotNull(c, "La commande créée ne doit pas être null");
    assertEquals(c, service.getCommandeActive());
  }

  /**
   * Vérifie l'ajout d'une pizza à la commande en cours.
   *
   * <p>Connecte un client, crée une commande et ajoute une pizza.
   * Vérifie que la pizza est bien présente dans la commande avec la
   * quantité spécifiée.
   * </p>
   *
   * @throws Exception ne doit pas être levée ici.
   */
  @Test
  void testAjouterPizzaCommande() throws Exception {
    InformationPersonnelle infos = new InformationPersonnelle("A", "B");
    service.inscription("user@test.fr", "mdp", infos);
    service.connexion("user@test.fr", "mdp");

    Commande c = service.debuterCommande();

    service.ajouterPizza(p1, 2, c);

    assertEquals(2, c.getPizzas().get(p1),
        "La pizza doit être ajoutée avec la quantité 2");
  }

  /**
   * Vérifie la validation de la commande.
   *
   * <p>Crée et remplit une commande, puis la valide.
   * Vérifie que son état passe à VALIDEE et qu'elle est ajoutée à
   * l'historique global des commandes.
   * </p>
   *
   * @throws Exception ne doit pas être levée ici.
   */
  @Test
  void testValiderCommande() throws Exception {
    InformationPersonnelle infos = new InformationPersonnelle("A", "B");
    service.inscription("valider@test.fr", "mdp", infos);
    service.connexion("valider@test.fr", "mdp");

    Commande c = service.debuterCommande();
    service.ajouterPizza(p1, 1, c);

    service.validerCommande(c);

    assertEquals(Etat.VALIDEE, c.getEtat(), "L'état doit passer à VALIDEE");
    assertTrue(menu.getCommandesGlobales().contains(c),
        "La commande doit être dans l'historique");
  }

  /**
   * Vérifie le filtrage des pizzas par prix maximum.
   *
   * <p>Applique un filtre de prix (13€). Vérifie que la pizza à 12€ est
   * conservée et que celle à 14€ est exclue.
   * </p>
   */
  @Test
  void testFiltrePrix() {
    service.ajouterFiltre(13.0);
    Set<Pizza> result = service.selectionPizzaFiltres();

    assertTrue(result.contains(p1), "Reine (12€) doit être sélectionnée");
    assertFalse(result.contains(p2),
        "4 Fromages (14€) ne doit pas être sélectionnée");
  }

  /**
   * Vérifie le filtrage des pizzas par ingrédient (exclusion).
   *
   * <p>Applique un filtre pour exclure le "Jambon". Vérifie que la pizza
   * contenant du jambon est retirée des résultats.
   * </p>
   */
  @Test
  void testFiltreIngredient() {
    service.ajouterFiltre("Jambon");
    Set<Pizza> result = service.selectionPizzaFiltres();

    assertFalse(result.contains(p1),
        "Reine (avec Jambon) doit être exclue");
    assertTrue(result.contains(p2),
        "4 Fromages (sans Jambon) doit être présente");
  }
}