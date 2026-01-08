package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import pizzas.Evaluation;

/**
 * Classe de test pour la classe Evaluation.
 *
 * <p>Vérifie principalement la logique de validation de la note dans le
 * constructeur (bornage entre 0 et 5) et le formatage de l'affichage.</p>
 */
class TestEvaluation {

  /**
   * Vérifie la création d'une évaluation avec des données valides.
   *
   * <p>Instancie une évaluation avec une note correcte (4/5) et un commentaire.
   * Vérifie que les accesseurs retournent les valeurs exactes.</p>
   */
  @Test
  void testConstructeurValide() {
    Evaluation eval = new Evaluation(4, "Très bonne pizza", "client@test.fr");

    assertEquals(4, eval.getNote(),
        "La note doit être conservée si elle est valide");
    assertEquals("Très bonne pizza", eval.getCommentaire());
    assertEquals("client@test.fr", eval.getEmailClient());
  }

  /**
   * Vérifie que le constructeur ramène une note négative à 0.
   *
   * <p>Instancie une évaluation avec une note de -5.
   * Vérifie que la note enregistrée est bien 0.</p>
   */
  @Test
  void testConstructeurNoteTropBasse() {
    Evaluation eval = new Evaluation(-5, "Pas bon", "client@test.fr");

    assertEquals(0, eval.getNote(),
        "Une note négative doit être ramenée à 0");
  }

  /**
   * Vérifie que le constructeur ramène une note supérieure à 5 à 5.
   *
   * <p>Instancie une évaluation avec une note de 20.
   * Vérifie que la note enregistrée est bien 5.</p>
   */
  @Test
  void testConstructeurNoteTropHaute() {
    Evaluation eval = new Evaluation(20, "Incroyable", "client@test.fr");

    assertEquals(5, eval.getNote(), "Une note > 5 doit être ramenée à 5");
  }

  /**
   * Vérifie la création d'une évaluation sans commentaire textuel.
   *
   * <p>Instancie une évaluation avec un commentaire null.
   * Vérifie que l'objet est créé et que le commentaire est bien null.</p>
   */
  @Test
  void testConstructeurSansCommentaire() {
    Evaluation eval = new Evaluation(3, null, "client@test.fr");

    assertEquals(3, eval.getNote());
    assertNull(eval.getCommentaire(), "Le commentaire doit être null");
  }

  /**
   * Vérifie le formatage de la méthode toString avec un commentaire.
   *
   * <p>Crée une évaluation complète et vérifie que la chaîne retournée
   * contient à la fois la note et le texte du commentaire.</p>
   */
  @Test
  void testToStringAvecCommentaire() {
    Evaluation eval = new Evaluation(4, "Super", "client@test.fr");
    String s = eval.toString();

    assertTrue(s.contains("4/5"), "La chaîne doit contenir la note");
    assertTrue(s.contains("Super"), "La chaîne doit contenir le commentaire");
  }

  /**
   * Vérifie le formatage de la méthode toString sans commentaire.
   *
   * <p>Crée une évaluation sans commentaire et vérifie que la chaîne
   * se termine proprement après la note, sans séparateur inutile.</p>
   */
  @Test
  void testToStringSansCommentaire() {
    Evaluation eval = new Evaluation(5, null, "client@test.fr");
    String s = eval.toString();

    assertTrue(s.contains("5/5"), "La chaîne doit contenir la note");
    assertTrue(s.endsWith("/5"),
        "La chaîne ne doit pas avoir de suffixe inutile");
  }
}