package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.Commande;
import pizzas.CompteClient;
import pizzas.InformationPersonnelle;

/**
 * Classe de test pour la classe CompteClient.
 * <p>
 * Elle vérifie le comportement spécifique des comptes clients :
 * l'initialisation de l'historique, la vérification du rôle (qui ne doit pas
 * être pizzaiolo) et l'ajout de commandes dans l'historique personnel.
 * </p>
 */
class TestCompteClient {

  /** Le compte client testé à chaque méthode. */
  private CompteClient client;

  /** Les informations personnelles utilisées pour le test. */
  private InformationPersonnelle infos;

  /**
   * Initialisation des objets avant chaque test.
   * Crée un client standard avec des données factices.
   */
  @BeforeEach
  void setUp() {
    infos = new InformationPersonnelle("Martin", "Paul", "Brest", 25);
    client = new CompteClient("paul.martin@email.com", "password123", infos);
  }

  /**
   * Vérifie que le constructeur initialise correctement le compte.
   * 
   * <p>On s'assure que :
   * 1. Les données de l'utilisateur (email, mdp) sont bien transmises à la
   * classe mère.
   * 2. La liste d'historique des commandes est bien instanciée (non null)
   * mais vide au départ.
   * </p>
   */
  @Test
  void testConstructeurEtInitialisation() {
    // Vérification des attributs hérités
    assertEquals("paul.martin@email.com", client.getEmail(),
        "L'email doit correspondre à celui passé au constructeur");
    assertEquals("password123", client.getMotDePasse(),
        "Le mot de passe doit correspondre");
    assertEquals(infos, client.getInfos(),
        "Les infos personnelles doivent correspondre");

    // Vérification de l'initialisation de la liste
    assertNotNull(client.getHistoriqueCommandes(),
        "La liste d'historique ne doit pas être null");
    assertTrue(client.getHistoriqueCommandes().isEmpty(),
        "L'historique doit être vide à la création du compte");
  }

  /**
   * Vérifie que la méthode estPizzaiolo renvoie bien false.
   * Un CompteClient ne doit jamais être considéré comme un pizzaiolo.
   */
  @Test
  void testEstPizzaiolo() {
    assertFalse(client.estPizzaiolo(),
        "Un CompteClient ne doit pas être identifié comme pizzaiolo");
  }

  /**
   * Vérifie l'ajout d'une commande à l'historique du client.
   * 
   * <p>Scénario :
   * 1. Création d'une commande liée au client.
   * 2. Appel de ajouterCommande.
   * 3. Vérification que la liste contient bien cette commande.
   * </p>
   */
  @Test
  void testAjouterCommande() {
    // Création d'une commande pour ce client
    Commande commande = new Commande(client);

    // Action : ajout
    client.ajouterCommande(commande);

    // Vérification
    assertEquals(1, client.getHistoriqueCommandes().size(),
        "La taille de l'historique doit être de 1 après l'ajout");
    assertTrue(client.getHistoriqueCommandes().contains(commande),
        "L'historique doit contenir la commande ajoutée");
  }
}