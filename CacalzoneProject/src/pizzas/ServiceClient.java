package pizzas;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service gérant la logique métier réservée aux clients.
 * Permet l'inscription, la connexion, la gestion du profil,
 * la passation de commandes et la consultation de l'historique.
 */
public class ServiceClient implements InterClient {

  /** Référence vers le menu centralisant les données. */
  private final Menu menu;

  /** Le client actuellement authentifié sur le service. */
  private CompteClient clientConnecte;

  /** Filtre optionnel sur le type de pizza. */
  private TypePizza filtreType;

  /** Filtre sur le prix maximum, initialisé à la valeur maximale. */
  private double filtrePrixMax = Double.MAX_VALUE;

  /** Liste des noms d'ingrédients à avoir lors de la recherche. */
  private Set<String> filtreIngredients = new HashSet<>();
  
  /** La commande actuellement en cours de préparation par le client. */
  private Commande commandeEnCours;

  /**
   * Constructeur du service client.
   *
   * @param menu Le menu contenant les données de la pizzeria.
   */
  public ServiceClient(Menu menu) {
    this.menu = menu;
  }

  @Override
  public int inscription(String email, String mdp,
      InformationPersonnelle info) {
    if (email == null || mdp == null || info == null) {
      return -3;
    }
    if (menu.trouverUtilisateur(email) != null) {
      return -1;
    }

    CompteClient nouveau = new CompteClient(email, mdp, info);
    menu.ajouterUtilisateur(nouveau);
    return 0;
  }

  @Override
  public boolean connexion(String email, String mdp) {
    Utilisateur u = menu.authentifier(email, mdp);
    if (u instanceof CompteClient) {
      this.clientConnecte = (CompteClient) u;
      return true;
    }
    return false;
  }

  @Override
  public void deconnexion() throws NonConnecteException {
    if (clientConnecte == null) {
      throw new NonConnecteException();
    }
    this.clientConnecte = null;
  }

  @Override
  public Commande debuterCommande() throws NonConnecteException {
    if (clientConnecte == null) {
      throw new NonConnecteException();
    }
    this.commandeEnCours = new Commande(clientConnecte);
    return this.commandeEnCours;
  }

  @Override
  public void validerCommande(Commande cmd)
      throws NonConnecteException, CommandeException {
    if (clientConnecte == null) {
      throw new NonConnecteException();
    }
    
    Commande commande = (cmd != null) ? cmd : this.commandeEnCours;

    if (commande == null) {
      throw new CommandeException("ERREUR : Commande vide ou inexistante.");
    }

    if (commande.valider()) {
      menu.ajouterCommande(commande);
      clientConnecte.ajouterCommande(commande);
      this.commandeEnCours = null;
    }
  }

  @Override
  public Set<Pizza> getPizzas() {
    return menu.getPizzas();
  }

  @Override
  public void ajouterPizza(Pizza p, int n, Commande c)
      throws CommandeException {
    if (c == null || p == null) {
      throw new CommandeException("ERREUR : Pizza ou Commande inexistante.");
    }
    c.ajouterPizza(p, n);
  }

  @Override
  public void annulerCommande(Commande c) {
    if (c != null) {
      c.annuler();
    }
  }

  @Override
  public List<Commande> getCommandesEncours() {
    if (clientConnecte == null) {
      return new ArrayList<>();
    }

    List<Commande> enCours = new ArrayList<>();
    for (Commande c : clientConnecte.getHistoriqueCommandes()) {
      if (c.getEtat() == Commande.Etat.VALIDEE) {
        enCours.add(c);
      }
    }
    return enCours;
  }

  @Override
  public List<Commande> getCommandePassees() {
    if (clientConnecte != null) {
      return clientConnecte.getHistoriqueCommandes();
    }
    return new ArrayList<>();
  }

  @Override
  public void ajouterFiltre(TypePizza t) {
    this.filtreType = t;
  }

  @Override
  public void ajouterFiltre(String... ingredients) {
    this.filtreIngredients.clear();
    for (String s : ingredients) {
      this.filtreIngredients.add(s.toLowerCase());
    }
  }

  @Override
  public void ajouterFiltre(double p) {
    this.filtrePrixMax = p;
  }

  @Override
  public Set<Pizza> selectionPizzaFiltres() {
    Set<Pizza> resultat = new HashSet<>();
    for (Pizza p : menu.getPizzas()) {
      if (p.getPrix() > filtrePrixMax) {
        continue;
      }

      if (filtreType != null && p.getTypePizza() != filtreType) {
        continue;
      }

      boolean contientIngredientFiltre = false;
      
      // Si la liste de filtres est vide, on considère que la pizza est valide
      // (Sinon aucune pizza ne s'affichera par défaut)
      if (this.filtreIngredients.isEmpty()) {
        contientIngredientFiltre = true;
      } else {
        for (Ingredient ing : p.getIngredients()) {
          if (this.filtreIngredients.contains(ing.getNom().toLowerCase())) {
            contientIngredientFiltre = true;
            break;
          }
        }
      }

      if (contientIngredientFiltre) {
        resultat.add(p);
      }
    }
    return resultat;
  }
  
  @Override
  public void supprimerFiltres() {
    this.filtreType = null;
    this.filtrePrixMax = Double.MAX_VALUE;
    this.filtreIngredients.clear();
  }

  @Override
  public Set<Evaluation> getEvaluationsPizza(Pizza p) {
    if (p != null) {
      return p.getEvaluations();
    }
    return new HashSet<>();
  }

  @Override
  public double getNoteMoyenne(Pizza p) {
    if (p == null || p.getEvaluations().isEmpty()) {
      return 0.0;
    }

    double somme = 0;
    for (Evaluation e : p.getEvaluations()) {
      somme += e.getNote();
    }
    return somme / p.getEvaluations().size();
  }

  @Override
  public boolean ajouterEvaluation(Pizza p, int note, String commentaire) {
    if (clientConnecte == null || p == null) {
      return false;
    }
    Evaluation eval = new Evaluation(note, commentaire,
        clientConnecte.getEmail());
    p.getEvaluations().add(eval);
    return true;
  }

  /**
   * Méthode qui permet de récupérer le menu associé au service.
   *
   * @return L'objet Menu.
   */
  public Menu getMenu() {
    return menu;
  }

  /**
   * Méthode qui permet de retrouver une pizza par son nom.
   *
   * @param nom Le nom de la pizza.
   * @return La Pizza trouvée ou null.
   */
  public Pizza getPizza(String nom) {
    for (Pizza p : this.menu.getPizzas()) {
      if (p.getNom().equals(nom)) {
        return p;
      }
    }
    return null;
  }

  /**
   * Méthode qui permet de récupérer la commande actuellement active.
   *
   * @return La commande en cours.
   */
  public Commande getCommandeActive() {
    return this.commandeEnCours;
  }
  
  /**
   * Récupère le compte du client actuellement connecté au service.
   *
   * @return Le client connecté, ou null si aucun utilisateur n'est authentifié.
   */
  public CompteClient getClient() {
    return this.clientConnecte;
  }
  
}
