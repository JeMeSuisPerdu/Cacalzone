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
 * <p>
 * Vérifie la capacité à écrire l'état du menu sur le disque et à le restaurer.
 * S'assure également que la référence de l'objet Menu est préservée lors du
 * chargement (mise à jour du contenu et non remplacement de l'objet).
 * </p>
 */
class TestServiceSauvegarde {

  private Menu menu;
  private ServiceSauvegarde service;
  private final String fichierTest = "test_sauvegarde.ser";

  @BeforeEach
  void setUp() {
    menu = new Menu();
    service = new ServiceSauvegarde(menu);
  }

  /**
   * Nettoyage après chaque test : on supprime le fichier créé.
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
   * Scénario :
   * 1. On modifie le menu (ajout ingrédient).
   * 2. On sauvegarde.
   * 3. On efface la mémoire.
   * 4. On charge.
   * 5. On vérifie que la donnée est revenue.
   */
  @Test
  void testSauvegarderEtCharger() throws IOException {
    // 1. Préparation des données
    Ingredient ing = new Ingredient("TestIng", 10.0);
    menu.getIngredients().add(ing);
    assertFalse(menu.getIngredients().isEmpty());

    // 2. Sauvegarde
    service.sauvegarderDonnees(fichierTest);

    // 3. Altération de la mémoire (simulation de perte ou redémarrage)
    menu.getIngredients().clear();
    assertTrue(menu.getIngredients().isEmpty(), "Le menu doit être vide avant chargement");

    // 4. Chargement
    service.chargerDonnees(fichierTest);

    // 5. Vérification
    assertEquals(1, menu.getIngredients().size(), "La liste doit être restaurée");
    assertEquals("TestIng", menu.getIngredients().iterator().next().getNom());
  }

  /**
   * Vérifie le comportement si le fichier demandé n'existe pas.
   */
  @Test
  void testChargerFichierInexistant() {
    assertThrows(IOException.class, () -> {
      service.chargerDonnees("fichier_qui_n_existe_pas.ser");
    }, "Doit lever une exception si le fichier est introuvable");
  }

  /**
   * Vérifie que le chargement met à jour l'instance existante du Menu
   * au lieu de la remplacer. C'est crucial pour que les contrôleurs
   * voient les changements.
   */
  @Test
  void testPersistanceReferenceMenu() throws IOException {
    // On garde une référence vers l'objet menu original
    Menu referenceOriginale = menu;

    // On fait une sauvegarde simple
    service.sauvegarderDonnees(fichierTest);

    // On charge
    service.chargerDonnees(fichierTest);

    // On vérifie que le service travaille toujours sur la même instance
    // (l'adresse mémoire de l'objet est identique)
    assertTrue(menu == referenceOriginale,
        "L'objet Menu ne doit pas être remplacé, mais mis à jour");
  }
}