package pizzas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service gérant la logique métier réservée au Pizzaïolo.
 *
 * <p>Cette classe offre toutes les fonctionnalités nécessaires à l'administration
 * de la pizzeria : gestion du catalogue (création de pizzas, d'ingrédients,
 * définition des prix), traitement des commandes clients et consultation
 * des statistiques financières et de vente.</p>
 */
public class ServicePizzaiolo implements InterPizzaiolo {

  /**
   * Référence vers le menu centralisant les données de l'application.
   */
  private final Menu menu;

  /**
   * Constructeur du service pizzaiolo.
   *
   * <p>Initialise le service avec une référence vers le menu partagé, permettant
   * d'accéder aux données persistantes.</p>
   *
   * @param menu Le menu contenant les données de la pizzeria.
   */
  public ServicePizzaiolo(Menu menu) {
    this.menu = menu;
  }

  /**
   * Méthode qui permet de récupérer le menu associé au service.
   *
   * @return L'objet Menu géré par ce service.
   */
  public Menu getMenu() {
    return menu;
  }

  /**
   * Crée une nouvelle pizza et l'ajoute au menu.
   *
   * <p>Cette méthode vérifie d'abord que le nom n'est pas vide et qu'il n'existe
   * pas déjà une pizza portant ce nom (la vérification est insensible à la casse).
   * Si les conditions sont réunies, la pizza est créée et stockée.</p>
   *
   * @param nom Le nom de la nouvelle pizza.
   * @param type Le type de la pizza (VIANDE, VEGETARIENNE, REGIONALE).
   * @return L'objet Pizza créé si succès, null si le nom est invalide ou existe déjà.
   */
  @Override
  public Pizza creerPizza(String nom, TypePizza type) {
    if (nom == null || nom.isBlank()
        || menu.getPizzas().stream()
            .anyMatch(e -> e.getNom().equalsIgnoreCase(nom))) {
      return null;
    }

    Pizza nouvelle = new Pizza(nom, type);
    menu.getPizzas().add(nouvelle);
    return nouvelle;
  }

  /**
   * Récupère et traite les commandes en attente de préparation.
   *
   * <p>Cette méthode sélectionne toutes les commandes ayant l'état {@code VALIDEE},
   * les passe à l'état {@code TRAITEE} (simulant l'action du cuisinier)
   * et renvoie la liste de ces commandes.</p>
   *
   * @return La liste des commandes qui viennent d'être traitées.
   */
  @Override
  public List<Commande> commandeNonTraitees() {
    List<Commande> atraiter = menu.getCommandesGlobales().stream()
        .filter(c -> c.getEtat() == Commande.Etat.VALIDEE)
        .collect(Collectors.toList());

    atraiter.forEach(Commande::traiter);
    return atraiter;
  }

  /**
   * Crée un nouvel ingrédient et l'ajoute au stock.
   *
   * <p>Effectue plusieurs vérifications : le nom ne doit pas être vide, le prix
   * doit être positif et l'ingrédient ne doit pas déjà exister (doublon de nom).</p>
   *
   * @param n Le nom de l'ingrédient.
   * @param p Le prix unitaire de l'ingrédient.
   * @return 0 si succès, -1 si nom invalide, -2 si ingrédient existe déjà,-3 si prix négatif.
   */
  @Override
  public int creerIngredient(String n, double p) {
    if (n == null || n.isBlank()) {
      return -1;
    }
    if (p <= 0) {
      return -3;
    }

    if (menu.getIngredients().stream()
        .anyMatch(e -> e.getNom().equalsIgnoreCase(n))) {
      return -2;
    }

    menu.getIngredients().add(new Ingredient(n, p));
    return 0;
  }

  /**
   * Récupère l'ensemble des pizzas disponibles dans le menu.
   *
   * @return Un Set contenant toutes les pizzas.
   */
  @Override
  public Set<Pizza> getPizzas() {
    return menu.getPizzas();
  }

  /**
   * Modifie le prix unitaire d'un ingrédient existant.
   *
   * <p>Recherche l'ingrédient par son nom et met à jour son prix.
   * Cela impactera le coût de revient des pizzas qui l'utilisent.</p>
   *
   * @param n Le nom de l'ingrédient à modifier.
   * @param p Le nouveau prix (doit être positif).
   * @return 0 si succès, -1 si nom invalide, -2 si prix invalide, -3 si ingrédient non trouvé.
   */
  @Override
  public int changerPrixIngredient(String n, double p) {
    if (n == null || n.isBlank()) {
      return -1;
    }
    if (p <= 0) {
      return -2;
    }
    for (Ingredient ing : menu.getIngredients()) {
      if (ing.getNom().equalsIgnoreCase(n)) {
        ing.setPrix(p);
        return 0;
      }
    }
    return -3;
  }

  /**
   * Ajoute une restriction d'utilisation pour un ingrédient.
   *
   * <p>L'ingrédient sera marqué comme interdit pour le type de pizza spécifié.
   * Par exemple, on peut interdire le "Jambon" pour le type "VEGETARIENNE".</p>
   *
   * @param nomIngredient Le nom de l'ingrédient concerné.
   * @param type Le type de pizza sur lequel l'ingrédient est interdit.
   * @return true si l'opération réussit, false si les paramètres sont invalides ou introuvable.
   */
  @Override
  public boolean interdireIngredient(String nomIngredient, TypePizza type) {
    if (nomIngredient == null || nomIngredient.isBlank() || type == null) {
      return false;
    }

    for (Ingredient ing : menu.getIngredients()) {
      if (ing.getNom().equalsIgnoreCase(nomIngredient)) {
        ing.addTypePizzaInterdit(type);
        return true;
      }
    }

    return false;
  }

  /**
   * Autorise un type de pizza auparavant interdit pour un ingrédient.
   *
   * <p>Supprime la restriction existante pour le couple ingrédient/type.</p>
   *
   * @param nomIngredient Le nom de l'ingrédient concerné.
   * @param type Le type de pizza à autoriser.
   * @return true si ce type a été autorisé pour l'ingrédient (restriction a été levée),false sinon.
   */
  public boolean autoriserTypePizza(String nomIngredient, TypePizza type) {
    if (nomIngredient == null || nomIngredient.isBlank() || type == null) {
      return false;
    }

    for (Ingredient ing : menu.getIngredients()) {
      if (ing.getNom().equalsIgnoreCase(nomIngredient)) {
        for (TypePizza tp : ing.getTypesPizzaInterdits()) {
          if (tp.equals(type)) {
            ing.removeTypePizzaInterdit(type);
            return true;
          }
        }
        return false;
      }
    }

    return false;
  }

  /**
   * Ajoute un ingrédient à la recette d'une pizza.
   *
   * <p>Vérifie que la pizza et l'ingrédient existent, et s'assure que l'ingrédient
   * n'est pas interdit pour le type de la pizza cible.</p>
   *
   * @param pizza La pizza à modifier.
   * @param nomIngredient Le nom de l'ingrédient à ajouter.
   * @return 0 si succès, -1 si pizza invalide, -2 si ingrédient invalide,-3 si ingrédient interdit.
   */
  @Override
  public int ajouterIngredientPizza(Pizza pizza, String nomIngredient) {
    if (pizza == null || !menu.getPizzas().contains(pizza)) {
      return -1;
    }
    if (nomIngredient == null || nomIngredient.isBlank()) {
      return -2;
    }

    for (Ingredient ing : menu.getIngredients()) {
      if (ing.getNom().equalsIgnoreCase(nomIngredient)) {
        if (ing.getTypesPizzaInterdits().contains(pizza.getTypePizza())) {
          return -3;
        }

        pizza.ajouterIngredient(ing);
        return 0;
      }
    }
    return -2;
  }

  /**
   * Retire un ingrédient de la recette d'une pizza.
   *
   * @param pizza La pizza à modifier.
   * @param nomIngredient Le nom de l'ingrédient à retirer.
   * @return 0 si succès, -1 si pizza invalide, -2 si nom invalide, -3 si l'ingrédient pas présent.
   */
  @Override
  public int retirerIngredientPizza(Pizza pizza, String nomIngredient) {
    if (pizza == null || !menu.getPizzas().contains(pizza)) {
      return -1;
    }

    if (nomIngredient == null || nomIngredient.isBlank()) {
      return -2;
    }

    Ingredient ingredientAtrouver = null;
    for (Ingredient ing : menu.getIngredients()) {
      if (ing.getNom().equalsIgnoreCase(nomIngredient)) {
        ingredientAtrouver = ing;
        break;
      }
    }

    if (ingredientAtrouver == null) {
      return -2;
    }

    if (!pizza.getIngredients().contains(ingredientAtrouver)) {
      return -3;
    }

    pizza.getIngredients().remove(ingredientAtrouver);

    return 0;
  }

  /**
   * Vérifie la conformité des ingrédients d'une pizza par rapport à son type.
   *
   * <p>Cette méthode est utile si le type d'une pizza a changé après sa création.
   * Elle liste tous les ingrédients présents dans la pizza mais qui sont théoriquement
   * interdits pour son type actuel.</p>
   *
   * @param pizza La pizza à vérifier.
   * @return Un ensemble contenant les noms des ingrédients conflictuels, ou null si inexistant.
   */
  @Override
  public Set<String> verifierIngredientsPizza(Pizza pizza) {
    if (pizza == null || !menu.getPizzas().contains(pizza)) {
      return null;
    }

    Set<String> ingredientsInterdits = new HashSet<>();
    TypePizza typeActuel = pizza.getTypePizza();

    for (Ingredient ing : pizza.getIngredients()) {
      if (ing.getTypesPizzaInterdits().contains(typeActuel)) {
        ingredientsInterdits.add(ing.getNom());
      }
    }

    return ingredientsInterdits;
  }

  /**
   * Associe une image à une pizza.
   *
   * @param pizza La pizza concernée.
   * @param file Le chemin ou l'URI du fichier image.
   * @return true si l'ajout a réussi, false si les paramètres sont invalides.
   * @throws IOException Si une erreur survient lors de l'accès au fichier.
   */
  @Override
  public boolean ajouterPhoto(Pizza pizza, String file) throws IOException {
    if (pizza == null || !menu.getPizzas().contains(pizza)) {
      return false;
    }

    if (file == null || file.isBlank()) {
      return false;
    }

    pizza.setPhoto(file);
    return true;
  }

  /**
   * Récupère le prix de vente actuel d'une pizza.
   *
   * @param pizza La pizza consultée.
   * @return Le prix de la pizza, ou -1 si la pizza est nulle ou inconnue.
   */
  @Override
  public double getPrixPizza(Pizza pizza) {
    if (pizza == null || !menu.getPizzas().contains(pizza)) {
      return -1;
    }
    return pizza.getPrix();
  }

  /**
   * Définit manuellement le prix de vente d'une pizza.
   *
   * <p>Le nouveau prix doit respecter la contrainte de rentabilité (supérieur ou égal
   * au prix minimal calculé).</p>
   *
   * @param pizza La pizza à modifier.
   * @param prix Le nouveau prix de vente.
   * @return true si le prix a été accepté, false sinon.
   */
  @Override
  public boolean setPrixPizza(Pizza pizza, double prix) {
    if (pizza == null || !menu.getPizzas().contains(pizza)) {
      return false;
    }
    return pizza.setPrix(prix);
  }

  /**
   * Calcule le prix de revient minimal d'une pizza.
   *
   * <p>Se base sur le coût des ingrédients et la marge obligatoire.</p>
   *
   * @param pizza La pizza concernée.
   * @return Le prix minimal, ou -1 si la pizza est invalide.
   */
  @Override
  public double calculerPrixMinimalPizza(Pizza pizza) {
    if (pizza == null || !menu.getPizzas().contains(pizza)) {
      return -1;
    }
    return pizza.getPrixMinimalPizza();
  }

  /**
   * Récupère la liste de tous les clients enregistrés.
   *
   * <p>Filtre la liste globale des utilisateurs pour ne garder que ceux qui
   * ne sont pas pizzaïolo.</p>
   *
   * @return Un ensemble d'informations personnelles des clients.
   */
  @Override
  public Set<InformationPersonnelle> ensembleClients() {
    Set<InformationPersonnelle> clients = new HashSet<>();
    for (Utilisateur u : menu.getUtilisateurs()) {
      if (!u.estPizzaiolo()) {
        clients.add(u.getInfos());
      }
    }
    return clients;
  }

  /**
   * Récupère la liste de toutes les commandes déjà traitées.
   *
   * @return Une liste de commandes ayant l'état {@code TRAITEE}.
   */
  @Override
  public List<Commande> commandesDejaTraitees() {
    List<Commande> traitees = new ArrayList<>();
    for (Commande c : menu.getCommandesGlobales()) {
      if (c.getEtat() == Commande.Etat.TRAITEE) {
        traitees.add(c);
      }
    }
    return traitees;
  }

  /**
   * Récupère l'historique des commandes traitées pour un client spécifique.
   *
   * @param client Les informations du client recherché.
   * @return Une liste de commandes traitées appartenant à ce client.
   */
  @Override
  public List<Commande> commandesTraiteesClient(InformationPersonnelle client) {
    if (client == null) {
      return null;
    }

    List<Commande> resultat = new ArrayList<>();
    for (Commande c : menu.getCommandesGlobales()) {
      if (c.getEtat() == Commande.Etat.TRAITEE
          && c.getClient().getInfos().equals(client)) {
        resultat.add(c);
      }
    }
    return resultat;
  }

  /**
   * Calcule le bénéfice unitaire réalisé sur chaque pizza du menu.
   *
   * <p>Le bénéfice est la différence entre le prix de vente et le prix minimal
   * (coût de revient + marge).</p>
   *
   * @return Une map associant chaque pizza à son bénéfice unitaire.
   */
  @Override
  public Map<Pizza, Double> beneficeParPizza() {
    Map<Pizza, Double> benefices = new HashMap<>();

    for (Pizza p : menu.getPizzas()) {
      double beneficeUnitaire = p.getPrix() - p.getPrixMinimalPizza();
      benefices.put(p, beneficeUnitaire);
    }

    return benefices;
  }

  /**
   * Calcule le bénéfice total réalisé sur une commande spécifique.
   *
   * <p>Somme les bénéfices unitaires de chaque pizza multipliés par leur quantité.</p>
   *
   * @param c La commande à analyser.
   * @return Le bénéfice total de la commande, ou -1 si la commande est nulle.
   */
  @Override
  public double beneficeCommandes(Commande c) {
    if (c == null) {
      return -1;
    }
    double beneficeTotal = 0;

    for (Map.Entry<Pizza, Integer> entree : c.getPizzas().entrySet()) {
      Pizza p = entree.getKey();
      int quantite = entree.getValue();
      double beneficeUnitaire = p.getPrix() - p.getPrixMinimalPizza();
      beneficeTotal += (beneficeUnitaire * quantite);
    }
    return beneficeTotal;
  }

  /**
   * Calcule le bénéfice total réalisé sur l'ensemble des commandes traitées.
   *
   * @return La somme des bénéfices de toutes les commandes traitées.
   */
  @Override
  public double beneficeToutesCommandes() {
    return menu.getCommandesGlobales().stream()
        .filter(c -> c.getEtat() == Commande.Etat.TRAITEE)
        .mapToDouble(this::beneficeCommandes)
        .sum();
  }

  /**
   * Calcule le nombre total de pizzas commandées par chaque client.
   *
   * <p>Ne prend en compte que les commandes traitées.</p>
   *
   * @return Une map associant chaque client (InformationPersonnelle) au nombre de pizzas achetées.
   */
  @Override
  public Map<InformationPersonnelle, Integer> nombrePizzasCommandeesParClient() {
    Map<InformationPersonnelle, Integer> statsClient = new HashMap<>();

    for (Commande cmd : menu.getCommandesGlobales()) {
      if (cmd.getEtat() == Commande.Etat.TRAITEE) {
        InformationPersonnelle infos = cmd.getClient().getInfos();
        int nbPizzasDansCommande = cmd.getPizzas().values().stream()
            .mapToInt(Integer::intValue)
            .sum();
        int ancienTotal = statsClient.getOrDefault(infos, 0);
        statsClient.put(infos, ancienTotal + nbPizzasDansCommande);
      }
    }

    return statsClient;
  }

  /**
   * Calcule le bénéfice total généré par chaque client.
   *
   * <p>Permet d'identifier les clients les plus rentables.</p>
   *
   * @return Une map associant chaque client à la somme des bénéfices de ses commandes traitées.
   */
  @Override
  public Map<InformationPersonnelle, Double> beneficeParClient() {
    Map<InformationPersonnelle, Double> benefClients = new HashMap<>();
    for (Commande cmd : menu.getCommandesGlobales()) {
      if (cmd.getEtat() == Commande.Etat.TRAITEE) {
        InformationPersonnelle infos = cmd.getClient().getInfos();
        double beneficeCommande = beneficeCommandes(cmd);
        double cumulActuel = benefClients.getOrDefault(infos, 0.0);
        benefClients.put(infos, cumulActuel + beneficeCommande);
      }
    }
    return benefClients;
  }

  /**
   * Compte combien de fois une pizza spécifique a été commandée.
   *
   * @param p La pizza concernée.
   * @return Le nombre total d'unités vendues dans les commandes traités,ou -1 si la pizza inconnue.
   */
  @Override
  public int nombrePizzasCommandees(Pizza p) {
    if (p == null || !menu.getPizzas().contains(p)) {
      return -1;
    }
    int nbPizzasCmd = 0;
    for (Commande cmd : menu.getCommandesGlobales()) {
      if (cmd.getEtat() == Commande.Etat.TRAITEE && cmd.getPizzas().containsKey(p)) {
        nbPizzasCmd += cmd.getPizzas().get(p);
      }
    }
    return nbPizzasCmd;
  }

  /**
   * Établit un classement des pizzas par popularité.
   *
   * @return Une liste de pizzas triée par ordre décroissant du nombre de ventes.
   */
  @Override
  public List<Pizza> classementPizzasParNombreCommandes() {
    Map<Pizza, Integer> ventesParPizza = new HashMap<>();
    for (Commande cmd : menu.getCommandesGlobales()) {
      if (cmd.getEtat() == Commande.Etat.TRAITEE) {
        for (Map.Entry<Pizza, Integer> entree : cmd.getPizzas().entrySet()) {
          Pizza p = entree.getKey();
          int quantite = entree.getValue();
          int cumul = ventesParPizza.getOrDefault(p, 0);
          ventesParPizza.put(p, cumul + quantite);
        }
      }
    }
    List<Pizza> classement = new ArrayList<>(ventesParPizza.keySet());
    classement.sort((p1, p2) -> ventesParPizza.get(p2)
        .compareTo(ventesParPizza.get(p1)));

    return classement;
  }

  /**
   * Réinitialise toutes les restrictions pour un ingrédient donné.
   *
   * <p>L'ingrédient redevient autorisé pour tous les types de pizzas.</p>
   *
   * @param nomIngredient Le nom de l'ingrédient à réinitialiser.
   */
  public void reinitialiserInterdictionsIngredient(String nomIngredient) {
    for (Ingredient ing : menu.getIngredients()) {
      if (ing.getNom().equalsIgnoreCase(nomIngredient)) {
        ing.getTypesPizzaInterdits().clear();
        break;
      }
    }
  }
}