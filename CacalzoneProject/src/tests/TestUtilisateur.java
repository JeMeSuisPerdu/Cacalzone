package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.InformationPersonnelle;
import pizzas.Utilisateur;

/**
 * Classe de test pour la classe abstraite Utilisateur.
 * Utilise une implémentation concrète interne pour valider
 * le comportement du constructeur et des getters communs.
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

        public UtilisateurConcret(String email, String mdp, InformationPersonnelle infos, boolean isPizzaiolo) {
            super(email, mdp, infos);
            this.isPizzaiolo = isPizzaiolo;
        }

        @Override
        public boolean estPizzaiolo() {
            return this.isPizzaiolo;
        }
    }

    @BeforeEach
    void setUp() {
        // Création de données de test (Infos basiques)
        infos = new InformationPersonnelle("Dupont", "Jean", "Brest", 30);
    }

    @Test
    void testConstructeurEtGetters() {
        // Action : Instanciation
        utilisateur = new UtilisateurConcret("jean.dupont@email.com", "secret123", infos, false);

        // Vérification
        assertAll("Vérification des attributs de base",
            () -> assertEquals("jean.dupont@email.com", utilisateur.getEmail(), "L'email doit correspondre"),
            () -> assertEquals("secret123", utilisateur.getMotDePasse(), "Le mot de passe doit correspondre"),
            () -> assertEquals(infos, utilisateur.getInfos(), "L'objet InformationPersonnelle doit être le même"),
            // Vérifie aussi que l'objet infos contient bien les bonnes données
            () -> assertEquals("Dupont", utilisateur.getInfos().getNom())
        );
    }

    @Test
    void testEstPizzaioloVrai() {
        // Test avec un utilisateur configuré comme pizzaiolo
        utilisateur = new UtilisateurConcret("chef@pizzas.fr", "admin", infos, true);
        assertTrue(utilisateur.estPizzaiolo(), "Doit renvoyer true pour un pizzaiolo");
    }

    @Test
    void testEstPizzaioloFaux() {
        // Test avec un utilisateur configuré comme client (pas pizzaiolo)
        utilisateur = new UtilisateurConcret("client@pizzas.fr", "1234", infos, false);
        assertFalse(utilisateur.estPizzaiolo(), "Doit renvoyer false pour un utilisateur standard");
    }
}