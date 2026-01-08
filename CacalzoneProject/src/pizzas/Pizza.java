package pizzas;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Classe Pizza représentant le produit principal de la pizzeria.
 *
 * <p>Cette classe regroupe toutes les caractéristiques d'une pizza : son identité
 * (nom, type, photo), sa composition (liste d'ingrédients), ses données
 * financières (prix) et les retours clients (évaluations). Elle intègre
 * également des méthodes utilitaires pour garantir la cohérence des données
 * (calcul de prix minimal, validation des ingrédients).</p>
 */
public class Pizza implements Serializable {

  /**
   * Identifiant de sérialisation pour la persistance des données.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Le nom commercial de la pizza (ex: "Reine", "Margherita").
   */
  private String nom;

  /**
   * La catégorie de la pizza (ex: VEGETARIENNE, VIANDE). Ce type détermine les
   * restrictions sur les ingrédients ajoutables.
   */
  private TypePizza typePizza;

  /**
   * L'ensemble des ingrédients composant la pizza. L'utilisation d'un Set
   * garantit l'unicité des ingrédients (pas de doublons).
   */
  private Set<Ingredient> ingredients = new HashSet<>();

  /**
   * L'ensemble des évaluations laissées par les clients pour cette pizza.
   */
  private Set<Evaluation> evaluations = new HashSet<>();

  /**
   * Le prix de vente défini par le pizzaïolo. S'il est null, le prix de vente
   * sera égal au prix minimal calculé.
   */
  private Double prix;

  /**
   * Le chemin d'accès (URI) vers l'image illustrant la pizza. Initialisé à une
   * chaîne vide par défaut.
   */
  private String photo = "";

  /**
   * Constructeur principal de la classe Pizza. Initialise une pizza avec les
   * listes d'ingrédients et d'évaluations vides.
   *
   * @param nom Le nom donné à la pizza.
   * @param typePizza Le type de la pizza (base de restrictions).
   */
  public Pizza(String nom, TypePizza typePizza) {
    super();
    this.nom = nom;
    this.typePizza = typePizza;
  }

  /**
   * Récupère le nom de la pizza.
   *
   * @return Le nom sous forme de chaîne de caractères.
   */
  public String getNom() {
    return nom;
  }

  /**
   * Modifie le nom de la pizza.
   * Si le nom est nul ou vide (après avoir enlevé les espaces),
   * on lance l'exception IllegalArgumentException.
   *
   * @param nom Le nouveau nom à attribuer.
   */
  public void setNom(String nom) {
    if (nom == null || nom.trim().isEmpty()) {
      throw new IllegalArgumentException(
          "Le nom de la pizza ne peut pas être vide");
    }
    this.nom = nom;
  }

  /**
   * Récupère le type (catégorie) de la pizza.
   *
   * @return La valeur de l'énumération TypePizza.
   */
  public TypePizza getTypePizza() {
    return typePizza;
  }

  /**
   * Modifie le type de la pizza. Attention : cela peut rendre certains
   * ingrédients actuels invalides si le nouveau type impose de nouvelles
   * restrictions.
   *
   * @param typePizza Le nouveau type de pizza.
   */
  public void setTypePizza(TypePizza typePizza) {
    this.typePizza = typePizza;
  }

  /**
   * Accède à l'ensemble des ingrédients qui composent la pizza.
   *
   * @return Le Set des ingrédients.
   */
  public Set<Ingredient> getIngredients() {
    return ingredients;
  }

  /**
   * Remplace la liste complète des ingrédients.
   *
   * @param ingredients Le nouvel ensemble d'ingrédients.
   */
  public void setIngredients(Set<Ingredient> ingredients) {
    this.ingredients = ingredients;
  }

  /**
   * Accède à l'ensemble des évaluations et notes associées à cette pizza.
   *
   * @return Le Set des évaluations.
   */
  public Set<Evaluation> getEvaluations() {
    return evaluations;
  }

  /**
   * Récupère le prix de vente effectif de la pizza.
   *
   * <p>Si un prix manuel a été défini (attribut non null), c'est celui-ci qui est
   * retourné. Sinon, la méthode renvoie le résultat de
   * {@link #getPrixMinimalPizza()}.</p>
   *
   * @return Le prix de vente en euros.
   */
  public Double getPrix() {
    return (this.prix != null) ? this.prix : this.getPrixMinimalPizza();
  }

  /**
   * Tente de définir un nouveau prix de vente pour la pizza.
   *
   * <p>Une vérification est effectuée : le nouveau prix doit être supérieur ou égal
   * au prix minimal (coût de revient + marge). Si le prix est trop bas, la
   * modification est refusée.</p>
   *
   * @param prix Le prix souhaité.
   * @return true si le prix a été mis à jour, false s'il est inférieur au minimum autorisé.
   */
  public boolean setPrix(Double prix) {
    if (prix >= this.getPrixMinimalPizza()) {
      this.prix = prix;
      return true;
    } else {
      return false;
    }
  }

  /**
   * Récupère le chemin vers le fichier image de la pizza.
   *
   * @return L'URI de la photo sous forme de String.
   */
  public String getPhoto() {
    return photo;
  }

  /**
   * Définit l'image associée à la pizza.
   *
   * @param photo Le chemin ou l'URI du fichier image.
   */
  public void setPhoto(String photo) {
    this.photo = photo;
  }

  /**
   * Calcule le prix de revient minimal de la pizza selon les règles métier.
   *
   * <p>La formule appliquée est la suivante : 1. Somme des prix unitaires de tous
   * les ingrédients. 2. Ajout d'une marge de 40% (multiplication par 1.4). 3.
   * Arrondi à la dizaine de centimes supérieure (ex: 12.11 devient 12.20).</p>
   *
   * @return Le prix minimal calculé (Double).
   */
  public Double getPrixMinimalPizza() {
    Double sommePrix = 0.0;
    for (Iterator<Ingredient> it = ingredients.iterator(); it.hasNext();) {
      Ingredient i = it.next();
      sommePrix += i.getPrix();
    }
    Double prix40pourcent = (sommePrix * 1.4) * 100;
    Double prixMin = Math.ceil(prix40pourcent / 10.0) * 10.0 / 100;

    return prixMin;
  }

  /**
   * Tente d'ajouter un ingrédient à la pizza en respectant les contraintes.
   *
   * <p>L'ajout n'est effectué que si deux conditions sont réunies : 1. L'ingrédient
   * n'est pas déjà présent dans la pizza (évite les doublons). 2. L'ingrédient
   * n'est pas interdit pour le {@link TypePizza} actuel de cette pizza (ex: pas
   * de viande sur une végétarienne).</p>
   *
   * @param i L'ingrédient à ajouter.
   * @return true si l'ajout a réussi, false si l'ingrédient est rejeté.
   */
  public Boolean ajouterIngredient(Ingredient i) {
    if (!this.ingredients.contains(i)
        && !i.getTypesPizzaInterdits().contains(this.getTypePizza())) {
      this.ingredients.add(i);
      return true;
    }
    return false;
  }
}