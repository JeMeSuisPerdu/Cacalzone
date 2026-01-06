package pizzas;

import io.InterSauvegarde;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Service technique gérant la persistance des données de la pizzeria.
 * 
 * <p>Cette classe utilise le mécanisme de sérialisation Java pour écrire et lire
 * l'état complet du menu (Pizzas, Ingrédients, Utilisateurs, Commandes)
 * dans un fichier binaire. Cela permet de conserver les données entre deux
 * exécutions de l'application.</p>

 */
public class ServiceSauvegarde implements InterSauvegarde {

  /**
   * Référence vers l'objet Menu central de l'application.
   * C'est cet objet qui est lu pour la sauvegarde et mis à jour lors du
   * chargement.
   */
  private Menu menu;

  /**
   * Constructeur du service de sauvegarde.
   * 
   * <p>Il est crucial de passer cette référence pour que le chargement mette à
   * jour les données visibles par les contrôleurs.
   * 
   * @param menu L'instance unique du Menu partagée par toute l'application.
   */
  public ServiceSauvegarde(Menu menu) {
    this.menu = menu;
  }

  /**
   * Sauvegarde l'état actuel du menu dans un fichier.
   * Utilise `ObjectOutputStream` pour sérialiser l'objet Menu entier.
   *
   * @param nomFichier Le chemin ou nom du fichier de destination
   * @throws IOException Si une erreur d'entrée/sortie survient (fichierinaccessible).
   */
  @Override
  public void sauvegarderDonnees(String nomFichier) throws IOException {
    try (ObjectOutputStream oos = new ObjectOutputStream(
        new FileOutputStream(nomFichier))) {
      oos.writeObject(this.menu);
    }
  }

  /**
   * Charge les données depuis un fichier et met à jour le menu actuel.
   * 
   * <p>Cette méthode ne remplace pas l'objet `this.menu` par
   * celui lu dans le fichier. À la place, elle vide les listes
   * de l'objet actuel (`clear()`) et y ajoute
   * le contenu de l'objet chargé (`addAll()`). 
   * Cela garantit que toute l'application voit les nouvelles données. </p>
   *
   * @param nomFichier Le nom du fichier source à lire.
   * @throws IOException Si le fichier n'existe pas ou est illisible.
   */
  @Override
  public void chargerDonnees(String nomFichier) throws IOException {
    try (ObjectInputStream ois = new ObjectInputStream(
        new FileInputStream(nomFichier))) {
      Menu menuCharge = (Menu) ois.readObject();

      if (menuCharge != null) {
        this.menu.getPizzas().clear();
        this.menu.getPizzas().addAll(menuCharge.getPizzas());

        this.menu.getIngredients().clear();
        this.menu.getIngredients().addAll(menuCharge.getIngredients());

        this.menu.getUtilisateurs().clear();
        this.menu.getUtilisateurs().addAll(menuCharge.getUtilisateurs());

        this.menu.getCommandesGlobales().clear();
        this.menu.getCommandesGlobales().addAll(
            menuCharge.getCommandesGlobales());
      }

    } catch (ClassNotFoundException e) {
      throw new IOException("Erreur : La classe Menu est introuvable ou "
          + "incompatible avec le fichier de sauvegarde.", e);
    }
  }
}