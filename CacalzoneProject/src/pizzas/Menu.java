package pizzas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Le Menu centralise toutes les données de la pizzeria.
 */
public class Menu implements Serializable {

  /**
   * Identifiant de sérialisation.
   */
  private static final long serialVersionUID = 1L;

  /** Ensemble des pizzas proposées au menu. */
  private Set<Pizza> pizzas = new HashSet<>();

  /** Ensemble des ingrédients disponibles. */
  private Set<Ingredient> ingredients = new HashSet<>();

  /** Liste de tous les utilisateurs (clients et pizzaïolo). */
  private List<Utilisateur> utilisateurs = new ArrayList<>();

  /** Historique global de toutes les commandes passées. */
  private List<Commande> commandesGlobales = new ArrayList<>();

  /**
   * Constructeur du menu.
   * Initialise le compte par défaut du pizzaïolo.
   */
  public Menu() {
    utilisateurs.add(new ComptePizzaiolo("chef@pizza.fr", "admin",
        new InformationPersonnelle("Mario", "Chef")));
  }

  /**
   * Authentifie un utilisateur à partir de ses identifiants.
   *
   * @param email L'adresse email de l'utilisateur.
   * @param mdp Le mot de passe de l'utilisateur.
   * @return L'objet Utilisateur si authentifié, null sinon.
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
   * Recherche un utilisateur par son email.
   *
   * @param email L'email à rechercher.
   * @return L'utilisateur correspondant ou null.
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
   * Ajoute un nouvel utilisateur à la liste globale.
   *
   * @param u L'utilisateur à ajouter.
   */
  public void ajouterUtilisateur(Utilisateur u) {
    this.utilisateurs.add(u);
  }

  /**
   * Enregistre une commande dans la liste globale.
   *
   * @param c La commande à enregistrer.
   */
  public void ajouterCommande(Commande c) {
    this.commandesGlobales.add(c);
  }

  /**
   * Récupère l'ensemble des pizzas du menu.
   *
   * @return Le Set des pizzas.
   */
  public Set<Pizza> getPizzas() {
    return pizzas;
  }

  /**
   * Récupère l'ensemble des ingrédients.
   *
   * @return Le Set des ingrédients.
   */
  public Set<Ingredient> getIngredients() {
    return ingredients;
  }

  /**
   * Récupère la liste de toutes les commandes.
   *
   * @return La liste des commandes globales.
   */
  public List<Commande> getCommandesGlobales() {
    return commandesGlobales;
  }

  /**
   * Récupère la liste de tous les utilisateurs enregistrés.
   *
   * @return La liste des utilisateurs.
   */
  public List<Utilisateur> getUtilisateurs() {
    return utilisateurs;
  }
}