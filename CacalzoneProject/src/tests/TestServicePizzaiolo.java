package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.*;
import java.util.List;

/**
 * Classe de tests unitaires pour la classe {@link ServicePizzaiolo}.
 * <p>
 * Ces tests vérifient les fonctionnalités principales liées au service
 * du pizzaiolo, notamment :
 * <ul>
 *   <li>la création de pizzas personnalisées et leur ajout au menu</li>
 *   <li>la récupération des commandes non traitées</li>
 * </ul>
 * <p>
 * L'objectif est de garantir que le service pizzaiolo fonctionne correctement
 * et que les opérations sur le menu et les commandes respectent les règles métier.
 * 
 */
public class TestServicePizzaiolo {

    /** Service pizzaiolo utilisé pour les tests */
    private ServicePizzaiolo service;

    /** Menu associé au service */
    private Menu menu;

    /**
     * Initialisation avant chaque test.
     * <p>
     * Crée un nouveau menu et un nouveau service pizzaiolo
     * pour garantir l'indépendance des tests.
     */
    @BeforeEach
    public void setUp() {
        menu = new Menu();
        service = new ServicePizzaiolo(menu);
    }

    /**
     * Vérifie que la création d'une nouvelle pizza fonctionne correctement.
     * <p>
     * Teste que :
     * <ul>
     *   <li>la pizza est bien créée et non nulle</li>
     *   <li>le nom correspond à celui fourni</li>
     *   <li>la pizza est bien ajoutée au menu</li>
     * </ul>
     */
    @Test
    public void testCreerPizzaSD() {
        String nomPizza = "Speciale Diallo";
        Pizza nouvelle = service.creerPizza(nomPizza, TypePizza.Regionale);

        assertNotNull(nouvelle, "La pizza devrait être créée");
        assertEquals(nomPizza, nouvelle.getNom());

        boolean trouvee = false;
        for (Pizza p : menu.getPizzas()) {
            if (p.getNom().equals(nomPizza)) {
                trouvee = true;
                break;
            }
        }
        assertTrue(trouvee, "La pizza créée par SD doit être ajoutée au menu");
    }

    /**
     * Vérifie que, au départ, aucune commande n'est marquée comme non traitée.
     * <p>
     * Ce test permet de s'assurer que la liste des commandes à traiter
     * est vide lors de l'initialisation du service pizzaiolo.
     */
    @Test
    public void testCommandeNonTraiteesInitialement() {
        List<Commande> aTraiter = service.commandeNonTraitees();

        assertNotNull(aTraiter, "La liste des commandes non traitées ne doit pas être null");
        assertTrue(aTraiter.isEmpty(), "Au début, aucune commande ne doit être en attente");
    }
}
