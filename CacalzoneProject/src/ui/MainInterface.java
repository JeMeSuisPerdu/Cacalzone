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

/**
 * Classe principale de l'application de gestion de pizzeria.
 * Elle hérite de `Application` (JavaFX) et sert de point d'entrée.
 * Son rôle est d'orchestrer le cycle de vie de l'application :
 * - Initialiser le modèle de données (Menu) et les services métier.
 * - Charger les données persistantes au démarrage.
 * - Lancer les deux interfaces graphiques (Client et Pizzaïolo).
 * - Sauvegarder les données à la fermeture de l'application.
 */
public final class MainInterface extends Application {

  /**
   * Le modèle de données central qui contient toutes les informations
   * (pizzas, ingrédients, clients, commandes). Il est partagé entre les
   * différents services.
   */
  private Menu menu;

  /**
   * Service métier dédié à la gestion des interactions côté client
   * (commande, filtre, inscription).
   */
  private ServiceClient serviceClient;

  /**
   * Service métier dédié à la gestion des interactions côté pizzaïolo
   * (création pizzas, traitement commandes, statistiques).
   */
  private ServicePizzaiolo servicePizzaiolo;

  /**
   * Service technique gérant la persistance des données (sauvegarde/chargement)
   * via la sérialisation Java.
   */
  private ServiceSauvegarde serviceSauvegarde;

  /**
   * Méthode d'initialisation du cycle de vie JavaFX appelée avant start().
   * Instancie le modèle central (Menu) et injecte ce modèle dans les
   * constructeurs des différents services.
   * Tente ensuite de vérifier l'existence du fichier de sauvegarde
   * "donnees_pizzeria.ser" pour restaurer l'état précédent.
   * Si le fichier n'existe pas ou est corrompu, le menu reste vide (neuf).
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
        System.out.println("Aucune sauvegarde, démarrage avec menu neuf.");
      }
    } catch (Exception e) {
      System.err.println("Erreur chargement automatique : " + e.getMessage());
    }
  }

  /**
   * Charge le fichier FXML de l'interface client et l'affiche dans une
   * nouvelle fenêtre (Stage).
   * Récupère le contrôleur associé au FXML et lui injecte le `ServiceClient`
   * pour qu'il puisse effectuer les traitements métier.
   * En cas d'erreur (fichier FXML introuvable ou invalide), l'exception est
   * capturée et affichée dans la console d'erreur.
   */
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
      System.err.println("Erreur fenêtre client : " + e);
    }
  }

  /**
   * Charge le fichier FXML de l'interface pizzaïolo et l'affiche dans la
   * fenêtre principale (PrimaryStage) fournie par JavaFX.
   * Récupère le contrôleur associé et lui injecte le `ServicePizzaiolo`.
   * En cas d'erreur de chargement, une trace est affichée dans la console.
   *
   * @param primaryStage La fenêtre principale de l'application.
   */
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
      System.err.println("Erreur fenêtre pizzaïolo : " + e);
    }
  }

  /**
   * Point d'entrée graphique principal de l'application JavaFX.
   * Cette méthode est appelée automatiquement après `init()`.
   * Elle lance l'ouverture simultanée des deux interfaces :
   * 1. La fenêtre du Pizzaïolo (sur le thread principal).
   * 2. La fenêtre du Client (dans une fenêtre séparée).
   *
   * @param primaryStage La scène principale fournie par le système.
   */
  @Override
  public void start(Stage primaryStage) {
    this.startFenetrePizzaiolo(primaryStage);
    this.startFenetreClient();
  }

  /**
   * Méthode appelée automatiquement à la fermeture de l'application.
   * Utilise le `ServiceSauvegarde` pour persister l'état complet du menu
   * dans le fichier "donnees_pizzeria.ser".
   * Cela garantit que les données ne sont pas perdues entre deux exécutions.
   */
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

  /**
   * Point d'entrée standard du programme Java.
   * Appelle `launch()` pour démarrer le cycle de vie de l'application JavaFX.
   *
   * @param args Arguments de la ligne de commande (non utilisés ici).
   */
  public static void main(String[] args) {
    launch(args);
  }
}