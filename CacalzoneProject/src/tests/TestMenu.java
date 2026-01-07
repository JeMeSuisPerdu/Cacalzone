package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.Commande;
import pizzas.CompteClient;
import pizzas.InformationPersonnelle;
import pizzas.Menu;
import pizzas.Utilisateur;

/**
 * Classe de test pour la classe Menu.
 * 
 * <p>Le Menu étant le point central de stockage des données, ces tests vérifient :
 * - L'initialisation correcte (présence du compte admin par défaut).
 * - Les mécanismes d'authentification et de recherche d'utilisateurs.
 * - L'ajout de nouvelles données (utilisateurs, commandes).
 * </p>
 */
class TestMenu {

  /** L'instance de Menu testée. */
  private Menu menu;

  /**
   * Initialisation avant chaque test.
   * Crée un nouveau menu vide (contenant juste le chef par défaut).
   */
  @BeforeEach
  void setUp() {
    menu = new Menu();
  }

  /**
   * Vérifie l'état initial du menu après construction.
   * 
   * <p>Doit contenir :
   * - 1 utilisateur (le chef "Mario").
   * - Des listes de pizzas, ingrédients et commandes vides mais non nulles.
   * </p>
   */
  @Test
  void testConstructeur() {
    // Vérification des listes vides
    assertNotNull(menu.getPizzas(), "La liste des pizzas ne doit pas être null");
    assertTrue(menu.getPizzas().isEmpty(), "La liste des pizzas doit être vide");
    
    assertNotNull(menu.getIngredients(), "La liste des ingrédients ne doit pas être null");
    assertTrue(menu.getIngredients().isEmpty(), "La liste des ingrédients doit être vide");
    
    assertNotNull(menu.getCommandesGlobales(), "La liste des commandes ne doit pas être null");
    assertTrue(menu.getCommandesGlobales().isEmpty(), "La liste des commandes doit être vide");

    // Vérification de la présence du chef par défaut
    assertEquals(1, menu.getUtilisateurs().size(), "Il doit y avoir 1 utilisateur par défaut");
    Utilisateur chef = menu.getUtilisateurs().get(0);
    assertEquals("chef@pizza.fr", chef.getEmail());
    assertTrue(chef.estPizzaiolo());
  }

  /**
   * Vérifie qu'un utilisateur peut s'authentifier avec les bons identifiants.
   */
  @Test
  void testAuthentifierSucces() {
    Utilisateur u = menu.authentifier("chef@pizza.fr", "admin");
    assertNotNull(u, "L'authentification doit réussir pour le chef");
    assertEquals("Mario", u.getInfos().getNom());
  }

  /**
   * Vérifie que l'authentification échoue avec un mauvais mot de passe.
   */
  @Test
  void testAuthentifierEchecMdp() {
    Utilisateur u = menu.authentifier("chef@pizza.fr", "mauvaisPassword");
    assertNull(u, "L'authentification doit échouer si le mot de passe est faux");
  }

  /**
   * Vérifie que l'authentification échoue avec un email inconnu.
   */
  @Test
  void testAuthentifierEchecEmail() {
    Utilisateur u = menu.authentifier("inconnu@pizza.fr", "admin");
    assertNull(u, "L'authentification doit échouer si l'email n'existe pas");
  }

  /**
   * Vérifie la recherche d'un utilisateur par son email.
   */
  @Test
  void testTrouverUtilisateurExiste() {
    Utilisateur u = menu.trouverUtilisateur("chef@pizza.fr");
    assertNotNull(u, "L'utilisateur doit être trouvé");
    assertEquals("chef@pizza.fr", u.getEmail());
  }

  /**
   * Vérifie que la recherche renvoie null pour un email inexistant.
   */
  @Test
  void testTrouverUtilisateurInconnu() {
    Utilisateur u = menu.trouverUtilisateur("fantome@pizza.fr");
    assertNull(u, "La méthode doit renvoyer null si l'utilisateur n'existe pas");
  }

  /**
   * Vérifie l'ajout d'un nouvel utilisateur dans le système.
   */
  @Test
  void testAjouterUtilisateur() {
    // Création d'un nouveau client
    InformationPersonnelle infos = new InformationPersonnelle("Test", "User");
    CompteClient nouveau = new CompteClient("test@client.fr", "1234", infos);

    // Action
    menu.ajouterUtilisateur(nouveau);

    // Vérifications
    assertEquals(2, menu.getUtilisateurs().size(), "La liste doit contenir 2 utilisateurs");
    assertNotNull(menu.trouverUtilisateur("test@client.fr"), "Le nouvel utilisateur doit être trouvable");
    
    // Vérifie qu'on peut s'authentifier avec ce nouveau compte
    assertNotNull(menu.authentifier("test@client.fr", "1234"));
  }

  /**
   * Vérifie l'ajout d'une commande dans l'historique global.
   */
  @Test
  void testAjouterCommande() {
    // On a besoin d'un client pour créer la commande (même fictif pour ce test)
    InformationPersonnelle infos = new InformationPersonnelle("A", "B");
    CompteClient client = new CompteClient("c@c.fr", "mdp", infos);
    
    Commande c = new Commande(client);

    // Action
    menu.ajouterCommande(c);

    // Vérification
    assertEquals(1, menu.getCommandesGlobales().size(), "La liste des commandes doit contenir 1 élément");
    assertTrue(menu.getCommandesGlobales().contains(c));
  }
}