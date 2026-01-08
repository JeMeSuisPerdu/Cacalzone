package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.Ingredient;
import pizzas.Menu;
import pizzas.ServiceSauvegarde;

/**
 * Classe de test pour ServiceSauvegarde.
 *
 * <p>Vérifie la capacité à écrire l'état du menu sur le disque et à le restaurer.
 * S'assure également que la référence de l'objet Menu est préservée lors du
 * chargement (mise à jour du contenu et non remplacement de l'objet).
 * </p>
 */
class TestServiceSauvegarde {

  private Menu menu;
  private ServiceSauvegarde service;
  private final String fichierTest = "test_sauvegarde.ser";

  /**
   * Initialisation du contexte avant chaque test.
   *
   * <p>Crée un nouveau menu et le service de sauvegarde associé.
   * </p>
   */
  @BeforeEach
  void setUp() {
    menu = new Menu();
    service = new ServiceSauvegarde(menu);
  }

  /**
   * Nettoyage après chaque test.
   *
   * <p>Supprime le fichier de sauvegarde temporaire créé pendant le test
   * pour ne pas laisser de traces sur le disque.
   * </p>
   */
  @AfterEach
  void tearDown() {
    File f = new File(fichierTest);
    if (f.exists()) {
      f.delete();
    }
  }

  /**
   * Vérifie le cycle complet de sauvegarde et de chargement.
   *
   * <p>Scénario :
   * 1. Ajout d'un ingrédient au menu.
   * 2. Sauvegarde sur disque.
   * 3. Effacement des données en mémoire.
   * 4. Chargement depuis le disque.
   * 5. Vérification que l'ingrédient est bien restauré.
   * </p>
   *
   * @throws IOException ne doit pas être levée ici.
   */
  @Test
  void testSauvegarderEtCharger() throws IOException {
    Ingredient ing = new Ingredient("TestIng", 10.0);
    menu.getIngredients().add(ing);
    assertFalse(menu.getIngredients().isEmpty());

    service.sauvegarderDonnees(fichierTest);

    menu.getIngredients().clear();
    assertTrue(menu.getIngredients().isEmpty(),
        "Le menu doit être vide avant chargement");

    service.chargerDonnees(fichierTest);

    assertEquals(1, menu.getIngredients().size(),
        "La liste doit être restaurée");
    assertEquals("TestIng", menu.getIngredients().iterator().next().getNom());
  }

  /**
   * Vérifie le comportement si le fichier demandé n'existe pas.
   *
   * <p>Tente de charger un fichier inexistant. Une IOException doit être levée.
   * </p>
   */
  @Test
  void testChargerFichierInexistant() {
    assertThrows(IOException.class, () -> {
      service.chargerDonnees("fichier_qui_n_existe_pas.ser");
    }, "Doit lever une exception si le fichier est introuvable");
  }

  /**
   * Vérifie la persistance de la référence de l'objet Menu.
   *
   * <p>Le chargement ne doit pas remplacer l'instance de Menu par une nouvelle,
   * mais mettre à jour son contenu (listes). C'est crucial pour que les
   * contrôleurs graphiques, qui possèdent une référence vers le menu,
   * voient les changements.
   * </p>
   *
   * @throws IOException ne doit pas être levée ici.
   */
  @Test
  void testPersistanceReferenceMenu() throws IOException {
    Menu referenceOriginale = menu;

    service.sauvegarderDonnees(fichierTest);

    service.chargerDonnees(fichierTest);

    assertTrue(menu == referenceOriginale,
        "L'objet Menu ne doit pas être remplacé, mais mis à jour");
  }
}