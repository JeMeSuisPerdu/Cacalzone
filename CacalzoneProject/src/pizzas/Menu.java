package pizzas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Classe centrale de l'application regroupant l'ensemble des données métiers.
 *
 * <p>Le Menu agit comme une base de données volatile (ou persistante via sérialisation).
 * Il centralise les stocks (pizzas, ingrédients), la gestion des comptes
 * utilisateurs (clients et pizzaïolo) ainsi que l'historique global des commandes.
 * C'est cet objet qui est manipulé par les différents services pour accéder
 * à l'état de la pizzeria.</p>
 */
public class Menu implements Serializable {

  /**
   * Identifiant de sérialisation utilisé pour garantir la compatibilité
   * des versions de classe lors de la sauvegarde et du chargement des données.
   */
  private static final long serialVersionUID = 1L;

  /**
   * L'ensemble des pizzas actuellement proposées à la vente.
   * L'utilisation d'un Set garantit qu'il n'y a pas de doublons dans la carte.
   */
  private Set<Pizza> pizzas = new HashSet<>();

  /**
   * L'ensemble des ingrédients disponibles pour la composition des pizzas.
   * Ces ingrédients sont utilisés pour créer de nouvelles recettes.
   */
  private Set<Ingredient> ingredients = new HashSet<>();

  /**
   * La liste complète de tous les utilisateurs enregistrés dans le système.
   * Elle contient à la fois les comptes des clients et celui du pizzaïolo.
   */
  private List<Utilisateur> utilisateurs = new ArrayList<>();

  /**
   * L'historique global de toutes les commandes passées dans la pizzeria.
   * Cette liste permet notamment de calculer les statistiques de vente.
   */
  private List<Commande> commandesGlobales = new ArrayList<>();

  /**
   * Constructeur par défaut de la classe Menu.
   *
   * <p>Initialise toutes les collections de données. Crée également le compte
   * administrateur par défaut pour le pizzaïolo (Email: "chef@pizza.fr",
   * Mot de passe: "admin", Identité: Mario Chef). Ce compte est indispensable
   * pour la première connexion à l'application.</p>
   */
  public Menu() {
    utilisateurs.add(new ComptePizzaiolo("chef@pizza.fr", "admin",
        new InformationPersonnelle("Mario", "Chef")));
  }

  /**
   * Tente d'authentifier un utilisateur avec ses identifiants.
   *
   * <p>Parcourt la liste des utilisateurs enregistrés pour trouver une correspondance.
   * La vérification de l'email n'est pas sensible à la casse (majuscules/minuscules),
   * contrairement au mot de passe qui doit être exact.</p>
   *
   * @param email L'adresse email fournie lors de la tentative de connexion.
   * @param mdp Le mot de passe fourni.
   * @return L'objet {@link Utilisateur} correspondant si l'authentification réussit,
   */
  public Utilisateur authentifier(String email, String mdp) {
    for (Utilisateur u : utilisateurs) {
      if (u.getEmail().equalsIgnoreCase(email)
          && u.getMotDePasse().equals(mdp)) {
        return u;
      }
    }
    return null;
  }

  /**
   * Recherche un utilisateur dans la base à partir de son adresse email.
   *
   * <p>Cette méthode est utile pour vérifier l'existence d'un compte (par exemple
   * lors de l'inscription pour éviter les doublons) ou pour retrouver un profil.
   * La recherche est insensible à la casse.</p>
   *
   * @param email L'adresse email de l'utilisateur recherché.
   * @return L'objet {@link Utilisateur} trouvé, ou {@code null} si aucun compte pour l'email.
   */
  public Utilisateur trouverUtilisateur(String email) {
    for (Utilisateur u : utilisateurs) {
      if (u.getEmail().equalsIgnoreCase(email)) {
        return u;
      }
    }
    return null;
  }

  /**
   * Enregistre un nouvel utilisateur dans la base de données de la pizzeria.
   *
   * <p>L'utilisateur est ajouté à la liste globale, ce qui lui permettra ensuite
   * de se connecter.</p>
   *
   * @param u L'objet {@link Utilisateur} (Client ou Pizzaïolo) à ajouter.
   */
  public void ajouterUtilisateur(Utilisateur u) {
    this.utilisateurs.add(u);
  }

  /**
   * Archive une commande validée dans l'historique global.
   *
   * <p>Cette méthode permet de centraliser toutes les transactions pour
   * les besoins du pizzaïolo (traitement des commandes, statistiques).</p>
   *
   * @param c La commande à ajouter à l'historique.
   */
  public void ajouterCommande(Commande c) {
    this.commandesGlobales.add(c);
  }

  /**
   * Accède à la liste des pizzas disponibles (la carte).
   *
   * @return L'ensemble des pizzas du menu.
   */
  public Set<Pizza> getPizzas() {
    return pizzas;
  }

  /**
   * Accède à la liste des ingrédients disponibles en stock.
   *
   * @return L'ensemble des ingrédients.
   */
  public Set<Ingredient> getIngredients() {
    return ingredients;
  }

  /**
   * Accède à l'historique complet des commandes de la pizzeria.
   *
   * @return La liste de toutes les commandes passées depuis la création du menu.
   */
  public List<Commande> getCommandesGlobales() {
    return commandesGlobales;
  }

  /**
   * Accède à la liste de tous les comptes utilisateurs.
   *
   * @return La liste contenant les clients et le pizzaïolo.
   */
  public List<Utilisateur> getUtilisateurs() {
    return utilisateurs;
  }
}