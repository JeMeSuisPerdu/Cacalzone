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

  @BeforeEach
  void setUp() {
    menu = new Menu();
    service = new ServicePizzaiolo(menu);
  }

  @Test
  void testCreerPizzaSucces() {
    Pizza p = service.creerPizza("Royale", TypePizza.Viande);
    assertNotNull(p, "La pizza doit être créée");
    assertTrue(menu.getPizzas().contains(p), "La pizza doit être dans le menu");
  }

  @Test
  void testCreerPizzaDoublon() {
    service.creerPizza("Royale", TypePizza.Viande);
    Pizza doublon = service.creerPizza("royale", TypePizza.Vegetarienne); 
    
    assertNull(doublon, "La création d'un doublon doit renvoyer null");
    assertEquals(1, menu.getPizzas().size(), "Le menu ne doit contenir qu'une seule pizza");
  }

  @Test
  void testCreerIngredientSucces() {
    int res = service.creerIngredient("Olive", 0.5);
    assertEquals(0, res, "La création doit réussir");
    assertEquals(1, menu.getIngredients().size());
  }

  @Test
  void testCreerIngredientInvalide() {
    assertEquals(-1, service.creerIngredient("", 0.5), "Nom vide interdit");
    assertEquals(-3, service.creerIngredient("Or", -10.0), "Prix négatif interdit");
    assertTrue(menu.getIngredients().isEmpty());
  }

  @Test
  void testInterdireIngredient() {
    service.creerIngredient("Jambon", 2.0);
    boolean res = service.interdireIngredient("Jambon", TypePizza.Vegetarienne);
    
    assertTrue(res, "L'interdiction doit être prise en compte");
    
    // Vérification interne
    Ingredient jambon = menu.getIngredients().iterator().next();
    assertTrue(jambon.getTypesPizzaInterdits().contains(TypePizza.Vegetarienne));
  }

  @Test
  void testAjouterIngredientPizza() {
    Pizza p = service.creerPizza("Test", TypePizza.Viande);
    service.creerIngredient("Tomate", 1.0);
    
    int res = service.ajouterIngredientPizza(p, "Tomate");
    assertEquals(0, res, "L'ajout doit réussir");
    assertTrue(p.getIngredients().stream().anyMatch(i -> i.getNom().equals("Tomate")));
  }

  @Test
  void testAjouterIngredientInterdit() {
    Pizza p = service.creerPizza("Végé", TypePizza.Vegetarienne);
    service.creerIngredient("Jambon", 2.0);
    service.interdireIngredient("Jambon", TypePizza.Vegetarienne);
    
    int res = service.ajouterIngredientPizza(p, "Jambon");
    assertEquals(-3, res, "L'ajout d'un ingrédient interdit doit échouer (-3)");
    assertTrue(p.getIngredients().isEmpty());
  }

  @Test
  void testCommandeNonTraitees() {
    // Préparation d'une commande validée
    InformationPersonnelle info = new InformationPersonnelle("A", "B");
    CompteClient c = new CompteClient("cli@ent.fr", "mdp", info);
    Commande cmd = new Commande(c);
    
    Pizza p = new Pizza("P", TypePizza.Viande); // Il faut une pizza pour valider
    p.setPrix(10.0); // Prix manuel pour éviter calculs complexes
    try {
      cmd.ajouterPizza(p, 1);
    } catch (Exception e) 
    {
    	
    }
    
    cmd.valider(); // État -> VALIDEE
    menu.ajouterCommande(cmd);

    // Action
    List<Commande> atraiter = service.commandeNonTraitees();

    // Vérification
    assertEquals(1, atraiter.size());
    assertTrue(atraiter.contains(cmd));
    assertEquals(Etat.TRAITEE, cmd.getEtat(), "La commande doit passer à l'état TRAITEE");
  }

  @Test
  void testBeneficeToutesCommandes() {
    // On simule une commande traitée
    // Pizza : coût min 0 (pas d'ingrédients pour simplifier), prix vente 10 => Bénéfice 10
    Pizza p = new Pizza("Benef", TypePizza.Viande);
    p.setPrix(10.0);
    // Le prix min sera 0 car pas d'ingrédients.
    
    InformationPersonnelle info = new InformationPersonnelle("A", "B");
    CompteClient c = new CompteClient("cli@ent.fr", "mdp", info);
    Commande cmd = new Commande(c);
    try { cmd.ajouterPizza(p, 2); } catch (Exception e) {}
    
    cmd.valider();
    cmd.traiter(); // Important pour les stats
    menu.ajouterCommande(cmd);
    
    // Le menu contient aussi la pizza pour que les calculs se fassent bien
    menu.getPizzas().add(p); 

    assertEquals(20.0, service.beneficeToutesCommandes(), 0.01);
  }
}