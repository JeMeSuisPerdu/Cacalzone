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
 * - L'ajout de nouvelles données (utilisateurs, commandes).</p>
 */
class TestMenu {

  /** L'instance de Menu testée. */
  private Menu menu;

  /**
   * Initialisation avant chaque test.
   *
   * <p>Crée un nouveau menu vide (contenant juste le chef par défaut)
   * pour garantir l'indépendance des tests.</p>
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
   * - Des listes de pizzas, ingrédients et commandes vides mais non nulles.</p>
   */
  @Test
  void testConstructeur() {
    assertNotNull(menu.getPizzas(),
        "La liste des pizzas ne doit pas être null");
    assertTrue(menu.getPizzas().isEmpty(),
        "La liste des pizzas doit être vide");

    assertNotNull(menu.getIngredients(),
        "La liste des ingrédients ne doit pas être null");
    assertTrue(menu.getIngredients().isEmpty(),
        "La liste des ingrédients doit être vide");

    assertNotNull(menu.getCommandesGlobales(),
        "La liste des commandes ne doit pas être null");
    assertTrue(menu.getCommandesGlobales().isEmpty(),
        "La liste des commandes doit être vide");

    assertEquals(1, menu.getUtilisateurs().size(),
        "Il doit y avoir 1 utilisateur par défaut");
    Utilisateur chef = menu.getUtilisateurs().get(0);
    assertEquals("chef@pizza.fr", chef.getEmail());
    assertTrue(chef.estPizzaiolo());
  }

  /**
   * Vérifie qu'un utilisateur peut s'authentifier avec les bons identifiants.
   *
   * <p>Utilise le compte par défaut du chef pour valider que la méthode
   * renvoie bien l'objet utilisateur correspondant.</p>
   */
  @Test
  void testAuthentifierSucces() {
    Utilisateur u = menu.authentifier("chef@pizza.fr", "admin");
    assertNotNull(u, "L'authentification doit réussir pour le chef");
    assertEquals("Mario", u.getInfos().getNom());
  }

  /**
   * Vérifie que l'authentification échoue avec un mauvais mot de passe.
   *
   * <p>Tente une connexion avec un email valide mais un mot de passe erroné.
   * La méthode doit renvoyer null.</p>
   */
  @Test
  void testAuthentifierEchecMdp() {
    Utilisateur u = menu.authentifier("chef@pizza.fr", "mauvaisPassword");
    assertNull(u,
        "L'authentification doit échouer si le mot de passe est faux");
  }

  /**
   * Vérifie que l'authentification échoue avec un email inconnu.
   *
   * <p>Tente une connexion avec un email qui n'est pas dans le système.
   * La méthode doit renvoyer null.</p>
   */
  @Test
  void testAuthentifierEchecEmail() {
    Utilisateur u = menu.authentifier("inconnu@pizza.fr", "admin");
    assertNull(u, "L'authentification doit échouer si l'email n'existe pas");
  }

  /**
   * Vérifie la recherche d'un utilisateur par son email.
   *
   * <p>Recherche l'utilisateur "chef@pizza.fr" qui existe par défaut.
   * Vérifie que l'objet retourné n'est pas null et a le bon email.</p>
   */
  @Test
  void testTrouverUtilisateurExiste() {
    Utilisateur u = menu.trouverUtilisateur("chef@pizza.fr");
    assertNotNull(u, "L'utilisateur doit être trouvé");
    assertEquals("chef@pizza.fr", u.getEmail());
  }

  /**
   * Vérifie que la recherche renvoie null pour un email inexistant.
   *
   * <p>Recherche un email fantaisiste. La méthode doit renvoyer null.</p>
   */
  @Test
  void testTrouverUtilisateurInconnu() {
    Utilisateur u = menu.trouverUtilisateur("fantome@pizza.fr");
    assertNull(u,
        "La méthode doit renvoyer null si l'utilisateur n'existe pas");
  }

  /**
   * Vérifie l'ajout d'un nouvel utilisateur dans le système.
   *
   * <p>Crée un compte client, l'ajoute au menu, puis vérifie que :
   * 1. La taille de la liste des utilisateurs a augmenté.
   * 2. L'utilisateur est trouvable par son email.
   * 3. L'authentification fonctionne pour ce nouveau compte.</p>
   */
  @Test
  void testAjouterUtilisateur() {
    InformationPersonnelle infos = new InformationPersonnelle("Test", "User");
    CompteClient nouveau = new CompteClient("test@client.fr", "1234", infos);

    menu.ajouterUtilisateur(nouveau);

    assertEquals(2, menu.getUtilisateurs().size(),
        "La liste doit contenir 2 utilisateurs");
    assertNotNull(menu.trouverUtilisateur("test@client.fr"),
        "Le nouvel utilisateur doit être trouvable");

    assertNotNull(menu.authentifier("test@client.fr", "1234"));
  }

  /**
   * Vérifie l'ajout d'une commande dans l'historique global.
   *
   * <p>Crée une commande (nécessitant un client factice) et l'ajoute au menu.
   * Vérifie que la liste des commandes globales contient bien cet élément.</p>
   */
  @Test
  void testAjouterCommande() {
    InformationPersonnelle infos = new InformationPersonnelle("A", "B");
    CompteClient client = new CompteClient("c@c.fr", "mdp", infos);

    Commande c = new Commande(client);

    menu.ajouterCommande(c);

    assertEquals(1, menu.getCommandesGlobales().size(),
        "La liste des commandes doit contenir 1 élément");
    assertTrue(menu.getCommandesGlobales().contains(c));
  }
}