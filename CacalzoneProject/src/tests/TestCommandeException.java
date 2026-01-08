package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import pizzas.CommandeException;

/**
 * Classe de test pour l'exception personnalisée CommandeException.
 *
 * <p>Vérifie que l'exception peut être instanciée correctement avec ou sans
 * message de détail, ce qui est crucial pour le retour d'information
 * dans l'interface graphique.</p>
 */
class TestCommandeException {

  /**
   * Vérifie le constructeur par défaut (sans message).
   *
   * <p>Instancie l'exception sans paramètre. Vérifie que le message
   * récupéré via getMessage() est bien null, conformément au comportement
   * par défaut de la classe Exception.</p>
   */
  @Test
  void testConstructeurDefaut() {
    CommandeException e = new CommandeException();
    assertNull(e.getMessage(), "Le message doit être null par défaut");
  }

  /**
   * Vérifie le constructeur prenant un message en paramètre.
   *
   * <p>Instancie l'exception avec une chaîne de caractères spécifique.
   * C'est ce constructeur qui est utilisé pour afficher les erreurs
   * dans les popups de l'application. Vérifie que le message est conservé
   * et récupérable.</p>
   */
  @Test
  void testConstructeurAvecMessage() {
    String msg = "Impossible de valider une commande vide";
    CommandeException e = new CommandeException(msg);

    assertEquals(msg, e.getMessage(),
        "Le message de l'exception doit être conservé");
  }
}