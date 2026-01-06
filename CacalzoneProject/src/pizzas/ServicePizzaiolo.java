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
 * Permet la gestion du menu (création de pizzas, d'ingrédients),
 * le traitement des commandes et l'accès aux statistiques de vente.
 */
public class ServicePizzaiolo implements InterPizzaiolo {

  /** Référence vers le menu centralisant les données. */
  private final Menu menu;

  /**
   * Constructeur du service pizzaiolo.
   *
   * @param menu Le menu contenant les données de la pizzeria.
   */
  public ServicePizzaiolo(Menu menu) {
    this.menu = menu;
  }

  @Override
  public Pizza creerPizza(String nom, TypePizza type) {
    for (Pizza p : menu.getPizzas()) {
      if (p.getNom().equalsIgnoreCase(nom)) {
        return null;
      }
    }
    Pizza nouvelle = new Pizza(nom, type);
    menu.getPizzas().add(nouvelle);
    return nouvelle;
  }

  @Override
  public List<Commande> commandeNonTraitees() {
    List<Commande> atraiter = menu.getCommandesGlobales().stream()
        .filter(c -> c.getEtat() == Commande.Etat.VALIDEE)
        .collect(Collectors.toList());

    atraiter.forEach(Commande::traiter);
    return atraiter;
  }

  @Override
  public int creerIngredient(String n, double p) {
    if (n == null || n.isBlank()) {
      return -1;
    }
    if (p <= 0) {
      return -3;
    }
    menu.getIngredients().add(new Ingredient(n, p));
    return 0;
  }

  @Override
  public Set<Pizza> getPizzas() {
    return menu.getPizzas();
  }

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

  @Override
  public int ajouterIngredientPizza(Pizza pizza, String nomIngredient) {
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
    if (ingredientAtrouver.getTypesPizzaInterdits()
        .contains(pizza.getTypePizza())) {
      return -3;
    }

    pizza.ajouterIngredient(ingredientAtrouver);
    return 0;
  }

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

  @Override
  public double getPrixPizza(Pizza pizza) {
    if (pizza == null || !menu.getPizzas().contains(pizza)) {
      return -1;
    }
    return pizza.getPrix();
  }

  @Override
  public boolean setPrixPizza(Pizza pizza, double prix) {
    if (pizza == null || !menu.getPizzas().contains(pizza)) {
      return false;
    }
    return pizza.setPrix(prix);
  }

  @Override
  public double calculerPrixMinimalPizza(Pizza pizza) {
    if (pizza == null || !menu.getPizzas().contains(pizza)) {
      return -1;
    }
    return pizza.getPrixMinimalPizza();
  }

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

  @Override
  public Map<Pizza, Double> beneficeParPizza() {
    Map<Pizza, Double> benefices = new HashMap<>();

    for (Pizza p : menu.getPizzas()) {
      double beneficeUnitaire = p.getPrix() - p.getPrixMinimalPizza();
      benefices.put(p, beneficeUnitaire);
    }

    return benefices;
  }

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

  @Override
  public double beneficeToutesCommandes() {
    return menu.getCommandesGlobales().stream()
        .filter(c -> c.getEtat() == Commande.Etat.TRAITEE)
        .mapToDouble(this::beneficeCommandes)
        .sum();
  }

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
   * Récupère le menu associé au service.
   *
   * @return L'objet Menu.
   */
  public Menu getMenu() {
    return menu;
  }

  /**
   * Réinitialise les interdictions de types pour un ingrédient donné.
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