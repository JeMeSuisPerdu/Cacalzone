package pizzas;

import java.util.List;
import java.util.Set;

/**
 * Classe Client implémentant l'interface InterClient.  
 * Cette classe représente un client de l'application qui peut s'inscrire,
 * se connecter, passer des commandes, appliquer des filtres sur les pizzas
 * et laisser des évaluations.  
 * 
 * En implémentant toutes les méthodes de l'interface, on évite de passer par une classe Service.
 */
public class Client implements InterClient {
  
  // --------------------METHODES IMPLEMENTEES DE INTERCLIENT------------------------

  /**
   * Inscrit un nouveau client avec son email, son mot de passe
   * et ses informations personnelles.
   *
   * @param email l'email du client
   * @param mdp le mot de passe du client
   * @param info informations personnelles du client
   * @return un code d'état indiquant si l'inscription a réussi
   */
  @Override
  public int inscription(String email, String mdp, InformationPersonnelle info) {
    return 0;
  }

  /**
   * Permet au client de se connecter avec son email et son mot de passe.
   *
   * @param email email du client
   * @param mdp mot de passe du client
   * @return true si la connexion réussit, false sinon
   */
  @Override
  public boolean connexion(String email, String mdp) {
    return false;
  }

  /**
   * Déconnecte le client actuellement connecté.
   *
   * @throws NonConnecteException si aucun client n'est connecté
   */
  @Override
  public void deconnexion() throws NonConnecteException {

  }

  /**
   * Permet au client de débuter une nouvelle commande.
   *
   * @return une nouvelle commande associée au client
   * @throws NonConnecteException si le client n'est pas connecté
   */
  @Override
  public Commande debuterCommande() throws NonConnecteException {
    return null;
  }

  /**
   * Ajoute une pizza à une commande existante.
   *
   * @param pizza pizza à ajouter
   * @param nombre quantité à ajouter
   * @param cmd commande dans laquelle ajouter la pizza
   * @throws NonConnecteException si le client n'est pas connecté
   * @throws CommandeException si la commande n'est pas modifiable
   */
  @Override
  public void ajouterPizza(Pizza pizza, int nombre, Commande cmd)
      throws NonConnecteException, CommandeException {
    cmd.ajouterPizza(pizza, nombre);
  }

  /**
   * Valide une commande en cours.
   *
   * @param cmd commande à valider
   * @throws NonConnecteException si le client n'est pas connecté
   * @throws CommandeException si la commande ne peut pas être validée
   */
  @Override
  public void validerCommande(Commande cmd) throws NonConnecteException, CommandeException {

  }

  /**
   * Annule une commande en cours.
   *
   * @param cmd commande à annuler
   * @throws NonConnecteException si le client n'est pas connecté
   * @throws CommandeException si la commande ne peut pas être annulée
   */
  @Override
  public void annulerCommande(Commande cmd) throws NonConnecteException, CommandeException {

  }

  /**
   * Renvoie la liste des commandes du client qui sont encore en cours.
   *
   * @return liste des commandes en cours
   * @throws NonConnecteException si le client n'est pas connecté
   */
  @Override
  public List<Commande> getCommandesEncours() throws NonConnecteException {
    return null;
  }

  /**
   * Renvoie la liste des commandes déjà passées par le client.
   *
   * @return liste des commandes passées
   * @throws NonConnecteException si le client n'est pas connecté
   */
  @Override
  public List<Commande> getCommandePassees() throws NonConnecteException {
    return null;
  }

  /**
   * Renvoie l'ensemble des pizzas disponibles.
   *
   * @return ensemble des pizzas proposées par la plateforme
   */
  @Override
  public Set<Pizza> getPizzas() {
    return null;
  }

  /**
   * Ajoute un filtre basé sur le type de pizza.
   *
   * @param type type de pizza à filtrer
   */
  @Override
  public void ajouterFiltre(TypePizza type) {

  }

  /**
   * Ajoute un filtre basé sur des ingrédients interdits.
   *
   * @param ingredients liste d'ingrédients à filtrer
   */
  @Override
  public void ajouterFiltre(String... ingredients) {

  }

  /**
   * Ajoute un filtre basé sur un prix maximum accepté.
   *
   * @param prixMaximum prix plafond pour filtrer les pizzas
   */
  @Override
  public void ajouterFiltre(double prixMaximum) {

  }

  /**
   * Retourne les pizzas correspondant aux filtres actifs.
   *
   * @return ensemble des pizzas filtrées
   */
  @Override
  public Set<Pizza> selectionPizzaFiltres() {
    return null;
  }

  /**
   * Supprime tous les filtres actifs.
   */
  @Override
  public void supprimerFiltres() {

  }

  /**
   * Renvoie les évaluations associées à une pizza donnée.
   *
   * @param pizza pizza dont on souhaite obtenir les évaluations
   * @return ensemble des évaluations pour cette pizza
   */
  @Override
  public Set<Evaluation> getEvaluationsPizza(Pizza pizza) {
    return null;
  }

  /**
   * Calcule la note moyenne d'une pizza.
   *
   * @param pizza pizza à évaluer
   * @return la note moyenne, ou 0 si aucune évaluation n'est disponible
   */
  @Override
  public double getNoteMoyenne(Pizza pizza) {
    return 0;
  }

  /**
   * Ajoute une évaluation pour une pizza.
   *
   * @param pizza pizza évaluée
   * @param note note attribuée (généralement entre 1 et 5)
   * @param commentaire commentaire facultatif
   * @return true si l'évaluation a été ajoutée
   * @throws NonConnecteException si le client n'est pas connecté
   * @throws CommandeException si la pizza n'est pas éligible à l'évaluation
   */
  @Override
  public boolean ajouterEvaluation(Pizza pizza, int note, String commentaire)
      throws NonConnecteException, CommandeException {
    return false;
  }

}
