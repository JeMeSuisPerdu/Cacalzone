package pizzas;

/**
 * Exception levée quand il y a un problème avec la commande d'un client.
 * 
 * <p>Cette exception est utilisée pour signaler des erreurs métiers lors du
 * cycle de vie d'une commande (ex: tentative de modification d'une commande
 * déjà validée, validation d'une commande vide, etc.).
 * </p>
 *
 * @author Eric Cariou
 */
public class CommandeException extends Exception {

  /**
   * Identifiant de sérialisation.
   */
  private static final long serialVersionUID = -2876441299971092712L;

  /**
   * Constructeur par défaut.
   * Crée une nouvelle instance de CommandeException sans message détaillé.
   * L'appel à getMessage() renverra null.
   */
  public CommandeException() {
    super();
  }

  /**
   * Constructeur permettant de spécifier un message d'erreur.
   * 
   * <p>C'est ce message qui sera récupéré par {@link #getMessage()} et affiché
   * dans les fenêtres d'alerte (popups) de l'interface graphique.
   * </p>
   *
   * @param message Le détail de l'erreur (ex: "Commande vide").
   */
  public CommandeException(String message) {
    super(message);
  }
}