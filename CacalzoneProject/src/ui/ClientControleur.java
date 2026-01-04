package ui;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import pizzas.ServiceClient;
import pizzas.TypePizza;
import pizzas.Pizza;
import pizzas.Commande;
import pizzas.Evaluation;
import pizzas.InformationPersonnelle;
import pizzas.Ingredient;


/**
 * Controleur JavaFX de la fenêtre du client.
 *
 * @author Eric Cariou
 */
public class ClientControleur {
	
  private ServiceClient service;
  private java.util.List<pizzas.Commande> commandesAffichees = new java.util.ArrayList<>();
  private java.util.List<pizzas.Evaluation> evaluationsAffichees = new java.util.ArrayList<>();
  
  private void mettreAJourListePizzasFiltrees(String message) {
	    listePizzas.getItems().clear();
	    for (pizzas.Pizza p : service.selectionPizzaFiltres()) {
	        listePizzas.getItems().add(p.getNom() + " - " + p.getPrix() + "€");
	    }
	    labelListePizzas.setText(message);
  }
  
  
  @FXML
  private ChoiceBox<String> choiceBoxFiltreType;
  
  @FXML
  private TextField entreeAdresseClient;
  
  @FXML
  private TextField entreeAgeClient;
  
  @FXML
  private TextField entreeAuteurEvaluation;
  
  @FXML
  private TextField entreeEmailClient;
  
  @FXML
  private TextField entreeEvaluationMoyenneEvaluations;
  
  @FXML
  private TextField entreeFiltreContientIngredient;
  
  @FXML
  private TextField entreeFiltrePrixMax;
  
  @FXML
  private TextField entreeMotDePasseClient;
  
  @FXML
  private TextField entreeNomClient;
  
  @FXML
  private TextField entreeNomPizza;
  
  @FXML
  private TextField entreeNomPizzaEvaluee;
  
  @FXML
  private TextField entreeNoteMoyennePizza;
  
  @FXML
  private TextField entreePrenomClient;
  
  @FXML
  private TextField entreePrixPizza;
  
  @FXML
  private TextField entreeTypePizza;
  
  @FXML
  private Label labelListeCommandes;
  
  @FXML
  private Label labelListePizzas;
  
  @FXML
  private ListView<String> listeCommandes;
  
  @FXML
  private ListView<String> listeEvaluations;
  
  @FXML
  private ListView<String> listeIngredients;
  
  @FXML
  private ChoiceBox<Integer> choiceBoxNoteEvaluation;
  
  @FXML
  private ListView<String> listePizzas;
  
  @FXML
  private StackPane panePhotoPizza;
  
  @FXML
  private TextArea texteCommentaireEvaluation;
  //FAIT
  @FXML
  void actionBoutonAfficherCommandesEnCours(ActionEvent event) {
	  try {
	        listeCommandes.getItems().clear();
	        this.commandesAffichees = service.getCommandesEncours();
	        
	        if (commandesAffichees.isEmpty()) {
	            labelListeCommandes.setText("Aucune commande en cours.");
	        } else {
	            for (Commande c : commandesAffichees) {
	                listeCommandes.getItems().add("Commande du " + c.getDate() + " [" + c.getEtat() + "]");
	            }
	            labelListeCommandes.setText("Vos commandes en cours");
	        }
	    } catch (Exception e) {
	        afficherPopupErreur("Erreur : " + e.getMessage());
	    }
  }
  //FAIT
  @FXML
  void actionBoutonAfficherCommandesTtraitees(ActionEvent event) {
	  try {
	        listeCommandes.getItems().clear();
	        this.commandesAffichees.clear();
	        
	        for (Commande c : service.getCommandePassees()) {
	            if (c.getEtat() == Commande.Etat.TRAITEE) {
	                this.commandesAffichees.add(c);
	                listeCommandes.getItems().add("Commande traitée du " + c.getDate() + " (" + c.getPrixTotal() + "€)");
	            }
	        }
	        labelListeCommandes.setText("Historique des commandes traitées");
	    } catch (Exception e) {
	        afficherPopupErreur("Vous devez être connecté.");
	    }
  }
  //FAIT
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
	        entreeEvaluationMoyenneEvaluations.setText(String.format("%.2f", service.getNoteMoyenne(p)));
	        
	        listeEvaluations.getItems().clear();
	        this.evaluationsAffichees = new ArrayList<>(service.getEvaluationsPizza(p));
	        
	        for (Evaluation ev : evaluationsAffichees) {
	            listeEvaluations.getItems().add(ev.getNote() + "/5 par " + ev.getEmailClient());
	        }
	    }
  }
  //FAIT
  @FXML
  void actionBoutonAfficherToutesPizzas(ActionEvent event) {
	  listePizzas.getItems().clear();
	    for (Pizza p : service.getPizzas()) {
	        listePizzas.getItems().add(p.getNom() + " - " + p.getPrix() + "€");
	    }
	    labelListePizzas.setText("Toutes les pizzas du menu");
  }
  //FAIT
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
	        if(service.ajouterEvaluation(p, note, commentaire)) {
		        afficherPopupInformation("Merci ! Votre évaluation a été ajoutée.");
	        }else {
		        afficherPopupErreur("ERREUR : Commentaire non ajouté.");
	        }
	        
	        double nouvelleNote = service.getNoteMoyenne(p);
	        entreeNoteMoyennePizza.setText(String.format("%.2f", nouvelleNote));
	        
	        texteCommentaireEvaluation.clear();
	    } catch (Exception e) {
	        afficherPopupErreur("Vous devez être connecté pour évaluer une pizza.");
	    }
  }
  //FAIT
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
	            afficherPopupErreur("Veuillez d'abord cliquer sur 'Créer nouvelle commande'.");
	            return;
	        }

	        service.ajouterPizza(p, 1, c);
	        
	        listeCommandes.getItems().add(p.getNom());
	        afficherPopupInformation(p.getNom() + " ajoutée au panier.");
	    } catch (Exception e) {
	        afficherPopupErreur("Erreur : " + e.getMessage());
	    }
  }
 //FAIT 
  @FXML
  void actionBoutonAppliquerFiltreContientngredient(ActionEvent event) {
	  String ingredient = entreeFiltreContientIngredient.getText();
	    if (ingredient != null && !ingredient.isEmpty()) {
	        service.ajouterFiltre(ingredient);
	        mettreAJourListePizzasFiltrees("Filtre : Contient " + ingredient);
	    }
  }
  //FAIT
  @FXML
  void actionBoutonAppliquerFiltrePrixMax(ActionEvent event) {
	  try {
	        if (!entreeFiltrePrixMax.getText().isEmpty()) {
	            double prix = Double.parseDouble(entreeFiltrePrixMax.getText());
	            service.ajouterFiltre(prix);
	            mettreAJourListePizzasFiltrees("Filtre : Max " + prix + "€");
	        }
	    } catch (NumberFormatException e) {
	        afficherPopupErreur("Prix invalide.");
	    }
  }
  //FAIT
  @FXML
  void actionBoutonAppliquerFiltreType(ActionEvent event) {
	  String typeStr = choiceBoxFiltreType.getValue();
	    if (typeStr != null) {
	        service.ajouterFiltre(TypePizza.valueOf(typeStr));
	        mettreAJourListePizzasFiltrees("Filtre : Type " + typeStr);
	    }
  }
  //FAIT
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
  //FAIT
  @FXML
  void actionBoutonCreerNouvelleCommande(ActionEvent event) {
	  try {
          service.debuterCommande();
          afficherPopupInformation("Nouvelle commande créée. Vous pouvez ajouter des pizzas.");
          labelListeCommandes.setText("Commande en cours...");
          listeCommandes.getItems().clear();
      } catch (Exception e) {
          afficherPopupErreur("ERREUR : " + e.getMessage());
      }
  }
  //FAIT
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
  //FAIT
  @FXML
  void actionBoutonInscription(ActionEvent event) {
      String email = entreeEmailClient.getText();
      String mdp = entreeMotDePasseClient.getText();
      String nom = entreeNomClient.getText();
      String prenom = entreePrenomClient.getText();
      String adresse = entreeAdresseClient.getText();
      String ageBrut = entreeAgeClient.getText();

      if (email.isEmpty() || mdp.isEmpty() || nom.isEmpty() || prenom.isEmpty() || adresse.isEmpty() || ageBrut.isEmpty()) {
          afficherPopupErreur("Veuillez remplir tous les champs du formulaire.");
          return;
      }

      try {
          int age = Integer.parseInt(ageBrut);

          InformationPersonnelle infos = new InformationPersonnelle(nom, prenom, adresse, age);
          int res = service.inscription(email, mdp, infos);

          if (res == 0) {
              afficherPopupInformation("Inscription réussie ! Vous pouvez vous connecter.");
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
  //FAIT
  @FXML
  void actionBoutonReinitialiserFiltre(ActionEvent event) {
	  service.supprimerFiltres();
	    entreeFiltrePrixMax.clear();
	    entreeFiltreContientIngredient.clear();
	    choiceBoxFiltreType.setValue(null);
	    actionBoutonAfficherToutesPizzas(event);
  }
  //FAIT
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
  //FAIT
  @FXML
  void actionSelectionCommnade(MouseEvent event) {
	  int index = listeCommandes.getSelectionModel().getSelectedIndex();
	    if (index >= 0 && index < commandesAffichees.size()) {
	        Commande c = commandesAffichees.get(index);
	        
	        listePizzas.getItems().clear();
	        for (java.util.Map.Entry<Pizza, Integer> entry : c.getPizzas().entrySet()) {
	            Pizza p = entry.getKey();
	            int qte = entry.getValue();
	            listePizzas.getItems().add(qte + " x " + p.getNom() + " (" + p.getPrix() + "€)");
	        }
	        labelListePizzas.setText("Contenu de la commande sélectionnée");
	    }
  }
  //FAIT
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
  //FAIT
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
  //FAIT
  public void setServices(ServiceClient s) {
	 this.service = s;
  }
  //FAIT
  private void afficherPopup(String message, javafx.scene.control.Alert.AlertType type) {
	    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(type);
	    if (type == javafx.scene.control.Alert.AlertType.ERROR) {
	        alert.setTitle("Erreur");
	    } else {
	        alert.setTitle("Information");
	    }
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.setResizable(true);
	    alert.showAndWait();
  }
  //FAIT
  private void afficherPopupErreur(String message) {
	    this.afficherPopup(message, javafx.scene.control.Alert.AlertType.ERROR);
  }
  //FAIT
  private void afficherPopupInformation(String message) {
	    this.afficherPopup(message, javafx.scene.control.Alert.AlertType.INFORMATION);
  }
  
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
