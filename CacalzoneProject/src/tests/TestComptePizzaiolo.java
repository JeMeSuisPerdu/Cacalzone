package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.ComptePizzaiolo;
import pizzas.InformationPersonnelle;

/**
 * Classe de test pour la classe ComptePizzaiolo.
 *
 * <p>Cette classe vérifie le comportement spécifique du compte réservé au
 * gestionnaire de la pizzeria. Elle s'assure principalement que l'héritage
 * des données fonctionne et que les droits spécifiques (méthode
 * {@link #testEstPizzaiolo()}) sont correctement attribués.
 * </p>
 */
class TestComptePizzaiolo {

  /** L'instance de ComptePizzaiolo utilisée pour les tests. */
  private ComptePizzaiolo pizzaiolo;

  /** Les informations personnelles factices pour le test. */
  private InformationPersonnelle infos;

  /**
   * Initialisation des objets avant chaque exécution de test.
   *
   * <p>Crée un profil de pizzaïolo standard ("Mario") avec des données
   * factices pour garantir un environnement de test propre.
   * </p>
   */
  @BeforeEach
  void setUp() {
    infos = new InformationPersonnelle("Mario", "Chef", "Rome", 45);
    pizzaiolo = new ComptePizzaiolo("mario@pizzeria.com", "secret", infos);
  }

  /**
   * Vérifie que le constructeur initialise correctement les données.
   *
   * <p>Ce test s'assure que les paramètres passés au constructeur de
   * {@code ComptePizzaiolo} (email, mot de passe, infos) sont bien transmis
   * à la classe mère {@code Utilisateur} et sont accessibles via les getters.
   * </p>
   */
  @Test
  void testConstructeur() {
    assertEquals("mario@pizzeria.com", pizzaiolo.getEmail(),
        "L'email du pizzaïolo doit correspondre à celui fourni");
    assertEquals("secret", pizzaiolo.getMotDePasse(),
        "Le mot de passe doit correspondre à celui fourni");
    assertEquals(infos, pizzaiolo.getInfos(),
        "Les informations personnelles doivent être correctement associées");
  }

  /**
   * Vérifie que ce type de compte est bien identifié comme un compte Pizzaïolo.
   *
   * <p>Contrairement au {@code CompteClient}, la méthode {@code estPizzaiolo()}
   * doit impérativement renvoyer {@code true} pour permettre l'accès à
   * l'interface de gestion réservée au personnel.
   * </p>
   */
  @Test
  void testEstPizzaiolo() {
    assertTrue(pizzaiolo.estPizzaiolo(),
        "La méthode estPizzaiolo() doit retourner true pour ce compte");
  }
}