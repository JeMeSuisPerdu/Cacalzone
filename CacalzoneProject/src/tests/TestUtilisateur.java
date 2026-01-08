package tests;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.InformationPersonnelle;
import pizzas.Utilisateur;

/**
 * Classe de test pour la classe abstraite Utilisateur.
 *
 * <p>Utilise une implémentation concrète interne pour valider
 * le comportement du constructeur et des getters communs à tous
 * les types d'utilisateurs.
 * </p>
 */
class TestUtilisateur {

  private Utilisateur utilisateur;
  private InformationPersonnelle infos;

  /**
   * Classe interne concrète permettant d'instancier et tester Utilisateur.
   */
  class UtilisateurConcret extends Utilisateur {
    private static final long serialVersionUID = 1L;
    private boolean isPizzaiolo;

    public UtilisateurConcret(String email, String mdp,
        InformationPersonnelle infos, boolean isPizzaiolo) {
      super(email, mdp, infos);
      this.isPizzaiolo = isPizzaiolo;
    }

    @Override
    public boolean estPizzaiolo() {
      return this.isPizzaiolo;
    }
  }

  /**
   * Initialisation du contexte avant chaque test.
   *
   * <p>Crée un objet InformationPersonnelle valide qui servira de base
   * pour l'instanciation des utilisateurs de test.
   * </p>
   */
  @BeforeEach
  void setUp() {
    infos = new InformationPersonnelle("Dupont", "Jean", "Brest", 30);
  }

  /**
   * Vérifie le constructeur et les accesseurs de base.
   *
   * <p>Instancie la classe concrète avec des données valides. Vérifie que
   * l'email, le mot de passe et les informations personnelles sont bien
   * stockés et accessibles via les getters correspondants.
   * </p>
   */
  @Test
  void testConstructeurEtGetters() {
    utilisateur = new UtilisateurConcret("jean.dupont@email.com", "secret123",
        infos, false);

    assertAll("Vérification des attributs de base",
        () -> assertEquals("jean.dupont@email.com", utilisateur.getEmail(),
            "L'email doit correspondre"),
        () -> assertEquals("secret123", utilisateur.getMotDePasse(),
            "Le mot de passe doit correspondre"),
        () -> assertEquals(infos, utilisateur.getInfos(),
            "L'objet InformationPersonnelle doit être le même"),
        () -> assertEquals("Dupont", utilisateur.getInfos().getNom())
    );
  }

  /**
   * Vérifie le comportement de la méthode abstraite pour un pizzaïolo.
   *
   * <p>Instancie l'utilisateur concret en simulant un profil de pizzaïolo.
   * Vérifie que la méthode estPizzaiolo renvoie bien true.
   * </p>
   */
  @Test
  void testEstPizzaioloVrai() {
    utilisateur = new UtilisateurConcret("chef@pizzas.fr", "admin", infos,
        true);
    assertTrue(utilisateur.estPizzaiolo(),
        "Doit renvoyer true pour un pizzaiolo");
  }

  /**
   * Vérifie le comportement de la méthode abstraite pour un client standard.
   *
   * <p>Instancie l'utilisateur concret en simulant un profil de client.
   * Vérifie que la méthode estPizzaiolo renvoie bien false.
   * </p>
   */
  @Test
  void testEstPizzaioloFaux() {
    utilisateur = new UtilisateurConcret("client@pizzas.fr", "1234", infos,
        false);
    assertFalse(utilisateur.estPizzaiolo(),
        "Doit renvoyer false pour un utilisateur standard");
  }
}