package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.InformationPersonnelle;

/**
 * Classe de tests unitaires pour la classe {@link InformationPersonnelle}.
 * <p>
 * Ces tests vérifient le bon fonctionnement de la gestion des informations personnelles
 * des clients, notamment :
 * <ul>
 *   <li>la création correcte d'un profil avec ou sans informations complètes</li>
 *   <li>la protection contre les valeurs invalides pour l'âge ou l'adresse</li>
 *   <li>la mise à jour sécurisée des attributs</li>
 * </ul>
 * <p>
 * L'objectif est de garantir que les objets {@link InformationPersonnelle} restent
 * fiables et robustes face aux saisies correctes et incorrectes.
 * 
 * @author Safiatou
 */
public class TestInformationPersonnelle {

    /** Profil de test avec données de base */
    private InformationPersonnelle infoBasique;

    /** Profil de test complet pour tester les mises à jour */
    private InformationPersonnelle infoComplete;

    /**
     * Initialisation des profils avant chaque test.
     * <p>
     * Crée un profil basique et un profil complet avec âge et adresse
     * afin de tester différents scénarios de modification.
     */
    @BeforeEach
    public void setUp() {
        // Profil de base
        infoBasique = new InformationPersonnelle("Diallo", "Safiatou");

        // Profil complet
        infoComplete = new InformationPersonnelle("Diallo", "Safiatou");
        infoComplete.setAge(20);
        infoComplete.setAdresse("Avenue de l'UBO, Brest");
    }

    /**
     * Vérifie que l'adresse n'est jamais null.
     * <p>
     * Ceci permet d'éviter les erreurs de type NullPointerException
     * dans le reste du système, même si aucune adresse n'a été fournie
     * à l'initialisation.
     */
    @Test
    public void testAdresseNonNull() {
        assertNotNull(infoBasique.getAdresse(), "L'adresse ne doit jamais être null, même par défaut");
        assertNotNull(infoComplete.getAdresse());
    }

    /**
     * Vérifie que la modification de l'âge fonctionne correctement.
     * <p>
     * On s'assure que lorsque l'utilisateur saisit un âge valide, celui-ci
     * est correctement enregistré dans l'objet {@link InformationPersonnelle}.
     */
    @Test
    public void testAgeValide() {
        infoBasique.setAge(25);
        assertEquals(25, infoBasique.getAge(), "L'âge devrait être mis à jour correctement");
    }

    /**
     * Vérifie la protection contre un âge négatif.
     * <p>
     * Si une valeur invalide est fournie, le système doit ignorer la modification
     * et conserver l'ancienne valeur.
     */
    @Test
    public void testAgeNegatif() {
        int ageAvant = infoComplete.getAge();
        infoComplete.setAge(-20);

        assertNotEquals(-20, infoComplete.getAge(), "Le système doit rejeter un âge négatif");
        assertEquals(ageAvant, infoComplete.getAge(), "L'âge doit rester à sa valeur initiale (20)");
    }

    /**
     * Vérifie la protection contre une mise à jour d'adresse invalide.
     * <p>
     * Si on tente de passer l'adresse à null, le système doit conserver
     * l'adresse existante pour garantir l'intégrité des données.
     */
    @Test
    public void testSetAdresseInvalide() {
        String adresseAvant = infoComplete.getAdresse();
        infoComplete.setAdresse(null);

        assertNotNull(infoComplete.getAdresse(), "L'adresse ne doit pas devenir null");
        assertEquals(adresseAvant, infoComplete.getAdresse(), "L'adresse doit rester inchangée après une tentative de mise à null");
    }
}
