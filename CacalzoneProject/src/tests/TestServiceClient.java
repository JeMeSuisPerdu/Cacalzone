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

  @BeforeEach
  void setUp() {
    menu = new Menu();
    service = new ServiceClient(menu);

    // Ajout de pizzas au menu pour les tests de filtres
    p1 = new Pizza("Reine", TypePizza.Viande);
    p1.ajouterIngredient(new Ingredient("Jambon", 2.0));
    p1.setPrix(12.0);
    menu.getPizzas().add(p1);

    p2 = new Pizza("4 Fromages", TypePizza.Vegetarienne);
    p2.ajouterIngredient(new Ingredient("Mozzarella", 2.0));
    p2.setPrix(14.0);
    menu.getPizzas().add(p2);
  }

  @Test
  void testInscriptionSucces() {
    InformationPersonnelle infos = new InformationPersonnelle("Test", "User");
    int res = service.inscription("new@client.fr", "1234", infos);
    
    assertEquals(0, res, "L'inscription doit réussir");
    assertNotNull(menu.trouverUtilisateur("new@client.fr"), "L'utilisateur doit être dans le menu");
  }

  @Test
  void testInscriptionDoublon() {
    InformationPersonnelle infos = new InformationPersonnelle("Test", "User");
    service.inscription("doublon@client.fr", "1234", infos);
    
    // Deuxième tentative
    int res = service.inscription("doublon@client.fr", "0000", infos);
    assertEquals(-1, res, "L'inscription doit échouer pour un email existant");
  }

  @Test
  void testConnexion() {
    InformationPersonnelle infos = new InformationPersonnelle("Test", "User");
    service.inscription("login@client.fr", "secret", infos);

    boolean success = service.connexion("login@client.fr", "secret");
    assertTrue(success, "La connexion doit réussir avec le bon mot de passe");

    boolean fail = service.connexion("login@client.fr", "wrong");
    assertFalse(fail, "La connexion doit échouer avec le mauvais mot de passe");
  }

  @Test
  void testDebuterCommandeNonConnecte() {
    assertThrows(NonConnecteException.class, () -> {
      service.debuterCommande();
    }, "Doit lever une exception si non connecté");
  }

  @Test
  void testDebuterCommandeConnecte() throws NonConnecteException {
    InformationPersonnelle infos = new InformationPersonnelle("A", "B");
    service.inscription("c@c.fr", "mdp", infos);
    service.connexion("c@c.fr", "mdp");

    Commande c = service.debuterCommande();
    assertNotNull(c, "La commande créée ne doit pas être null");
    assertEquals(c, service.getCommandeActive());
  }

  @Test
  void testAjouterPizzaCommande() throws Exception {
    // Connexion
    InformationPersonnelle infos = new InformationPersonnelle("A", "B");
    service.inscription("user@test.fr", "mdp", infos);
    service.connexion("user@test.fr", "mdp");

    // Création commande
    Commande c = service.debuterCommande();
    
    // Ajout pizza
    service.ajouterPizza(p1, 2, c);
    
    assertEquals(2, c.getPizzas().get(p1), "La pizza doit être ajoutée avec la quantité 2");
  }

  @Test
  void testValiderCommande() throws Exception {
    InformationPersonnelle infos = new InformationPersonnelle("A", "B");
    service.inscription("valider@test.fr", "mdp", infos);
    service.connexion("valider@test.fr", "mdp");

    Commande c = service.debuterCommande();
    service.ajouterPizza(p1, 1, c);
    
    service.validerCommande(c);
    
    assertEquals(Etat.VALIDEE, c.getEtat(), "L'état doit passer à VALIDEE");
    assertTrue(menu.getCommandesGlobales().contains(c), "La commande doit être dans l'historique");
  }

  @Test
  void testFiltrePrix() {
    service.ajouterFiltre(13.0); // Max 13€
    Set<Pizza> result = service.selectionPizzaFiltres();
    
    assertTrue(result.contains(p1), "Reine (12€) doit être sélectionnée");
    assertFalse(result.contains(p2), "4 Fromages (14€) ne doit pas être sélectionnée");
  }

  @Test
  void testFiltreIngredient() {
    service.ajouterFiltre("Jambon"); // Exclusion
    Set<Pizza> result = service.selectionPizzaFiltres();
    
    assertFalse(result.contains(p1), "Reine (avec Jambon) doit être exclue");
    assertTrue(result.contains(p2), "4 Fromages (sans Jambon) doit être présente");
  }
}