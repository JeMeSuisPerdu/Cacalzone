package ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import pizzas.Commande;
import pizzas.InformationPersonnelle;
import pizzas.Ingredient;
import pizzas.Pizza;
import pizzas.ServicePizzaiolo;
import pizzas.ServiceSauvegarde;
import pizzas.TypePizza;

/**
 * Controleur JavaFX de la fenêtre du pizzaïolo.
 * Cette classe fait le lien entre la vue (fichier FXML) et le modèle (Service).
 * Elle gère :
 * - La création et modification des pizzas et ingrédients.
 * - La gestion des prix et des règles métier (interdictions).
 * - Le traitement des commandes validées par les clients.
 * - L'affichage des statistiques financières.
 *
 * @author Eric Cariou
 */
public class PizzaioloControleur {

  /** Service gérant la logique métier du pizzaïolo. */
  private ServicePizzaiolo service;

  /** Liste tampon des ingrédients affichés dans la ListView. */
  private List<Ingredient> ingredientsAffiches = new ArrayList<>();

  /** Liste tampon des pizzas affichées dans la ListView. */
  private List<Pizza> pizzasAffichees = new ArrayList<>();

  /** Liste tampon des commandes affichées dans la ListView. */
  private List<Commande> commandesAffichees = new ArrayList<>();

  /**
   * Liste déroulante permettant de sélectionner un type de pizza
   * (ex: VEGETARIENNE, VIANDE) pour lui appliquer une interdiction.
   */
  @FXML
  private ChoiceBox<String> choiceBoxTypeIngredient;

  /**
   * Liste déroulante permettant de sélectionner le type d'une pizza
   * lors de sa création.
   */
  @FXML
  private ChoiceBox<String> choiceBoxTypePizza;

  /**
   * Liste déroulante contenant les noms des clients pour filtrer
   * l'historique des commandes.
   */
  @FXML
  private ComboBox<String> comboBoxClients;

  /**
   * Champ affichant le bénéfice total généré par le client sélectionné
   * dans la liste déroulante.
   */
  @FXML
  private TextField entreeBeneficeClient;

  /**
   * Champ affichant le bénéfice net généré par la commande spécifiquement
   * sélectionnée dans la liste.
   */
  @FXML
  private TextField entreeBeneficeCommande;

  /**
   * Champ affichant la somme des bénéfices de toutes les commandes
   * actuellement traitées et affichées.
   */
  @FXML
  private TextField entreeBeneficeTotalCommandes;

  /**
   * Champ affichant le bénéfice cumulé généré par la pizza sélectionnée
   * (Bénéfice unitaire * Nombre de ventes).
   */
  @FXML
  private TextField entreeBeneficeTotalPizza;

  /**
   * Champ affichant la marge nette sur une seule pizza sélectionnée
   * (Prix de vente - Prix de revient minimal).
   */
  @FXML
  private TextField entreeBeneficeUnitairePizza;

  /**
   * Champ affichant le nombre total de fois où la pizza sélectionnée
   * a été commandée par les clients.
   */
  @FXML
  private TextField entreeNbCommandesPizza;

  /**
   * Champ affichant le nombre total de pizzas commandées par le client
   * sélectionné dans la liste déroulante.
   */
  @FXML
  private TextField entreeNbPizzasClient;

  /**
   * Champ de saisie pour le nom d'un ingrédient (création ou modification).
   */
  @FXML
  private TextField entreeNomIngredient;

  /**
   * Champ de saisie pour le nom d'une pizza (création ou modification).
   */
  @FXML
  private TextField entreeNomPizza;

  /**
   * Champ (inutilisé dans cette version) prévu pour le total des commandes.
   */
  @FXML
  private TextField entreeNombreTotalCommandes;

  /**
   * Champ affichant le chemin d'accès (URI) vers l'image de la pizza.
   */
  @FXML
  private TextField entreePhotoPizza;

  /**
   * Champ de saisie pour le prix d'achat d'un ingrédient.
   */
  @FXML
  private TextField entreePrixIngredient;

  /**
   * Champ en lecture seule affichant le coût de revient minimum de la pizza.
   * Calculé : Somme des ingrédients + 40% (arrondi à la dizaine sup).
   */
  @FXML
  private TextField entreePrixMinimalPizza;

  /**
   * Champ de saisie pour définir le prix de vente final de la pizza.
   * Doit être supérieur au prix minimal.
   */
  @FXML
  private TextField entreePrixVentePizza;

  /**
   * Label affichant le titre ou le statut de la liste des commandes.
   */
  @FXML
  private Label labelListeCommandes;

  /**
   * Label affichant le titre ou le statut de la liste des ingrédients.
   */
  @FXML
  private Label labelListeIngredients;

  /**
   * Label affichant le titre ou le statut de la liste des pizzas.
   */
  @FXML
  private Label labelListePizzas;

  /**
   * Composant visuel listant les commandes (en cours ou historique).
   */
  @FXML
  private ListView<String> listeCommandes;

  /**
   * Composant visuel listant les ingrédients (du menu ou d'une pizza).
   */
  @FXML
  private ListView<String> listeIngredients;

  /**
   * Composant visuel listant les pizzas disponibles ou triées.
   */
  @FXML
  private ListView<String> listePizzas;

  /**
   * Trie les pizzas en fonction de leur popularité (nombre de commandes)
   * et met à jour l'affichage dans la liste.
   * Utilise la méthode service.classementPizzasParNombreCommandes().
   *
   * @param event L'événement du clic sur le bouton.
   */
  @FXML
  void actionBoutonAfficherListeTrieePizzas(ActionEvent event) {
    listePizzas.getItems().clear();
    pizzasAffichees = service.classementPizzasParNombreCommandes();
    for (Pizza p : pizzasAffichees) {
      listePizzas.getItems().add(p.getNom() + " ("
          + service.nombrePizzasCommandees(p) + " ventes)");
    }
    labelListePizzas.setText("Pizzas triées par succès");
  }

  /**
   * Réinitialise la liste des ingrédients pour afficher l'ensemble
   * des ingrédients disponibles dans le menu global.
   * Efface les filtres liés à la sélection d'une pizza spécifique.
   *
   * @param event L'événement du clic sur le bouton.
   */
  @FXML
  void actionBoutonAfficherTousIngredients(ActionEvent event) {
    listeIngredients.getItems().clear();
    ingredientsAffiches.clear();
    for (Ingredient ing : service.getMenu().getIngredients()) {
      ingredientsAffiches.add(ing);
      listeIngredients.getItems().add(ing.getNom() + " - " + ing.getPrix()
          + "€" + " - " + "Interdit : " + ing.getTypesPizzaInterdits());
    }
    labelListeIngredients.setText("Tous les ingrédients disponibles");
  }

  /**
   * Affiche la liste complète de toutes les pizzas définies dans le menu.
   * Met à jour la liste tampon 'pizzasAffichees'.
   *
   * @param event L'événement du clic sur le bouton.
   */
  @FXML
  void actionBoutonAfficherToutesPizzas(ActionEvent event) {
    listePizzas.getItems().clear();
    pizzasAffichees.clear();
    for (Pizza p : service.getPizzas()) {
      pizzasAffichees.add(p);
      listePizzas.getItems().add(p.getNom());
    }
    labelListePizzas.setText("Toutes les pizzas en vente");
  }

  /**
   * Tente d'ajouter l'ingrédient sélectionné à la pizza sélectionnée.
   * Vérifie via le service si l'ajout est possible (règles d'interdiction).
   * Met à jour le prix minimal affiché en cas de succès.
   * Affiche une popup d'erreur si le service renvoie un code d'erreur
   * (ex: ingrédient interdit pour ce type de pizza).
   *
   * @param event L'événement du clic sur le bouton.
   */
  @FXML
  void actionBoutonAjouterIngredientPizza(ActionEvent event) {
    int indexP = listePizzas.getSelectionModel().getSelectedIndex();
    int indexI = listeIngredients.getSelectionModel().getSelectedIndex();
    if (indexP < 0 || indexI < 0) {
      afficherPopupErreur("Sélectionnez une pizza et un ingrédient.");
      return;
    }
    Pizza p = pizzasAffichees.get(indexP);
    Ingredient ing = ingredientsAffiches.get(indexI);
    int res = service.ajouterIngredientPizza(p, ing.getNom());
    if (res == 0) {
      entreePrixMinimalPizza.setText(String.format("%.2f",
          service.calculerPrixMinimalPizza(p)));
      actionListeSelectionPizza(null);
    } else {
      afficherPopupErreur(
          "Ajout impossible (Ingrédient interdit pour ce type).");
    }
  }

  /**
   * Filtre et affiche uniquement les commandes qui ont déjà été traitées
   * par le pizzaïolo (état TRAITEE).
   *
   * @param event L'événement du clic sur le bouton.
   */
  @FXML
  void actionBoutonCommandesDejaTraitees(ActionEvent event) {
    listeCommandes.getItems().clear();
    commandesAffichees = service.commandesDejaTraitees();
    for (Commande c : commandesAffichees) {
      listeCommandes.getItems().add("Commande du " + c.getDate() + " ("
          + c.getClient().getInfos().getNom() + ")");
    }
    labelListeCommandes.setText("Historique de toutes les commandes traitées");
  }

  /**
   * Récupère les commandes en attente (état VALIDEE) depuis le service.
   * Cette action a pour effet de bord de marquer ces commandes comme TRAITEE.
   * Met à jour le bénéfice total et rafraîchit la liste des clients.
   *
   * @param event L'événement du clic sur le bouton.
   */
  @FXML
  void actionBoutonCommandesNonTraitees(ActionEvent event) {
    listeCommandes.getItems().clear();
    commandesAffichees = service.commandeNonTraitees();
    for (Commande c : commandesAffichees) {
      listeCommandes.getItems().add("Commande de "
          + c.getClient().getInfos().getNom());
    }
    labelListeCommandes.setText("Commandes venant d'être traitées");
    entreeBeneficeTotalCommandes.setText(String.format("%.2f",
        service.beneficeToutesCommandes()));
    rafraichirClients();
  }

  /**
   * Filtre l'historique des commandes pour n'afficher que celles appartenant
   * au client sélectionné dans la ComboBox.
   * Affiche une erreur si aucun client n'est sélectionné.
   *
   * @param event L'événement du clic sur le bouton.
   */
  @FXML
  void actionBoutonCommandesTraiteesClient(ActionEvent event) {
    String clientStr = comboBoxClients.getValue();
    if (clientStr == null) {
      afficherPopupErreur("Sélectionnez un client dans la liste déroulante.");
      return;
    }

    InformationPersonnelle clientInfo = null;
    for (InformationPersonnelle info : service.ensembleClients()) {
      if ((info.getNom() + " " + info.getPrenom()).equals(clientStr)) {
        clientInfo = info;
        break;
      }
    }

    if (clientInfo != null) {
      listeCommandes.getItems().clear();
      commandesAffichees = service.commandesTraiteesClient(clientInfo);
      for (Commande c : commandesAffichees) {
        listeCommandes.getItems().add("Commande du " + c.getDate()
            + " [" + c.getPrixTotal() + "€]");
      }
      labelListeCommandes.setText("Commandes traitées de " + clientStr);
    }
  }

  /**
   * Crée un nouvel ingrédient à partir des champs de saisie.
   * Gère les erreurs de saisie (prix non numérique) et les erreurs métier
   * (ingrédient déjà existant).
   * Vide les champs de saisie en cas de succès.
   *
   * @param event L'événement du clic sur le bouton.
   */
  @FXML
  void actionBoutonCreerIngredient(ActionEvent event) {
    try {
      String nom = entreeNomIngredient.getText();
      double prix = Double.parseDouble(entreePrixIngredient.getText());
      int res = service.creerIngredient(nom, prix);
      if (res == 0) {
        afficherPopupInformation("Ingrédient créé !");
        viderChampsIngredient();
        actionBoutonAfficherTousIngredients(event);
      } else {
        afficherPopupErreur(
            "Erreur : Ingrédient déjà présent ou données invalides.");
      }
    } catch (NumberFormatException e) {
      afficherPopupErreur("Le prix doit être un nombre.");
    }
  }

  /**
   * Crée une nouvelle pizza vierge avec le nom et le type saisis.
   * Affiche une erreur si le nom est vide ou le type non sélectionné.
   * Met à jour la liste des pizzas après création.
   *
   * @param event L'événement du clic sur le bouton.
   */
  @FXML
  void actionBoutonCreerPizza(ActionEvent event) {
    String nom = entreeNomPizza.getText();
    String typeStr = choiceBoxTypePizza.getValue();
    if (nom.isEmpty() || typeStr == null) {
      afficherPopupErreur("Nom et Type obligatoires.");
      return;
    }
    if (service.creerPizza(nom, TypePizza.valueOf(typeStr)) != null) {
      afficherPopupInformation("Pizza créée ! Ajoutez des ingrédients.");
      actionBoutonAfficherToutesPizzas(event);
    }
  }

  /**
   * Autorise ou interdit un type de pizza pour un ingrédient donné.
   * Si le type séléctionné est déjà interdit, alors il devient autorisé,
   * sinon il est bien ajouté à la liste des types interdits.
   * Préviens l'utilisateur en cas de réussite ou d'échec.
   * @param event Événement déclenché par le bouton.
   */
  @FXML
  void actionBoutonInterdireAutoriserIngredient(ActionEvent event) {
      String nom = entreeNomIngredient.getText();
      String typeStr = choiceBoxTypeIngredient.getValue();
      
      if (nom.isEmpty() || typeStr == null) {
          afficherPopupErreur("Sélectionnez un ingrédient et un type.");
          return;
      }
      
      if (service.autoriserTypePizza(nom, TypePizza.valueOf(typeStr))) {
          afficherPopupInformation(nom + " est maintenant autorisé pour le type " + typeStr);
      } else if (service.interdireIngredient(nom, TypePizza.valueOf(typeStr))) {
              afficherPopupInformation(nom + " est maintenant interdit pour le type " + typeStr);
      } else {
              afficherPopupErreur("Erreur lors de l'interdiction ou l'autorisation.");
      }
  }

  /**
   * Met à jour le prix de l'ingrédient affiché dans les champs de texte.
   * Appelle le service pour effectuer la modification et rafraîchit la liste.
   * Gère l'exception si le format du prix est incorrect.
   *
   * @param event L'événement du clic sur le bouton.
   */
  @FXML
  void actionBoutonModifierPrixIngredient(ActionEvent event) {
    try {
      String nom = entreeNomIngredient.getText();
      double prix = Double.parseDouble(entreePrixIngredient.getText());
      if (service.changerPrixIngredient(nom, prix) == 0) {
        afficherPopupInformation("Prix de l'ingrédient modifié avec succès.");
        actionBoutonAfficherTousIngredients(event);
      } else {
        afficherPopupErreur("Erreur : Impossible de modifier le prix.");
      }
    } catch (NumberFormatException e) {
      afficherPopupErreur("Le prix saisi n'est pas un nombre valide.");
    }
  }

  /**
   * Modifie le prix de vente de la pizza sélectionnée.
   * Vérifie d'abord que le nouveau prix est supérieur au prix minimal calculé
   * par le service.
   * Affiche une erreur explicite si le prix est trop bas.
   *
   * @param event L'événement du clic sur le bouton.
   */
  @FXML
  void actionBoutonModifierPrixPizza(ActionEvent event) {
    try {
      int index = listePizzas.getSelectionModel().getSelectedIndex();
      if (index < 0) {
        return;
      }

      double nouveauPrix = Double.parseDouble(entreePrixVentePizza.getText());
      Pizza p = pizzasAffichees.get(index);

      if (service.setPrixPizza(p, nouveauPrix)) {
        afficherPopupInformation("Prix de vente mis à jour.");
        actionListeSelectionPizza(null);
      } else {
        afficherPopupErreur("Le prix de vente doit être supérieur au prix "
            + "minimal (" + service.calculerPrixMinimalPizza(p) + "€).");
      }
    } catch (NumberFormatException e) {
      afficherPopupErreur("Prix invalide.");
    }
  }

  /**
   * Ouvre une boîte de dialogue système pour sélectionner une image (PNG, JPG).
   * Associe l'image choisie à la pizza sélectionnée et met à jour l'affichage
   * du chemin.
   *
   * @param event L'événement du clic sur le bouton.
   */
  @FXML
  void actionBoutonParcourirPhotoPizza(ActionEvent event) {
    int index = listePizzas.getSelectionModel().getSelectedIndex();
    if (index < 0) {
      afficherPopupErreur("Sélectionnez une pizza pour lui ajouter une photo.");
      return;
    }

    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Sélectionner la photo de la pizza");
    fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
    );

    File selectedFile = fileChooser.showOpenDialog(null);
    if (selectedFile != null) {
      try {
        Pizza p = pizzasAffichees.get(index);
        String cheminPhoto = selectedFile.toURI().toString();
        if (service.ajouterPhoto(p, cheminPhoto)) {
          entreePhotoPizza.setText(cheminPhoto);
          afficherPopupInformation("Photo ajoutée avec succès !");
        }
      } catch (IOException e) {
        afficherPopupErreur("Erreur lors de l'accès au fichier : "
            + e.getMessage());
      }
    }
  }

  /**
   * Retire l'ingrédient sélectionné de la composition de la pizza.
   * Ne fonctionne que si une pizza et un ingrédient sont sélectionnés.
   * Rafraîchit les statistiques de la pizza après suppression.
   *
   * @param event L'événement du clic sur le bouton.
   */
  @FXML
  void actionBoutonSupprimerIngredientPizza(ActionEvent event) {
    int indexP = listePizzas.getSelectionModel().getSelectedIndex();
    int indexI = listeIngredients.getSelectionModel().getSelectedIndex();
    if (indexP < 0 || indexI < 0) {
      return;
    }

    Pizza p = pizzasAffichees.get(indexP);
    Ingredient ing = ingredientsAffiches.get(indexI);

    if (service.retirerIngredientPizza(p, ing.getNom()) == 0) {
      actionListeSelectionPizza(null);
    } else {
      afficherPopupErreur("Erreur lors de la suppression de l'ingrédient.");
    }
  }

  /**
   * Analyse la composition de la pizza sélectionnée pour détecter d'éventuels
   * conflits (ex: un ingrédient interdit pour ce type de pizza).
   * Met à jour la liste visuelle pour afficher uniquement les ingrédients
   * problématiques s'il y en a.
   *
   * @param event L'événement du clic sur le bouton.
   */
  @FXML
  void actionBoutonVerifierValiditeIngredientsPizza(ActionEvent event) {
    int index = listePizzas.getSelectionModel().getSelectedIndex();
    if (index < 0) {
      return;
    }

    Pizza p = pizzasAffichees.get(index);
    Set<String> invalides = service.verifierIngredientsPizza(p);

    listeIngredients.getItems().clear();
    if (invalides.isEmpty()) {
      labelListeIngredients.setText("Tous les ingrédients sont valides.");
    } else {
      listeIngredients.getItems().addAll(invalides);
      labelListeIngredients.setText("INGRÉDIENTS INVALIDES DÉTECTÉS !");
    }
  }

  /**
   * Action déclenchée lorsqu'on clique sur une ligne de la liste des commandes.
   * Affiche le bénéfice net généré par cette commande spécifique.
   *
   * @param event L'événement de la souris.
   */
  @FXML
  void actionListeSelectionCommande(MouseEvent event) {
    int index = listeCommandes.getSelectionModel().getSelectedIndex();
    if (index >= 0 && index < commandesAffichees.size()) {
      Commande c = commandesAffichees.get(index);
      entreeBeneficeCommande.setText(String.format("%.2f",
          service.beneficeCommandes(c)));
    }
  }

  /**
   * Action déclenchée lorsqu'on clique sur un ingrédient dans la liste.
   * Remplit les champs de saisie (nom, prix) pour permettre la modification.
   * Si la sélection est vide, vide les champs pour permettre une création.
   *
   * @param event L'événement de la souris.
   */
  @FXML
  void actionListeSelectionIngredient(MouseEvent event) {
    int index = listeIngredients.getSelectionModel().getSelectedIndex();

    if (index >= 0 && index < ingredientsAffiches.size()) {
      Ingredient ing = ingredientsAffiches.get(index);
      entreeNomIngredient.setText(ing.getNom());
      entreePrixIngredient.setText(String.valueOf(ing.getPrix()));
      choiceBoxTypeIngredient.setValue(null);
    } else {
      viderChampsIngredient();
    }
  }

  /**
   * Action déclenchée lorsqu'on clique sur une pizza dans la liste.
   * Met à jour tous les champs de détails (Nom, Prix, Type, Stats).
   * Recalcule dynamiquement le prix minimal et le bénéfice unitaire.
   * Met à jour la liste des ingrédients pour n'afficher que ceux de la pizza.
   *
   * @param event L'événement de la souris.
   */
  @FXML
  void actionListeSelectionPizza(MouseEvent event) {
    int index = listePizzas.getSelectionModel().getSelectedIndex();
    if (index >= 0 && index < pizzasAffichees.size()) {
      Pizza p = pizzasAffichees.get(index);

      entreeNomPizza.setText(p.getNom());
      choiceBoxTypePizza.setValue(p.getTypePizza().name());
      entreePrixVentePizza.setText(String.valueOf(p.getPrix()));

      double prixMin = service.calculerPrixMinimalPizza(p);
      entreePrixMinimalPizza.setText(String.format("%.2f", prixMin));
      entreeBeneficeUnitairePizza.setText(String.format("%.2f",
          p.getPrix() - prixMin));

      entreeNbCommandesPizza.setText(
          String.valueOf(service.nombrePizzasCommandees(p)));
      entreeBeneficeTotalPizza.setText(String.format("%.2f",
          (p.getPrix() - prixMin) * service.nombrePizzasCommandees(p)));

      listeIngredients.getItems().clear();
      ingredientsAffiches.clear();
      for (Ingredient ing : p.getIngredients()) {
        ingredientsAffiches.add(ing);
        listeIngredients.getItems().add(ing.getNom() + " ("
            + ing.getPrix() + "€)");
      }
      labelListeIngredients.setText("Ingrédients de la pizza : " + p.getNom());
    }
  }

  /**
   * Affiche une popup d'information avec les crédits de l'application.
   *
   * @param event L'événement du menu.
   */
  @FXML
  void actionMenuApropos(ActionEvent event) {
    afficherPopupInformation("Gestion Pizzeria v1.0\nL3 Informatique UBO");
  }

  /**
   * Charge manuellement l'état de l'application depuis le fichier de sauvegarde.
   * Rafraîchit ensuite l'ensemble de l'interface (Pizzas, Ingrédients, Clients).
   *
   * @param event L'événement du menu.
   */
  @FXML
  void actionMenuCharger(ActionEvent event) {
    try {
      ServiceSauvegarde ss = new ServiceSauvegarde(service.getMenu());
      ss.chargerDonnees("donnees_pizzeria.ser");

      actionBoutonAfficherToutesPizzas(null);
      actionBoutonAfficherTousIngredients(null);
      rafraichirClients();

      afficherPopupInformation("Données chargées manuellement.");
    } catch (IOException e) {
      afficherPopupErreur("Erreur de chargement : " + e.getMessage());
    }
  }

  /**
   * Ferme l'application proprement.
   *
   * @param event L'événement du menu.
   */
  @FXML
  void actionMenuQuitter(ActionEvent event) {
    Platform.exit();
  }

  /**
   * Sauvegarde manuellement l'état actuel du menu dans un fichier.
   *
   * @param event L'événement du menu.
   */
  @FXML
  void actionMenuSauvegarder(ActionEvent event) {
    try {
      ServiceSauvegarde ss = new ServiceSauvegarde(service.getMenu());
      ss.sauvegarderDonnees("donnees_pizzeria.ser");
      afficherPopupInformation("Données sauvegardées manuellement.");
    } catch (IOException e) {
      afficherPopupErreur("Erreur de sauvegarde : " + e.getMessage());
    }
  }

  /**
   * Action déclenchée lors de la sélection d'un client dans la liste déroulante.
   * Met à jour les statistiques spécifiques à ce client (Nombre de pizzas
   * achetées, bénéfice généré).
   *
   * @param event L'événement de la ComboBox.
   */
  @FXML
  void actionSelectionClient(ActionEvent event) {
    String clientStr = comboBoxClients.getValue();
    if (clientStr == null) {
      return;
    }

    for (InformationPersonnelle info : service.ensembleClients()) {
      if ((info.getNom() + " " + info.getPrenom()).equals(clientStr)) {
        entreeNbPizzasClient.setText(String.valueOf(service
            .nombrePizzasCommandeesParClient().getOrDefault(info, 0)));
        entreeBeneficeClient.setText(String.format("%.2f",
            service.beneficeParClient().getOrDefault(info, 0.0)));
        break;
      }
    }
  }

  /**
   * Injecte le service métier dans le contrôleur.
   * Cette méthode est appelée lors de l'initialisation par MainInterface.
   * Elle déclenche un premier rafraîchissement de la liste des clients.
   *
   * @param s L'instance du ServicePizzaiolo.
   */
  public void setServices(ServicePizzaiolo s) {
    this.service = s;
    rafraichirClients();
  }

  /**
   * Met à jour la liste déroulante (ComboBox) contenant les noms des clients.
   * Cette méthode est appelée au démarrage et après chaque traitement de
   * nouvelles commandes (pour inclure les nouveaux inscrits).
   */
  private void rafraichirClients() {
    if (comboBoxClients != null && service != null) {
      comboBoxClients.getItems().clear();
      for (InformationPersonnelle info : service.ensembleClients()) {
        comboBoxClients.getItems().add(info.getNom() + " " + info.getPrenom());
      }
    }
  }

  /**
   * Affiche une fenêtre surgie (Popup) générique.
   *
   * @param message Le message à afficher dans la fenêtre.
   * @param type Le type d'alerte (Erreur, Information, etc.).
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
   * Raccourci pour afficher une popup d'erreur.
   *
   * @param message Le message d'erreur.
   */
  private void afficherPopupErreur(String message) {
    this.afficherPopup(message, AlertType.ERROR);
  }

  /**
   * Raccourci pour afficher une popup d'information.
   *
   * @param message Le message informatif.
   */
  private void afficherPopupInformation(String message) {
    this.afficherPopup(message, AlertType.INFORMATION);
  }

  /**
   * Réinitialise les champs de saisie pour la création d'ingrédient.
   * Vide le nom, le prix, et désélectionne les éléments dans les listes.
   */
  private void viderChampsIngredient() {
    entreeNomIngredient.clear();
    entreePrixIngredient.clear();

    listeIngredients.getSelectionModel().clearSelection();

    if (choiceBoxTypeIngredient != null) {
      choiceBoxTypeIngredient.setValue(null);
      choiceBoxTypeIngredient.getSelectionModel().clearSelection();
    }

    labelListeIngredients.setText("Saisie d'un nouvel ingrédient");
  }

  /**
   * Méthode d'initialisation appelée automatiquement par JavaFX.
   * Remplit les listes déroulantes avec les valeurs de l'énumération TypePizza.
   * Ajoute une option 'null' pour permettre la désélection.
   */
  @FXML
  void initialize() {
    if (choiceBoxTypeIngredient != null) {
      choiceBoxTypeIngredient.getItems().add(null);
      for (TypePizza t : TypePizza.values()) {
        choiceBoxTypeIngredient.getItems().add(t.name());
      }
      choiceBoxTypeIngredient.setValue(null);
    }
    if (choiceBoxTypePizza != null) {
      for (TypePizza t : TypePizza.values()) {
        choiceBoxTypePizza.getItems().add(t.name());
      }
    }
  }
}