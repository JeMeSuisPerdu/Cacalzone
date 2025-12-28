package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * Controleur JavaFX de la fenêtre du pizzaïolo.
 *
 * @author Eric Cariou
 */
public class PizzaioloControleur {
  
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

  @FXML
  void actionBoutonAfficherListeTrieePizzas(ActionEvent event) {

  }

  @FXML
  void actionBoutonAfficherTousIngredients(ActionEvent event) {

  }

  @FXML
  void actionBoutonAfficherToutesPizzas(ActionEvent event) {

  }

  @FXML
  void actionBoutonAjouterIngredientPizza(ActionEvent event) {

  }

  @FXML
  void actionBoutonCommandesDejaTraitees(ActionEvent event) {

  }

  @FXML
  void actionBoutonCommandesNonTraitees(ActionEvent event) {

  }

  @FXML
  void actionBoutonCommandesTraiteesClient(ActionEvent event) {

  }

  @FXML
  void actionBoutonCreerIngredient(ActionEvent event) {

  }

  @FXML
  void actionBoutonCreerPizza(ActionEvent event) {

  }

  @FXML
  void actionBoutonInterdireIngredient(ActionEvent event) {

  }

  @FXML
  void actionBoutonModifierPrixIngredient(ActionEvent event) {

  }

  @FXML
  void actionBoutonModifierPrixPizza(ActionEvent event) {

  }

  @FXML
  void actionBoutonParcourirPhotoPizza(ActionEvent event) {

  }

  @FXML
  void actionBoutonSupprimerIngredientPizza(ActionEvent event) {

  }

  @FXML
  void actionBoutonVerifierValiditeIngredientsPizza(ActionEvent event) {

  }

  @FXML
  void actionListeSelectionCommande(MouseEvent event) {

  }

  @FXML
  void actionListeSelectionIngredient(MouseEvent event) {

  }

  @FXML
  void actionListeSelectionPizza(MouseEvent event) {

  }

  @FXML
  void actionMenuApropos(ActionEvent event) {

  }

  @FXML
  void actionMenuCharger(ActionEvent event) {

  }

  @FXML
  void actionMenuQuitter(ActionEvent event) {

  }

  @FXML
  void actionMenuSauvegarder(ActionEvent event) {

  }

  @FXML
  void actionSelectionClient(ActionEvent event) {

  }

  
  @FXML
  void initialize() {
    // Mettre ici le code d'initialisation du contenu de la fenêtre
  }
}
