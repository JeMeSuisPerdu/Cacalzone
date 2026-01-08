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

  /**
   * Initialisation du contexte avant chaque exécution de test.
   *
   * <p>Crée un client, une nouvelle commande vierge associée à ce client,
   * ainsi que deux pizzas de test ("Luxe" et "Basique") avec des ingrédients
   * et des prix cohérents pour les calculs ultérieurs.
   * </p>
   */
  @BeforeEach
  void setUp() {
    InformationPersonnelle infos = new InformationPersonnelle("Durand",
        "Alice");
    client = new CompteClient("alice@mail.com", "1234", infos);

    commande = new Commande(client);

    p1 = new Pizza("Luxe", TypePizza.Viande);
    p1.ajouterIngredient(new Ingredient("Truffe", 10.0));

    p2 = new Pizza("Basique", TypePizza.Regionale);
    p2.ajouterIngredient(new Ingredient("Légumes", 5.0));
  }

  /**
   * Vérifie l'état initial d'une commande juste après sa construction.
   *
   * <p>S'assure que le client est bien associé, que l'état est CREEE,
   * que la liste des pizzas est vide et que la date est générée.
   * </p>
   */
  @Test
  void testConstructeur() {
    assertAll("État initial de la commande",
        () -> assertEquals(client, commande.getClient(),
            "Le client doit correspondre"),
        () -> assertEquals(Etat.CREEE, commande.getEtat(),
            "L'état initial doit être CREEE"),
        () -> assertTrue(commande.getPizzas().isEmpty(),
            "La liste des pizzas doit être vide"),
        () -> assertNotNull(commande.getDate(),
            "La date ne doit pas être null")
    );
  }

  /**
   * Vérifie l'ajout de pizzas valides à la commande.
   *
   * <p>Teste l'ajout d'une première pizza, puis l'ajout d'une quantité
   * supplémentaire pour cette même pizza afin de vérifier le cumul.
   * </p>
   *
   * @throws CommandeException ne doit pas être levée ici.
   */
  @Test
  void testAjouterPizzaValide() throws CommandeException {
    boolean ajout1 = commande.ajouterPizza(p1, 1);
    assertTrue(ajout1, "L'ajout doit réussir");
    assertEquals(1, commande.getPizzas().get(p1), "La quantité doit être 1");

    boolean ajout2 = commande.ajouterPizza(p1, 2);
    assertTrue(ajout2);
    assertEquals(3, commande.getPizzas().get(p1),
        "La quantité doit passer à 3 (1+2)");
  }

  /**
   * Vérifie que l'ajout est refusé si les paramètres sont invalides.
   *
   * <p>Teste l'ajout avec une pizza null, une quantité nulle ou une quantité
   * négative. La méthode doit renvoyer false et la liste doit rester vide.
   * </p>
   *
   * @throws CommandeException ne doit pas être levée ici.
   */
  @Test
  void testAjouterPizzaInvalide() throws CommandeException {
    assertFalse(commande.ajouterPizza(null, 1), "Ne doit pas ajouter null");

    assertFalse(commande.ajouterPizza(p1, 0),
        "Ne doit pas ajouter quantité 0");
    assertFalse(commande.ajouterPizza(p1, -5),
        "Ne doit pas ajouter quantité négative");

    assertTrue(commande.getPizzas().isEmpty(), "La map doit rester vide");
  }

  /**
   * Vérifie la levée d'une exception lors d'une tentative de modification
   * interdite.
   *
   * <p>Valide d'abord la commande, puis tente d'ajouter une pizza. Une
   * exception doit être levée car la commande n'est plus à l'état CREEE.
   * </p>
   */
  @Test
  void testAjouterPizzaException() throws CommandeException {
    commande.ajouterPizza(p1, 1);
    commande.valider();

    CommandeException e = assertThrows(CommandeException.class, () -> {
      commande.ajouterPizza(p2, 1);
    });

    assertNotNull(e.getMessage());
  }

  /**
   * Vérifie que la validation d'une commande fonctionne correctement.
   *
   * <p>Ajoute une pizza puis valide. La méthode doit renvoyer true et
   * l'état doit passer à VALIDEE.
   * </p>
   *
   * @throws CommandeException ne doit pas être levée ici.
   */
  @Test
  void testValiderSucces() throws CommandeException {
    commande.ajouterPizza(p1, 1);

    boolean res = commande.valider();

    assertTrue(res,
        "La validation doit réussir si la commande n'est pas vide");
    assertEquals(Etat.VALIDEE, commande.getEtat());
  }

  /**
   * Vérifie que la validation échoue si la commande est vide.
   *
   * <p>Tente de valider une commande sans pizzas. La méthode doit renvoyer
   * false et l'état doit rester CREEE.
   * </p>
   */
  @Test
  void testValiderEchec() {
    boolean res = commande.valider();

    assertFalse(res, "La validation doit échouer si la commande est vide");
    assertEquals(Etat.CREEE, commande.getEtat());
  }

  /**
   * Vérifie le passage de l'état VALIDEE à TRAITEE.
   *
   * <p>Simule le cycle complet : ajout, validation, puis traitement par le
   * pizzaïolo.
   * </p>
   *
   * @throws CommandeException ne doit pas être levée ici.
   */
  @Test
  void testTraiter() throws CommandeException {
    commande.ajouterPizza(p1, 1);
    commande.valider();

    commande.traiter();

    assertEquals(Etat.TRAITEE, commande.getEtat(),
        "L'état doit passer à TRAITEE");
  }

  /**
   * Vérifie l'annulation d'une commande en cours de création.
   *
   * <p>Ajoute une pizza puis annule. L'état doit passer à ANNULEE et le
   * contenu de la commande doit être vidé.
   * </p>
   *
   * @throws CommandeException ne doit pas être levée ici.
   */
  @Test
  void testAnnulerDepuisCreee() throws CommandeException {
    commande.ajouterPizza(p1, 1);

    commande.annuler();

    assertEquals(Etat.ANNULEE, commande.getEtat());
    assertTrue(commande.getPizzas().isEmpty(),
        "Le contenu doit être vidé après annulation");
  }

  /**
   * Vérifie l'annulation d'une commande qui a déjà été validée.
   *
   * <p>Valide une commande puis l'annule. L'état doit passer à ANNULEE et
   * le contenu doit être vidé.
   * </p>
   *
   * @throws CommandeException ne doit pas être levée ici.
   */
  @Test
  void testAnnulerDepuisValidee() throws CommandeException {
    commande.ajouterPizza(p1, 1);
    commande.valider();

    commande.annuler();

    assertEquals(Etat.ANNULEE, commande.getEtat());
    assertTrue(commande.getPizzas().isEmpty(), "Le contenu doit être vidé");
  }

  /**
   * Vérifie le calcul du prix total de la commande.
   *
   * <p>Ajoute deux types de pizzas avec des quantités différentes et vérifie
   * que la somme totale correspond au calcul attendu.
   * </p>
   *
   * @throws CommandeException ne doit pas être levée ici.
   */
  @Test
  void testGetPrixTotal() throws CommandeException {
    commande.ajouterPizza(p1, 1);
    commande.ajouterPizza(p2, 2);

    assertEquals(28.0, commande.getPrixTotal(), 0.01,
        "Le prix total est incorrect");
  }
}