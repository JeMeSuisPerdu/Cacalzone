package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import pizzas.*;

/**
 * Classe de tests unitaires pour la gestion des comptes utilisateurs.
 * <p>
 * Ces tests vérifient que le système distingue correctement les droits
 * des clients et des pizzaiolos, et que les historiques sont bien initialisés.
 * <p>
 * L'objectif est de garantir que les profils utilisateurs sont correctement
 * configurés dès leur création et que les droits d'accès sont respectés.
 * 
 */
public class TestComptes {

    /**
     * Vérifie que le CompteClient est correctement initialisé.
     * <p>
     * Un client ne doit pas avoir de droits pizzaiolo et son historique
     * doit être prêt à recevoir des commandes.
     */
    @Test
    public void testProfilClientSD() {
        InformationPersonnelle info = new InformationPersonnelle("Diallo", "Safiatou");
        CompteClient client = new CompteClient("sd@client.fr", "pass123", info);

        // Vérification des droits
        assertFalse(client.estPizzaiolo(), "Un client ne doit pas être reconnu comme pizzaiolo");

        // Vérification de l'historique
        assertNotNull(client.getHistoriqueCommandes(), "L'historique doit être créé à l'initialisation");
        assertTrue(client.getHistoriqueCommandes().isEmpty(), "L'historique doit être vide au départ");
    }

    /**
     * Vérifie que le ComptePizzaiolo possède les privilèges nécessaires.
     * <p>
     * Ce compte doit retourner {@code true} pour la méthode {@code estPizzaiolo()}.
     */
    @Test
    public void testProfilPizzaioloSD() {
        InformationPersonnelle info = new InformationPersonnelle("Diallo", "Safiatou");
        ComptePizzaiolo pro = new ComptePizzaiolo("pizzaiolo@ubo.fr", "chef2024", info);

        // Vérification des droits
        assertTrue(pro.estPizzaiolo(), "Le compte pizzaiolo doit avoir les droits d'accès pro");
    }
}
