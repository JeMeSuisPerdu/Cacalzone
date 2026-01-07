package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.*;
import java.io.File;

/**
 * Classe de tests unitaires pour la classe {@link ServiceSauvegarde}.
 * <p>
 * Ces tests vérifient le bon fonctionnement de la sauvegarde et du chargement
 * des données du menu et des utilisateurs, notamment :
 * <ul>
 *   <li>sauvegarde d'un menu et des comptes clients dans un fichier</li>
 *   <li>chargement des données depuis un fichier pour restaurer l'état</li>
 * </ul>
 * <p>
 * L'objectif est de s'assurer que le service de sauvegarde permet de persister
 * et restaurer correctement les données de l'application.
 * 
 */
public class TestServiceSauvegarde {

    /** Service de sauvegarde utilisé pour les tests */
    private ServiceSauvegarde service;

    /** Menu associé au service de sauvegarde */
    private Menu menu;

    /** Nom du fichier utilisé pour les tests de sauvegarde */
    private String nomFichierTest = "donnees_test_sd.ser";

    /**
     * Initialisation avant chaque test.
     * <p>
     * Crée un nouveau menu et un service de sauvegarde associé pour garantir
     * l'indépendance des tests.
     */
    @BeforeEach
    public void setUp() {
        menu = new Menu();
        service = new ServiceSauvegarde(menu);
    }

    /**
     * Vérifie que la sauvegarde et le chargement fonctionnent correctement.
     * <p>
     * Ce test effectue les opérations suivantes :
     * <ul>
     *   <li>ajoute un utilisateur au menu</li>
     *   <li>sauvegarde les données dans un fichier</li>
     *   <li>charge les données dans un nouveau menu</li>
     *   <li>vérifie que l'utilisateur sauvegardé est bien retrouvé</li>
     *   <li>supprime le fichier de test après vérification</li>
     * </ul>
     *
     * @throws Exception si une erreur survient lors de la sauvegarde ou du chargement
     */
    @Test
    public void testSauvegardeEtChargementSD() throws Exception {
        InformationPersonnelle info = new InformationPersonnelle("Diallo", "Safiatou");
        menu.ajouterUtilisateur(new CompteClient("sd@ubo.fr", "pass", info));

        service.sauvegarderDonnees(nomFichierTest);

        Menu menuVide = new Menu();
        ServiceSauvegarde serviceLecture = new ServiceSauvegarde(menuVide);
        serviceLecture.chargerDonnees(nomFichierTest);

        assertNotNull(menuVide.trouverUtilisateur("sd@ubo.fr"),
                "Le client sauvegardé par SD doit être retrouvé après chargement");

        // Nettoyage du fichier de test
        new File(nomFichierTest).delete();
    }
}
