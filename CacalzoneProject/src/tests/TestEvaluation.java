package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import pizzas.Evaluation;

/**
 * Classe de tests unitaires pour la classe {@link Evaluation}.
 * <p>
 * Ces tests vérifient le bon fonctionnement des évaluations laissées par les clients
 * sur les pizzas, notamment :
 * <ul>
 *   <li>la mémorisation correcte des données d'un avis (note, commentaire, auteur)</li>
 *   <li>la gestion des valeurs limites pour la notation</li>
 * </ul>
 * <p>
 * L'objectif est de s'assurer que le système est robuste et fiable face à
 * des saisies correctes et incorrectes.
 * 
 */
public class TestEvaluation {

    /**
     * Vérifie que les données d'un avis (note, commentaire, email du client)
     * sont correctement enregistrées dans l'objet {@link Evaluation}.
     * <p>
     * Cas testé : création d'un avis standard par Safiatou avec la note maximale
     * autorisée et un commentaire positif.
     */
    @Test
    public void testCreationEvaluationSD() {
        // Création d'un avis positif par Safiatou
        Evaluation eval = new Evaluation(5, "Excellente pizza !", "sd@ubo.fr");

        // Vérification que la note est bien enregistrée
        assertEquals(5, eval.getNote(), "La note enregistrée doit être 5");

        // Vérification que le commentaire est bien mémorisé
        assertEquals("Excellente pizza !", eval.getCommentaire(),
                "Le commentaire doit être identique à celui saisi");

        // Vérification que l'email du client est correct
        assertEquals("sd@ubo.fr", eval.getEmailClient(),
                "L'email du client doit être correct");
    }

    /**
     * Test de sécurité sur les valeurs limites de la notation.
     * <p>
     * Vérifie que si un client tente de saisir une note hors échelle
     * (par exemple 10 sur une échelle maximale de 5), le système
     * ramène automatiquement la valeur à la limite supérieure autorisée.
     */
    @Test
    public void testNoteLimites() {
        // Tentative de saisie d'une note trop élevée
        Evaluation evalTropHaute = new Evaluation(10, "Trop bien", "sd@ubo.fr");

        // Vérification que le système bride la note à 5
        assertTrue(evalTropHaute.getNote() <= 5,
                "Le système doit empêcher les notes supérieures à 5");
    }
}
