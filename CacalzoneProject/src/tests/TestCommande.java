package tests;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.Commande;
import pizzas.Commande.Etat;
import pizzas.CommandeException;
import pizzas.CompteClient;
import pizzas.InformationPersonnelle;
import pizzas.Ingredient;
import pizzas.Pizza;
import pizzas.TypePizza;

/**
 * Classe de test pour la classe Commande.
 * 
 * <p>Vérifie le cycle de vie d'une commande (création, ajout, validation, 
 * traitement, annulation), la gestion des erreurs (exceptions) et 
 * les calculs financiers.
 * </p>
 */
class TestCommande {

  private Commande commande;
  private CompteClient client;
  private Pizza p1;
  private Pizza p2;

  @BeforeEach
  void setUp() {
    // 1. Création du client
    InformationPersonnelle infos = new InformationPersonnelle("Durand", "Alice");
    client = new CompteClient("alice@mail.com", "1234", infos);

    // 2. Création de la commande
    commande = new Commande(client);

    // 3. Préparation des pizzas et ingrédients pour avoir des prix cohérents
    // Pizza 1 : Prix min ~ 14.0 (10 + 40%)
    p1 = new Pizza("Luxe", TypePizza.Viande);
    p1.ajouterIngredient(new Ingredient("Truffe", 10.0)); 
    
    // Pizza 2 : Prix min ~ 7.0 (5 + 40%)
    p2 = new Pizza("Basique", TypePizza.Regionale);
    p2.ajouterIngredient(new Ingredient("Légumes", 5.0));
  }

  @Test
  void testConstructeur() {
    assertAll("État initial de la commande",
        () -> assertEquals(client, commande.getClient(), "Le client doit correspondre"),
        () -> assertEquals(Etat.CREEE, commande.getEtat(), "L'état initial doit être CREEE"),
        () -> assertTrue(commande.getPizzas().isEmpty(), "La liste des pizzas doit être vide"),
        () -> assertNotNull(commande.getDate(), "La date ne doit pas être null")
    );
  }

  @Test
  void testAjouterPizzaValide() throws CommandeException {
    // Ajout d'une pizza
    boolean ajout1 = commande.ajouterPizza(p1, 1);
    assertTrue(ajout1, "L'ajout doit réussir");
    assertEquals(1, commande.getPizzas().get(p1), "La quantité doit être 1");

    // Ajout de la même pizza (cumul quantité)
    boolean ajout2 = commande.ajouterPizza(p1, 2);
    assertTrue(ajout2);
    assertEquals(3, commande.getPizzas().get(p1), "La quantité doit passer à 3 (1+2)");
  }

  @Test
  void testAjouterPizzaInvalide() throws CommandeException {
    // Pizza null
    assertFalse(commande.ajouterPizza(null, 1), "Ne doit pas ajouter null");
    
    // Quantité 0 ou négative
    assertFalse(commande.ajouterPizza(p1, 0), "Ne doit pas ajouter quantité 0");
    assertFalse(commande.ajouterPizza(p1, -5), "Ne doit pas ajouter quantité négative");
    
    assertTrue(commande.getPizzas().isEmpty(), "La map doit rester vide");
  }

  @Test
  void testAjouterPizzaException() throws CommandeException {
    // On valide d'abord la commande pour changer son état
    commande.ajouterPizza(p1, 1);
    commande.valider(); // État -> VALIDEE

    // Tentative d'ajout alors que l'état n'est plus CREEE
    CommandeException e = assertThrows(CommandeException.class, () -> {
      commande.ajouterPizza(p2, 1);
    });
    
    // Vérification optionnelle du message (si ton constructeur CommandeException gère le message)
    assertNotNull(e.getMessage()); 
  }

  @Test
  void testValiderSucces() throws CommandeException {
    commande.ajouterPizza(p1, 1);
    
    boolean res = commande.valider();
    
    assertTrue(res, "La validation doit réussir si la commande n'est pas vide");
    assertEquals(Etat.VALIDEE, commande.getEtat());
  }

  @Test
  void testValiderEchec() {
    // Commande vide
    boolean res = commande.valider();
    
    assertFalse(res, "La validation doit échouer si la commande est vide");
    assertEquals(Etat.CREEE, commande.getEtat());
  }

  @Test
  void testTraiter() throws CommandeException {
    commande.ajouterPizza(p1, 1);
    commande.valider(); // VALIDEE
    
    commande.traiter(); // Action
    
    assertEquals(Etat.TRAITEE, commande.getEtat(), "L'état doit passer à TRAITEE");
  }

  @Test
  void testAnnulerDepuisCreee() throws CommandeException {
    commande.ajouterPizza(p1, 1);
    
    commande.annuler();
    
    assertEquals(Etat.ANNULEE, commande.getEtat());
    assertTrue(commande.getPizzas().isEmpty(), "Le contenu doit être vidé après annulation");
  }

  @Test
  void testAnnulerDepuisValidee() throws CommandeException {
    commande.ajouterPizza(p1, 1);
    commande.valider();
    
    commande.annuler();
    
    assertEquals(Etat.ANNULEE, commande.getEtat());
    assertTrue(commande.getPizzas().isEmpty(), "Le contenu doit être vidé");
  }

  @Test
  void testGetPrixTotal() throws CommandeException {
    // p1 ~ 14.0€, p2 ~ 7.0€
    commande.ajouterPizza(p1, 1); // 14.0
    commande.ajouterPizza(p2, 2); // 7.0 * 2 = 14.0
    
    // Total attendu : 28.0
    assertEquals(28.0, commande.getPrixTotal(), 0.01, "Le prix total est incorrect");
  }
}