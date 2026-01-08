package pizzas;

import java.util.Objects;

/**
 * Description des informations personnelles d'une personne : identité, âge et
 * adresse.
 *
 * <p>Cette classe est utilisée pour stocker les données civiles des clients
 * et du pizzaïolo. Certaines informations sont immuables (nom, prénom) tandis
 * que d'autres peuvent évoluer (âge, adresse).</p>
 *
 * @author Eric Cariou
 */
public final class InformationPersonnelle implements java.io.Serializable {

  /**
   * Identifiant de sérialisation.
   */
  private static final long serialVersionUID = 4026408353251835506L;

  /**
   * Le nom de famille de la personne.
   * Cet attribut est immuable et ne peut pas être modifié après la création.
   */
  private final String nom;

  /**
   * Le prénom de la personne.
   * Cet attribut est immuable et ne peut pas être modifié après la création.
   */
  private final String prenom;

  /**
   * L'âge de la personne en années.
   * La valeur 0 est utilisée par convention pour signifier un âge non défini.
   */
  private int age;

  /**
   * L'adresse postale de la personne.
   * Une chaîne vide "" est utilisée pour signifier une adresse non définie.
   */
  private String adresse;

  /**
   * Renvoie le nom de famille de la personne.
   *
   * @return Le nom de la personne.
   */
  public String getNom() {
    return nom;
  }

  /**
   * Renvoie le prénom de la personne.
   *
   * @return Le prénom de la personne.
   */
  public String getPrenom() {
    return prenom;
  }

  /**
   * Renvoie l'âge actuel de la personne.
   *
   * @return L'âge en années.
   */
  public int getAge() {
    return age;
  }

  /**
   * Modifie l'âge de la personne.
   *
   * <p>Une vérification est effectuée : la modification n'est prise en compte
   * que si l'âge fourni est strictement positif. Sinon, l'ancien âge est conservé.</p>
   *
   * @param age Le nouvel âge (doit être supérieur à 0).
   */
  public void setAge(int age) {
    if (age > 0) {
      this.age = age;
    }
  }

  /**
   * Renvoie l'adresse postale de la personne.
   *
   * @return L'adresse sous forme de chaîne de caractères.
   */
  public String getAdresse() {
    return adresse;
  }

  /**
   * Modifie l'adresse de la personne.
   *
   * <p>Une vérification est effectuée : la modification n'est prise en compte
   * que si l'adresse fournie n'est pas nulle.</p>
   *
   * @param adresse La nouvelle adresse (doit être différente de null).
   */
  public void setAdresse(String adresse) {
    if (adresse != null) {
      this.adresse = adresse;
    }
  }

  /**
   * Crée une personne avec ses informations obligatoires uniquement.
   *
   * <p>L'adresse est initialisée à une chaîne vide et l'âge à 0 par défaut.</p>
   *
   * @param nom Le nom de famille de la personne.
   * @param prenom Le prénom de la personne.
   */
  public InformationPersonnelle(String nom, String prenom) {
    this(nom, prenom, "", 0);
  }

  /**
   * Crée une personne avec l'ensemble de ses informations.
   *
   * @param nom Le nom de famille de la personne.
   * @param prenom Le prénom de la personne.
   * @param adresse L'adresse postale complète.
   * @param age L'âge de la personne.
   */
  public InformationPersonnelle(String nom, String prenom, String adresse,
      int age) {
    super();
    this.nom = nom;
    this.prenom = prenom;
    this.adresse = adresse;
    this.age = age;
  }

  /**
   * Calcule le code de hachage de l'objet basé sur ses attributs.
   *
   * @return Un entier représentant le hash code.
   */
  @Override
  public int hashCode() {
    return Objects.hash(adresse, age, nom, prenom);
  }

  /**
   * Vérifie l'égalité entre cet objet et un autre objet.
   *
   * <p>Deux instances sont considérées comme égales si elles ont exactement
   * le même nom, prénom, âge et adresse.</p>
   *
   * @param obj L'objet à comparer.
   * @return true si les objets sont égaux, false sinon.
   */
  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    InformationPersonnelle other = (InformationPersonnelle) obj;
    return Objects.equals(adresse, other.adresse) && age == other.age
        && Objects.equals(nom, other.nom)
        && Objects.equals(prenom, other.prenom);
  }

  /**
   * Retourne une représentation textuelle de la personne.
   *
   * @return Une chaîne décrivant la personne (Prénom Nom, âge et adresse).
   */
  @Override
  public String toString() {
    return prenom + " " + nom + " d'age " + age + " ans, habite " + adresse;
  }
}