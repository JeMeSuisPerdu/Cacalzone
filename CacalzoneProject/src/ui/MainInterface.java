package ui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pizzas.Menu;
import pizzas.ServiceClient;
import pizzas.ServicePizzaiolo;
import pizzas.ServiceSauvegarde;

public final class MainInterface extends Application {
  
  // --- LES SERVICES ---
  private Menu menu;
  private ServiceClient serviceClient;
  private ServicePizzaiolo servicePizzaiolo;
  private ServiceSauvegarde serviceSauvegarde;

  /**
   * Initialisation des services avant le lancement des fenêtres.
   */
  @Override
  public void init() {
    this.menu = new Menu();
    this.serviceClient = new ServiceClient(menu);
    this.servicePizzaiolo = new ServicePizzaiolo(menu);
    this.serviceSauvegarde = new ServiceSauvegarde(menu);
    
    try {
        File fichier = new File("donnees_pizzeria.ser");
        if (fichier.exists()) {
            serviceSauvegarde.chargerDonnees("donnees_pizzeria.ser");
            System.out.println("Données restaurées depuis le fichier.");
        } else {
            System.out.println("Aucune sauvegarde trouvée, démarrage avec un menu neuf.");
        }
    } catch (Exception e) {
        System.err.println("Erreur lors du chargement automatique : " + e.getMessage());
    }
  }

  public void startFenetreClient() {
    try {
      URL url = getClass().getResource("client.fxml");
      FXMLLoader fxmlLoader = new FXMLLoader(url);
      VBox root = (VBox) fxmlLoader.load();
      
      ClientControleur ctrl = fxmlLoader.getController();
      ctrl.setServices(serviceClient);
      
      Scene scene = new Scene(root, 1210, 620);
      Stage stage = new Stage();
      stage.setResizable(true);
      stage.setTitle("Commandes de pizzas");
      stage.setScene(scene);
      stage.show();
      
    } catch (IOException e) {
      System.err.println("Erreur au chargement de la fenêtre du client : " + e);
    }
  }
  
  public void startFenetrePizzaiolo(Stage primaryStage) {
    try {
      URL url = getClass().getResource("pizzaiolo.fxml");
      FXMLLoader fxmlLoader = new FXMLLoader(url);
      VBox root = (VBox) fxmlLoader.load();
      
      PizzaioloControleur ctrl = fxmlLoader.getController();
      ctrl.setServices(servicePizzaiolo);
      
      Scene scene = new Scene(root, 985, 630);
      primaryStage.setScene(scene);
      primaryStage.setResizable(true);
      primaryStage.setTitle("Gestion des pizzas");
      primaryStage.show();
      
    } catch (IOException e) {
      System.err.println("Erreur au chargement de la fenêtre du pizzaïolo : " + e);
    }
  }
  
  @Override
  public void start(Stage primaryStage) {
    this.startFenetrePizzaiolo(primaryStage);
    this.startFenetreClient();
  }
  
  @Override
  public void stop() {
      try {
          if (this.serviceSauvegarde != null) {
              serviceSauvegarde.sauvegarderDonnees("donnees_pizzeria.ser");
              System.out.println("Sauvegarde automatique réussie !");
          }
      } catch (Exception e) {
          e.printStackTrace();
      }
  }
  
  public static void main(String[] args) {
    launch(args);
  }
  
}