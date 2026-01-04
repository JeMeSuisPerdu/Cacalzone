package ui;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import pizzas.ServicePizzaiolo;
import pizzas.TypePizza;
import pizzas.Ingredient;
import pizzas.Pizza;
import pizzas.Commande;
import pizzas.InformationPersonnelle;


/**
 * Controleur JavaFX de la fenêtre du pizzaïolo.
 *
 * @author Eric Cariou
 */
public class PizzaioloControleur {
  // --- ATTRIBUTS DE GESTION ---
  private ServicePizzaiolo service;  

  private List<Ingredient> ingredientsAffiches = new ArrayList<>();
  private List<Pizza> pizzasAffichees = new ArrayList<>();
  private List<Commande> commandesAffichees = new ArrayList<>();  
  
  @FXML
  private ChoiceBox<String> choiceBoxTypeIngredient;

  @FXML
  private ChoiceBox<String> choiceBoxTypePizza;

  @FXML
  private ComboBox<String> comboBoxClients;

  @FXML
  private TextField entreeBeneficeClient;

  @FXML
  private TextField entreeBeneficeCommande;

  @FXML
  private TextField entreeBeneficeTotalCommandes;

  @FXML
  private TextField entreeBeneficeTotalPizza;

  @FXML
  private TextField entreeBeneficeUnitairePizza;

  @FXML
  private TextField entreeNbCommandesPizza;

  @FXML
  private TextField entreeNbPizzasClient;

  @FXML
  private TextField entreeNomIngredient;

  @FXML
  private TextField entreeNomPizza;

  @FXML
  private TextField entreeNombreTotalCommandes;

  @FXML
  private TextField entreePhotoPizza;

  @FXML
  private TextField entreePrixIngredient;

  @FXML
  private TextField entreePrixMinimalPizza;

  @FXML
  private TextField entreePrixVentePizza;

  @FXML
  private Label labelListeCommandes;

  @FXML
  private Label labelListeIngredients;

  @FXML
  private Label labelListePizzas;

  @FXML
  private ListView<String> listeCommandes;

  @FXML
  private ListView<String> listeIngredients;

  @FXML
  private ListView<String> listePizzas;
  
  //FAIT
  @FXML
  void actionBoutonAfficherListeTrieePizzas(ActionEvent event) {
	  listePizzas.getItems().clear();
      pizzasAffichees = service.classementPizzasParNombreCommandes();
      for (Pizza p : pizzasAffichees) {
          listePizzas.getItems().add(p.getNom() + " (" + service.nombrePizzasCommandees(p) + " ventes)");
      }
      labelListePizzas.setText("Pizzas triées par succès");
  }
  //FAIT
  @FXML
  void actionBoutonAfficherTousIngredients(ActionEvent event) {
	  listeIngredients.getItems().clear();
      ingredientsAffiches.clear();
      for (Ingredient ing : service.getMenu().getIngredients()) {
          ingredientsAffiches.add(ing);
          listeIngredients.getItems().add(ing.getNom() + " - " + ing.getPrix() + "€" + " - " + "Interdit : " + ing.getTypesPizzaInterdits().toString());
      }
      labelListeIngredients.setText("Tous les ingrédients disponibles");
  }
  //FAIT
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
  //FAIT
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
          entreePrixMinimalPizza.setText(String.format("%.2f", service.calculerPrixMinimalPizza(p)));
          actionListeSelectionPizza(null); 
      } else {
          afficherPopupErreur("Ajout impossible (Ingrédient interdit pour ce type).");
      }
  }

  @FXML
  void actionBoutonCommandesDejaTraitees(ActionEvent event) {
	  listeCommandes.getItems().clear();
      commandesAffichees = service.commandesDejaTraitees();
      for (Commande c : commandesAffichees) {
          listeCommandes.getItems().add("Commande du " + c.getDate() + " (" + c.getClient().getInfos().getNom() + ")");
      }
      labelListeCommandes.setText("Historique de toutes les commandes traitées");
  }
  //FAIT
  @FXML
  void actionBoutonCommandesNonTraitees(ActionEvent event) {
	  listeCommandes.getItems().clear();
      commandesAffichees = service.commandeNonTraitees();
      for (Commande c : commandesAffichees) {
          listeCommandes.getItems().add("Commande de " + c.getClient().getInfos().getNom());
      }
      labelListeCommandes.setText("Commandes venant d'être traitées");
      entreeBeneficeTotalCommandes.setText(String.format("%.2f", service.beneficeToutesCommandes()));
      rafraichirClients();
  }

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
              listeCommandes.getItems().add("Commande du " + c.getDate() + " [" + c.getPrixTotal() + "€]");
          }
          labelListeCommandes.setText("Commandes traitées de " + clientStr);
      }
  }
  //FAIT
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
              afficherPopupErreur("Erreur : Ingrédient déjà présent ou données invalides.");
          }
      } catch (NumberFormatException e) {
          afficherPopupErreur("Le prix doit être un nombre.");
      }
  }
  //FAIT
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
  //FAIT
  @FXML
  void actionBoutonInterdireIngredient(ActionEvent event) {
	  String nom = entreeNomIngredient.getText();
      String typeStr = choiceBoxTypeIngredient.getValue();
      if (nom.isEmpty() || typeStr == null) {
          afficherPopupErreur("Sélectionnez un ingrédient et un type.");
          return;
      }
	  if (service.interdireIngredient(nom, TypePizza.valueOf(typeStr))) {
	      afficherPopupInformation(nom + " est maintenant interdit pour le type " + typeStr);
	  } else {
	      afficherPopupErreur("Erreur lors de l'ajout de l'interdiction.");
	  }
  }

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

  @FXML
  void actionBoutonModifierPrixPizza(ActionEvent event) {
	  try {
          int index = listePizzas.getSelectionModel().getSelectedIndex();
          if (index < 0) return;
          
          double nouveauPrix = Double.parseDouble(entreePrixVentePizza.getText());
          Pizza p = pizzasAffichees.get(index);
          
          if (service.setPrixPizza(p, nouveauPrix)) {
              afficherPopupInformation("Prix de vente mis à jour.");
              actionListeSelectionPizza(null);
          } else {
              afficherPopupErreur("Le prix de vente doit être supérieur au prix minimal (" + service.calculerPrixMinimalPizza(p) + "€).");
          }
      } catch (NumberFormatException e) {
          afficherPopupErreur("Prix invalide.");
      }
  }

  @FXML
  void actionBoutonParcourirPhotoPizza(ActionEvent event) {
	  int index = listePizzas.getSelectionModel().getSelectedIndex();
      if (index < 0) {
          afficherPopupErreur("Sélectionnez une pizza pour lui ajouter une photo.");
          return;
      }

      javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
      fileChooser.setTitle("Sélectionner la photo de la pizza");
      fileChooser.getExtensionFilters().addAll(
          new javafx.stage.FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
      );
      
      java.io.File selectedFile = fileChooser.showOpenDialog(null);
      if (selectedFile != null) {
          try {
              Pizza p = pizzasAffichees.get(index);
              String cheminPhoto = selectedFile.toURI().toString();
              if (service.ajouterPhoto(p, cheminPhoto)) {
                  entreePhotoPizza.setText(cheminPhoto);
                  afficherPopupInformation("Photo ajoutée avec succès !");
              }
          } catch (java.io.IOException e) {
              afficherPopupErreur("Erreur lors de l'accès au fichier : " + e.getMessage());
          }
      }
  }

  @FXML
  void actionBoutonSupprimerIngredientPizza(ActionEvent event) {
	  int indexP = listePizzas.getSelectionModel().getSelectedIndex();
      int indexI = listeIngredients.getSelectionModel().getSelectedIndex();
      if (indexP < 0 || indexI < 0) return;

      Pizza p = pizzasAffichees.get(indexP);
      Ingredient ing = ingredientsAffiches.get(indexI);
      
      if (service.retirerIngredientPizza(p, ing.getNom()) == 0) {
          actionListeSelectionPizza(null);
      } else {
          afficherPopupErreur("Erreur lors de la suppression de l'ingrédient.");
      }
  }

  @FXML
  void actionBoutonVerifierValiditeIngredientsPizza(ActionEvent event) {
	  int index = listePizzas.getSelectionModel().getSelectedIndex();
      if (index < 0) return;
      
      Pizza p = pizzasAffichees.get(index);
      java.util.Set<String> invalides = service.verifierIngredientsPizza(p);
      
      listeIngredients.getItems().clear();
      if (invalides.isEmpty()) {
          labelListeIngredients.setText("Tous les ingrédients sont valides.");
      } else {
          listeIngredients.getItems().addAll(invalides);
          labelListeIngredients.setText("INGRÉDIENTS INVALIDES DÉTECTÉS !");
      }
  }

  @FXML
  void actionListeSelectionCommande(MouseEvent event) {
	  int index = listeCommandes.getSelectionModel().getSelectedIndex();
      if (index >= 0 && index < commandesAffichees.size()) {
          Commande c = commandesAffichees.get(index);
          entreeBeneficeCommande.setText(String.format("%.2f", service.beneficeCommandes(c)));
      }
  }
  //FAIT
  @FXML
  void actionListeSelectionIngredient(MouseEvent event) {
	    Object source = event.getTarget();
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
          entreeBeneficeUnitairePizza.setText(String.format("%.2f", p.getPrix() - prixMin));
          
          entreeNbCommandesPizza.setText(String.valueOf(service.nombrePizzasCommandees(p)));
          entreeBeneficeTotalPizza.setText(String.format("%.2f", (p.getPrix() - prixMin) * service.nombrePizzasCommandees(p)));
          
          listeIngredients.getItems().clear();
          ingredientsAffiches.clear();
          for (Ingredient ing : p.getIngredients()) {
              ingredientsAffiches.add(ing);
              listeIngredients.getItems().add(ing.getNom() + " (" + ing.getPrix() + "€)");
          }
          labelListeIngredients.setText("Ingrédients de la pizza : " + p.getNom());
      }
  }

  @FXML
  void actionMenuApropos(ActionEvent event) {
	  afficherPopupInformation("Gestion Pizzeria v1.0\nL3 Informatique UBO");
  }

  @FXML
  void actionMenuCharger(ActionEvent event) {
	  try {
          pizzas.ServiceSauvegarde ss = new pizzas.ServiceSauvegarde(service.getMenu());
          ss.chargerDonnees("donnees_pizzeria.ser");
          
          actionBoutonAfficherToutesPizzas(null);
          actionBoutonAfficherTousIngredients(null);
          rafraichirClients();
          
          afficherPopupInformation("Données chargées manuellement.");
      } catch (java.io.IOException e) {
          afficherPopupErreur("Erreur de chargement : " + e.getMessage());
      }
  }

  @FXML
  void actionMenuQuitter(ActionEvent event) {
	  javafx.application.Platform.exit();
  }

  @FXML
  void actionMenuSauvegarder(ActionEvent event) {
	  try {
          pizzas.ServiceSauvegarde ss = new pizzas.ServiceSauvegarde(service.getMenu());
          ss.sauvegarderDonnees("donnees_pizzeria.ser");
          afficherPopupInformation("Données sauvegardées manuellement.");
      } catch (java.io.IOException e) {
          afficherPopupErreur("Erreur de sauvegarde : " + e.getMessage());
      }
  }

  @FXML
  void actionSelectionClient(ActionEvent event) {
	  String clientStr = comboBoxClients.getValue();
      if (clientStr == null) return;

      for (InformationPersonnelle info : service.ensembleClients()) {
          if ((info.getNom() + " " + info.getPrenom()).equals(clientStr)) {
              entreeNbPizzasClient.setText(String.valueOf(service.nombrePizzasCommandeesParClient().getOrDefault(info, 0)));
              entreeBeneficeClient.setText(String.format("%.2f", service.beneficeParClient().getOrDefault(info, 0.0)));
              break;
          }
      }
  }
  //FAIT
  public void setServices(ServicePizzaiolo s) {
	this.service = s;
	rafraichirClients();
  }
  //FAIT
  private void rafraichirClients() {
      if (comboBoxClients != null && service != null) {
          comboBoxClients.getItems().clear();
          for (InformationPersonnelle info : service.ensembleClients()) {
              comboBoxClients.getItems().add(info.getNom() + " " + info.getPrenom());
          }
      }
  }  
  //FAIT
  private void afficherPopup(String message, javafx.scene.control.Alert.AlertType type) {
      javafx.scene.control.Alert alert = new javafx.scene.control.Alert(type);
      if (type == javafx.scene.control.Alert.AlertType.ERROR) alert.setTitle("Erreur");
      else alert.setTitle("Information");
      alert.setHeaderText(null);
      alert.setContentText(message);
      alert.setResizable(true);
      alert.showAndWait();
  }
  //FAIT
  private void afficherPopupErreur(String message) { this.afficherPopup(message, javafx.scene.control.Alert.AlertType.ERROR); }
  //FAIT
  private void afficherPopupInformation(String message) { this.afficherPopup(message, javafx.scene.control.Alert.AlertType.INFORMATION); }

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
  //FAIT
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
