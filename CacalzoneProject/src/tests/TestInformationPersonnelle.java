package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.InformationPersonnelle;

/**
 * Classe de test pour la classe InformationPersonnelle.
 * 
 * <p>Vérifie le comportement des constructeurs, des accesseurs,
 * des règles de validation dans les setters (âge positif, adresse non null)
 * et de l'égalité entre objets.
 * </p>
 */
class TestInformationPersonnelle {

  private InformationPersonnelle personne;

  @BeforeEach
  void setUp() {
    // Initialisation d'une personne de base pour les tests de setters
    personne = new InformationPersonnelle("Turing", "Alan", "Londres", 41);
  }

  @Test
  void testConstructeurBasique() {
    InformationPersonnelle p = new InformationPersonnelle("Curie", "Marie");

    assertEquals("Curie", p.getNom());
    assertEquals("Marie", p.getPrenom());
    // Vérification des valeurs par défaut définies dans le code
    assertEquals("", p.getAdresse(), "L'adresse par défaut doit être une chaîne vide");
    assertEquals(0, p.getAge(), "L'âge par défaut doit être 0");
  }

  @Test
  void testConstructeurComplet() {
    InformationPersonnelle p = new InformationPersonnelle("Lovelace", "Ada", "Londres", 36);

    assertEquals("Lovelace", p.getNom());
    assertEquals("Ada", p.getPrenom());
    assertEquals("Londres", p.getAdresse());
    assertEquals(36, p.getAge());
  }

  @Test
  void testSetAgeValide() {
    personne.setAge(50);
    assertEquals(50, personne.getAge(), "L'âge doit être mis à jour car 50 > 0");
  }

  @Test
  void testSetAgeInvalide() {
    int ancienAge = personne.getAge();
    
    personne.setAge(-10);
    assertEquals(ancienAge, personne.getAge(), "Age ne doit pas changer pour une valeur négative");

    personne.setAge(0);
    assertEquals(ancienAge, personne.getAge(), "L'âge ne doit pas changer pour 0");
  }

  @Test
  void testSetAdresseValide() {
    personne.setAdresse("Manchester");
    assertEquals("Manchester", personne.getAdresse(), "L'adresse doit être mise à jour");
  }

  @Test
  void testSetAdresseInvalide() {
    String ancienneAdresse = personne.getAdresse();
    
    personne.setAdresse(null);
    assertEquals(ancienneAdresse, personne.getAdresse(), "Adresse doit pas être MAJ avec null");
    assertNotNull(personne.getAdresse());
  }

  @Test
  void testEquals() {
    InformationPersonnelle p1 = new InformationPersonnelle("Turing", "Alan", "Londres", 41);
    InformationPersonnelle p2 = new InformationPersonnelle("Turing", "Alan", "Londres", 41);

    assertTrue(p1.equals(p2), "Deux objets avec les mêmes attributs doivent être égaux");
    assertTrue(p2.equals(p1), "La relation d'égalité doit être symétrique");
    assertEquals(p1.hashCode(), p2.hashCode(), "hashCodes doivent être pareil pour deux objets");
  }

  @Test
  void testNotEquals() {
    InformationPersonnelle p1 = new InformationPersonnelle("Turing", "Alan", "Londres", 41);
    InformationPersonnelle p2 = new InformationPersonnelle("Turing", "Alan", "Paris", 41);
    InformationPersonnelle p3 = new InformationPersonnelle("Turing", "Alan", "Londres", 42);

    assertFalse(p1.equals(p2), "Les objets ne doivent pas être égaux si l'adresse diffère");
    assertFalse(p1.equals(p3), "Les objets ne doivent pas être égaux si l'âge diffère");
    assertFalse(p1.equals(null), "Un objet ne doit pas être égal à null");
    assertFalse(p1.equals("Une chaine"), "objet doit pas être égal à un objet d'une autre classe");
  }
}