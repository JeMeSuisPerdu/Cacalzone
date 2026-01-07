package ui;

import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import pizzas.Commande;
import pizzas.Evaluation;
import pizzas.InformationPersonnelle;
import pizzas.Ingredient;
import pizzas.Pizza;
import pizzas.ServiceClient;
import pizzas.TypePizza;

/**
 * Controleur JavaFX de la fenêtre du client.
 * Cette classe fait le lien entre la vue (fichier FXML) et le modèle (Service).
 * Elle gère :
 * - L'affichage du menu et le filtrage des pizzas.
 * - Le processus de commande (panier, validation).
 * - L'inscription et la connexion des clients.
 * - La consultation de l'historique et l'ajout d'évaluations.
 *
 * @author Eric Cariou
 */
public class ClientControleur {

  /** Service gérant la logique métier côté client. */
  private ServiceClient service;

  /** Liste tampon des commandes affichées dans l'interface. */
  private List<Commande> commandesAffichees = new ArrayList<>();

  /** Liste tampon des évaluations affichées dans l'interface. */
  private List<Evaluation> evaluationsAffichees = new ArrayList<>();

  /** Liste déroulante pour filtrer les pizzas par type. */
  @FXML
  private ChoiceBox<String> choiceBoxFiltreType;

  /** Champ de saisie pour l'adresse du client. */
  @FXML
  private TextField entreeAdresseClient;

  /** Champ de saisie pour l'âge du client. */
  @FXML
  private TextField entreeAgeClient;

  /** Champ affichant l'auteur d'une évaluation sélectionnée. */
  @FXML
  private TextField entreeAuteurEvaluation;

  /** Champ de saisie pour l'email du client (connexion/inscription). */
  @FXML
  private TextField entreeEmailClient;

  /** Champ affichant la note moyenne globale des évaluations. */
  @FXML
  private TextField entreeEvaluationMoyenneEvaluations;

  /** Champ de filtre pour inclure les pizzas contenant un ingrédient. */
  @FXML
  private TextField entreeFiltreContientIngredient;

  /** Champ de filtre pour définir un prix maximum. */
  @FXML
  private TextField entreeFiltrePrixMax;

  /** Champ de saisie pour le mot de passe du client. */
  @FXML
  private TextField entreeMotDePasseClient;

  /** Champ de saisie pour le nom du client. */
  @FXML
  private TextField entreeNomClient;

  /** Champ affichant le nom de la pizza sélectionnée. */
  @FXML
  private TextField entreeNomPizza;

  /** Champ affichant le nom de la pizza en cours d'évaluation. */
  @FXML
  private TextField entreeNomPizzaEvaluee;

  /** Champ affichant la note moyenne de la pizza sélectionnée. */
  @FXML
  private TextField entreeNoteMoyennePizza;

  /** Champ de saisie pour le prénom du client. */
  @FXML
  private TextField entreePrenomClient;

  /** Champ affichant le prix de la pizza sélectionnée. */
  @FXML
  private TextField entreePrixPizza;

  /** Champ affichant le type de la pizza sélectionnée. */
  @FXML
  private TextField entreeTypePizza;

  /** Label pour le titre de la liste des commandes. */
  @FXML
  private Label labelListeCommandes;

  /** Label pour le titre de la liste des pizzas. */
  @FXML
  private Label labelListePizzas;

  /** Composant visuel listant les commandes (en cours ou historique). */
  @FXML
  private ListView<String> listeCommandes;

  /** Composant visuel listant les évaluations d'une pizza. */
  @FXML
  private ListView<String> listeEvaluations;

  /** Composant visuel listant les ingrédients de la pizza sélectionnée. */
  @FXML
  private ListView<String> listeIngredients;

  /** Liste déroulante pour choisir une note d'évaluation. */
  @FXML
  private ChoiceBox<Integer> choiceBoxNoteEvaluation;

  /** Composant visuel listant les pizzas du menu. */
  @FXML
  private ListView<String> listePizzas;

  /** Zone pour afficher la photo de la pizza. */
  @FXML
  private StackPane panePhotoPizza;

  /** Zone de texte pour saisir un commentaire d'évaluation. */
  @FXML
  private TextArea texteCommentaireEvaluation;

  /**
   * Met à jour la liste visuelle des pizzas en appliquant les filtres actifs.
   * Récupère la liste filtrée depuis le service et met à jour l'affichage
   * (ListView) ainsi que le label descriptif.
   *
   * @param message Le message à afficher dans le label au-dessus de la liste.
   */
  private void actualiserPizzas(String message) {
    listePizzas.getItems().clear();
    for (Pizza p : service.selectionPizzaFiltres()) {
      listePizzas.getItems().add(p.getNom() + " - " + p.getPrix() + "€");
    }
    labelListePizzas.setText(message);
  }

  /**
   * Affiche les commandes actuellement en cours de préparation par le client.
   * Interroge le service pour récupérer les commandes non traitées.
   * Si aucune commande n'est en cours, un message spécifique est affiché.
   *
   * @param event L'événement du clic sur le bouton.
   */
  @FXML
  void actionBoutonAfficherCommandesEnCours(ActionEvent event) {
    try {
      listeCommandes.getItems().clear();
      this.commandesAffichees = service.getCommandesEncours();

      if (commandesAffichees.isEmpty()) {
        labelListeCommandes.setText("Aucune commande en cours.");
      } else {
        for (Commande c : commandesAffichees) {
          listeCommandes.getItems().add("Commande du " + c.getDate()
              + " [" + c.getEtat() + "]");
        }
        labelListeCommandes.setText("Vos commandes en cours");
      }
    } catch (Exception e) {
      afficherPopupErreur("Erreur : " + e.getMessage());
    }
  }

  /**
   * Affiche l'historique complet des commandes déjà traitées pour ce client.
   * Filtre les commandes passées pour ne garder que celles à l'état TRAITEE.
   * Affiche une erreur si le client n'est pas connecté.
   *
   * @param event L'événement du clic sur le bouton.
   */
  @FXML
  void actionBoutonAfficherCommandesTtraitees(ActionEvent event) {
    try {
      listeCommandes.getItems().clear();
      this.commandesAffichees.clear();

      for (Commande c : service.getCommandePassees()) {
        if (c.getEtat() == Commande.Etat.TRAITEE) {
          this.commandesAffichees.add(c);
          listeCommandes.getItems().add("Commande traitée du " + c.getDate()
              + " (" + c.getPrixTotal() + "€)");
        }
      }
      labelListeCommandes.setText("Historique des commandes traitées");
    } catch (Exception e) {
      afficherPopupErreur("Vous devez être connecté.");
    }
  }

  /**
   * Affiche les évaluations associées à la pizza dont le nom est saisi.
   * Récupère la pizza via le service, calcule sa note moyenne et liste
   * toutes les évaluations détaillées.
   *
   * @param event L'événement du clic sur le bouton.
   */
  @FXML
  void actionBoutonAfficherEvaluationPizzas(ActionEvent event) {
    String nomPizza = entreeNomPizza.getText();
    if (nomPizza == null || nomPizza.isEmpty()) {
      afficherPopupErreur("Sélectionnez une pizza d'abord.");
      return;
    }

    Pizza p = service.getPizza(nomPizza);
    if (p != null) {
      entreeNomPizzaEvaluee.setText(p.getNom());
      entreeEvaluationMoyenneEvaluations.setText(
          String.format("%.2f", service.getNoteMoyenne(p)));

      listeEvaluations.getItems().clear();
      this.evaluationsAffichees = new ArrayList<>(
          service.getEvaluationsPizza(p));

      for (Evaluation ev : evaluationsAffichees) {
        listeEvaluations.getItems().add(ev.getNote() + "/5 par "
            + ev.getEmailClient());
      }
    }
  }

  /**
   * Réinitialise l'affichage pour montrer toutes les pizzas du menu.
   * Efface les filtres visuels et recharge la liste complète depuis le service.
   *
   * @param event L'événement du clic sur le bouton.
   */
  @FXML
  void actionBoutonAfficherToutesPizzas(ActionEvent event) {
    listePizzas.getItems().clear();
    for (Pizza p : service.getPizzas()) {
      listePizzas.getItems().add(p.getNom() + " - " + p.getPrix() + "€");
    }
    labelListePizzas.setText("Toutes les pizzas du menu");
  }

  /**
   * Enregistre une nouvelle évaluation pour la pizza sélectionnée.
   * Vérifie que la pizza et la note sont renseignées.
   * Affiche un message de succès ou d'erreur (si non connecté).
   * Met à jour la note moyenne affichée après l'ajout.
   *
   * @param event L'événement du clic sur le bouton.
   */
  @FXML
  void actionBoutonAjouterMonEvaluation(ActionEvent event) {
    String nomPizza = entreeNomPizza.getText();
    Integer note = choiceBoxNoteEvaluation.getValue();
    String commentaire = texteCommentaireEvaluation.getText();

    if (nomPizza.isEmpty() || note == null) {
      afficherPopupErreur("Sélectionnez une pizza et une note.");
      return;
    }

    try {
      Pizza p = service.getPizza(nomPizza);
      if (service.ajouterEvaluation(p, note, commentaire)) {
        afficherPopupInformation("Merci ! Votre évaluation a été ajoutée.");
      } else {
        afficherPopupErreur("ERREUR : Commentaire non ajouté.");
      }

      double nouvelleNote = service.getNoteMoyenne(p);
      entreeNoteMoyennePizza.setText(String.format("%.2f", nouvelleNote));
      texteCommentaireEvaluation.clear();
    } catch (Exception e) {
      afficherPopupErreur("Vous devez être connecté pour évaluer une pizza.");
    }
  }

  /**
   * Ajoute une unité de la pizza sélectionnée à la commande en cours.
   * Vérifie qu'une commande est bien active (créée).
   * Met à jour la liste visuelle du panier (listeCommandes).
   *
   * @param event L'événement du clic sur le bouton.
   */
  @FXML
  void actionBoutonAjouterPizzaSelectionneeCommande(ActionEvent event) {
    String nomPizza = entreeNomPizza.getText();
    if (nomPizza.isEmpty()) {
      afficherPopupErreur("Veuillez sélectionner une pizza d'abord.");
      return;
    }

    try {
      Pizza p = service.getPizza(nomPizza);
      Commande c = service.getCommandeActive();

      if (c == null) {
        afficherPopupErreur(
            "Veuillez d'abord cliquer sur 'Créer nouvelle commande'.");
        return;
      }

      service.ajouterPizza(p, 1, c);
      listeCommandes.getItems().add(p.getNom());
      afficherPopupInformation(p.getNom() + " ajoutée au panier.");
    } catch (Exception e) {
      afficherPopupErreur("Erreur : " + e.getMessage());
    }
  }

  /**
   * Active un filtre pour ne conserver que les pizzas contenant l'ingrédient
   * saisi.
   * Met à jour l'affichage de la liste des pizzas via 'actualiserPizzas'.
   *
   * @param event L'événement du clic sur le bouton.
   */
  @FXML
  void actionBoutonAppliquerFiltreContientngredient(ActionEvent event) {
    String ingredient = entreeFiltreContientIngredient.getText();
    if (ingredient != null && !ingredient.isEmpty()) {
      service.ajouterFiltre(ingredient);
      actualiserPizzas("Filtre : Contient " + ingredient);
    }
  }

  /**
   * Active un filtre sur le prix maximum.
   * Vérifie que la saisie est un nombre valide avant d'appliquer le filtre.
   *
   * @param event L'événement du clic sur le bouton.
   */
  @FXML
  void actionBoutonAppliquerFiltrePrixMax(ActionEvent event) {
    try {
      if (!entreeFiltrePrixMax.getText().isEmpty()) {
        double prix = Double.parseDouble(entreeFiltrePrixMax.getText());
        service.ajouterFiltre(prix);
        actualiserPizzas("Filtre : Max " + prix + "€");
      }
    } catch (NumberFormatException e) {
      afficherPopupErreur("Prix invalide.");
    }
  }

  /**
   * Active un filtre sur le type de pizza (ex: VEGETARIENNE).
   *
   * @param event L'événement du clic sur le bouton.
   */
  @FXML
  void actionBoutonAppliquerFiltreType(ActionEvent event) {
    String typeStr = choiceBoxFiltreType.getValue();
    if (typeStr != null) {
      service.ajouterFiltre(TypePizza.valueOf(typeStr));
      actualiserPizzas("Filtre : Type " + typeStr);
    }
  }

  /**
   * Tente de connecter l'utilisateur avec l'email et le mot de passe saisis.
   * Affiche un message de bienvenue en cas de succès, ou une erreur sinon.
   *
   * @param event L'événement du clic sur le bouton.
   */
  @FXML
  void actionBoutonConnexion(ActionEvent event) {
    String email = entreeEmailClient.getText();
    String mdp = entreeMotDePasseClient.getText();

    if (service.connexion(email, mdp)) {
      afficherPopupInformation("Bienvenue ! Connexion réussie.");
    } else {
      afficherPopupErreur("Identifiants incorrects.");
    }
  }

  /**
   * Initialise une nouvelle commande vide pour le client connecté.
   * Vide la liste visuelle du panier pour commencer une nouvelle saisie.
   *
   * @param event L'événement du clic sur le bouton.
   */
  @FXML
  void actionBoutonCreerNouvelleCommande(ActionEvent event) {
    try {
      service.debuterCommande();
      afficherPopupInformation(
          "Nouvelle commande créée. Vous pouvez ajouter des pizzas.");
      labelListeCommandes.setText("Commande en cours...");
      listeCommandes.getItems().clear();
    } catch (Exception e) {
      afficherPopupErreur("ERREUR : " + e.getMessage());
    }
  }

  /**
   * Déconnecte l'utilisateur actuel.
   * Vide les champs de saisie des identifiants par sécurité.
   *
   * @param event L'événement du clic sur le bouton.
   */
  @FXML
  void actionBoutonDeconnexion(ActionEvent event) {
    try {
      service.deconnexion();
      afficherPopupInformation("Vous avez été déconnecté.");
      entreeEmailClient.clear();
      entreeMotDePasseClient.clear();
    } catch (Exception e) {
      afficherPopupErreur("Erreur : " + e.getMessage());
    }
  }

  /**
   * Tente d'inscrire un nouveau client avec les informations saisies.
   * Vérifie que tous les champs obligatoires sont remplis et que l'âge est
   * un entier valide.
   * Affiche une erreur si l'email existe déjà.
   *
   * @param event L'événement du clic sur le bouton.
   */
  @FXML
  void actionBoutonInscription(ActionEvent event) {
    String email = entreeEmailClient.getText();
    String mdp = entreeMotDePasseClient.getText();
    String nom = entreeNomClient.getText();
    String prenom = entreePrenomClient.getText();
    String adresse = entreeAdresseClient.getText();
    String ageBrut = entreeAgeClient.getText();

    if (email.isEmpty() || mdp.isEmpty() || nom.isEmpty()
        || prenom.isEmpty() || adresse.isEmpty() || ageBrut.isEmpty()) {
      afficherPopupErreur("Veuillez remplir tous les champs du formulaire.");
      return;
    }

    try {
      int age = Integer.parseInt(ageBrut);
      InformationPersonnelle infos = new InformationPersonnelle(nom, prenom,
          adresse, age);
      int res = service.inscription(email, mdp, infos);

      if (res == 0) {
        afficherPopupInformation(
            "Inscription réussie ! Vous pouvez vous connecter.");
      } else {
        afficherPopupErreur("Cet email est déjà utilisé par un autre compte.");
      }

      entreeEmailClient.clear();
      entreeMotDePasseClient.clear();
      entreeNomClient.clear();
      entreePrenomClient.clear();
      entreeAdresseClient.clear();
      entreeAgeClient.clear();
    } catch (NumberFormatException e) {
      afficherPopupErreur("L'âge doit être un nombre entier valide.");
    }
  }

  /**
   * Réinitialise tous les filtres de recherche (Prix, Type, Ingrédients).
   * Vide les champs de saisie des filtres et réaffiche toutes les pizzas.
   *
   * @param event L'événement du clic sur le bouton.
   */
  @FXML
  void actionBoutonReinitialiserFiltre(ActionEvent event) {
    service.supprimerFiltres();
    entreeFiltrePrixMax.clear();
    entreeFiltreContientIngredient.clear();
    choiceBoxFiltreType.setValue(null);
    actionBoutonAfficherToutesPizzas(event);
  }

  /**
   * Valide la commande en cours pour l'envoyer au pizzaïolo.
   * Une fois validée, la commande ne peut plus être modifiée par le client.
   *
   * @param event L'événement du clic sur le bouton.
   */
  @FXML
  void actionBoutonValiderCommandeEnCours(ActionEvent event) {
    try {
      service.validerCommande(null);
      afficherPopupInformation("Commande validée !");
      listeCommandes.getItems().clear();
      labelListeCommandes.setText("Aucune commande en cours");
    } catch (Exception e) {
      afficherPopupErreur("Erreur : " + e.getMessage());
    }
  }

  /**
   * Affiche le détail d'une commande sélectionnée dans la liste.
   * Remplit la liste des pizzas avec le contenu de la commande.
   *
   * @param event L'événement de la souris.
   */
  @FXML
  void actionSelectionCommnade(MouseEvent event) {
    int index = listeCommandes.getSelectionModel().getSelectedIndex();
    if (index >= 0 && index < commandesAffichees.size()) {
      Commande c = commandesAffichees.get(index);

      listePizzas.getItems().clear();
      for (java.util.Map.Entry<Pizza, Integer> entry : c.getPizzas()
          .entrySet()) {
        Pizza p = entry.getKey();
        int qte = entry.getValue();
        listePizzas.getItems()
            .add(qte + " x " + p.getNom() + " (" + p.getPrix() + "€)");
      }
      labelListePizzas.setText("Contenu de la commande sélectionnée");
    }
  }

  /**
   * Affiche les détails d'une évaluation sélectionnée dans la liste.
   * Remplit les champs (Auteur, Note, Commentaire) pour consultation.
   *
   * @param event L'événement de la souris.
   */
  @FXML
  void actionSelectionEvaluation(MouseEvent event) {
    int index = listeEvaluations.getSelectionModel().getSelectedIndex();
    if (index >= 0 && index < evaluationsAffichees.size()) {
      Evaluation ev = evaluationsAffichees.get(index);
      entreeAuteurEvaluation.setText(ev.getEmailClient());
      choiceBoxNoteEvaluation.setValue(ev.getNote());
      texteCommentaireEvaluation.setText(ev.getCommentaire());
    }
  }

  /**
   * Affiche les détails d'une pizza sélectionnée dans la liste principale.
   * Met à jour l'affichage des ingrédients, du prix, de la note moyenne, etc.
   *
   * @param event L'événement de la souris.
   */
  @FXML
  void actionSelectionPizza(MouseEvent event) {
    String selection = listePizzas.getSelectionModel().getSelectedItem();

    if (selection != null) {
      String nomPizza = selection.split(" - ")[0];
      Pizza p = service.getPizza(nomPizza);

      if (p != null) {
        entreeNomPizza.setText(p.getNom());
        entreePrixPizza.setText(p.getPrix() + " €");
        entreeTypePizza.setText(p.getTypePizza().name());

        listeIngredients.getItems().clear();
        for (Ingredient ing : p.getIngredients()) {
          listeIngredients.getItems().add(ing.getNom());
        }

        double note = service.getNoteMoyenne(p);
        entreeNoteMoyennePizza.setText(String.format("%.2f", note));
      }
    }
  }

  /**
   * Injecte l'instance du service métier dans le contrôleur.
   * Cette méthode est appelée par `MainInterface` au démarrage.
   *
   * @param s L'instance de ServiceClient.
   */
  public void setServices(ServiceClient s) {
    this.service = s;
  }

  /**
   * Affiche une fenêtre surgie (popup) générique.
   *
   * @param message Le message à afficher.
   * @param type Le type d'alerte (Erreur/Info).
   */
  private void afficherPopup(String message, AlertType type) {
    Alert alert = new Alert(type);
    if (type == AlertType.ERROR) {
      alert.setTitle("Erreur");
    } else {
      alert.setTitle("Information");
    }
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.setResizable(true);
    alert.showAndWait();
  }

  /**
   * Affiche une popup d'erreur standardisée.
   *
   * @param message Le message d'erreur.
   */
  private void afficherPopupErreur(String message) {
    this.afficherPopup(message, AlertType.ERROR);
  }

  /**
   * Affiche une popup d'information standardisée.
   *
   * @param message Le message informatif.
   */
  private void afficherPopupInformation(String message) {
    this.afficherPopup(message, AlertType.INFORMATION);
  }

  /**
   * Initialise les composants de l'interface graphique.
   * Remplit les ChoiceBox (Note, TypePizza) avec les valeurs par défaut.
   */
  @FXML
  void initialize() {
    if (choiceBoxNoteEvaluation != null) {
      choiceBoxNoteEvaluation.getItems().addAll(0, 1, 2, 3, 4, 5);
      choiceBoxNoteEvaluation.setValue(5);
    }
    if (choiceBoxFiltreType != null) {
      for (TypePizza t : TypePizza.values()) {
        choiceBoxFiltreType.getItems().add(t.name());
      }
    }
  }
}