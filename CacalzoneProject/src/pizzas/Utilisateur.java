package pizzas;

import java.io.Serializable;

/**
 * Classe abstraite définissant le socle commun à tous les types d'utilisateurs
 * de l'application (Clients et Pizzaïolo).
 * 
 * <p>Elle regroupe les informations nécessaires à l'authentification (email,
 * mot de passe) ainsi que les données personnelles.
 * En tant que classe abstraite, elle ne peut pas être instanciée directement ;
 * elle sert de base pour le polymorphisme dans la gestion des comptes.
 * </p>
 */
public abstract class Utilisateur implements Serializable {

  /**
   * Identifiant de sérialisation utilisé pour garantir la compatibilité
   * des versions de classe lors de la sauvegarde et du chargement des données.
   */
  private static final long serialVersionUID = 1L;

  /**
   * L'adresse email unique servant d'identifiant de connexion pour
   * l'utilisateur.
   */
  protected String email;

  /**
   * Le mot de passe utilisé pour l'authentification lors de la connexion.
   * (Note : Stocké en clair dans cette version pédagogique).
   */
  protected String motDePasse;

  /**
   * Objet regroupant les détails personnels de l'utilisateur (Nom, Prénom,
   * Adresse, etc.).
   */
  protected InformationPersonnelle infos;

  /**
   * Constructeur destiné à être appelé par les sous-classes via `super()`.
   * Initialise les attributs fondamentaux d'un compte utilisateur.
   *
   * @param email L'adresse email (doit être unique dans le système).
   * @param motDePasse Le mot de passe de connexion.
   * @param infos Les informations civiles de l'utilisateur.
   */
  public Utilisateur(String email, String motDePasse,
      InformationPersonnelle infos) {
    this.email = email;
    this.motDePasse = motDePasse;
    this.infos = infos;
  }

  /**
   * Fournit l'adresse email de l'utilisateur.
   * Utilisé principalement lors de l'authentification et pour l'affichage.
   *
   * @return L'email de l'utilisateur.
   */
  public String getEmail() {
    return email;
  }

  /**
   * Fournit le mot de passe de l'utilisateur.
   * Utilisé par le service d'authentification pour vérifier la correspondance.
   *
   * @return Le mot de passe.
   */
  public String getMotDePasse() {
    return motDePasse;
  }

  /**
   * Accède aux informations personnelles détaillées de l'utilisateur.
   *
   * @return L'objet InformationPersonnelle associé.
   */
  public InformationPersonnelle getInfos() {
    return infos;
  }

  /**
   * Méthode abstraite permettant de déterminer le rôle de l'utilisateur
   * sans avoir recours systématiquement à l'introspection (`instanceof`).
   * Cette méthode force les sous-classes à déclarer explicitement leur nature.
   *
   * @return true si l'utilisateur possède les privilèges de Pizzaïolo, false si c'est un client.
   */
  public abstract boolean estPizzaiolo();
}