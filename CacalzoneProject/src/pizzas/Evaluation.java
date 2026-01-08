package pizzas;

import java.io.Serializable;

/**
 * Représente une évaluation laissée par un client sur une pizza.
 *
 * <p>Cette classe permet de stocker le retour d'expérience d'un consommateur.
 * Une évaluation est composée obligatoirement d'une note chiffrée (bornée
 * entre 0 et 5) et d'un identifiant client. Elle peut optionnellement
 * contenir un commentaire textuel détaillant l'avis.</p>
 */
public class Evaluation implements Serializable {

  /**
   * Identifiant de sérialisation utilisé pour garantir la compatibilité
   * des versions de classe lors de la sauvegarde et du chargement des données.
   */
  private static final long serialVersionUID = 1L;

  /**
   * La note attribuée à la pizza, représentant le niveau de satisfaction.
   * Cette valeur est garantie d'être comprise entre 0 (mauvais) et 5 (excellent).
   */
  private int note;

  /**
   * Le commentaire textuel associé à l'évaluation.
   * Ce champ est facultatif et peut être null si le client n'a pas laissé d'avis écrit.
   */
  private String commentaire;

  /**
   * L'adresse email du client ayant laissé l'évaluation.
   * Sert d'identifiant unique pour savoir qui est l'auteur de la note.
   */
  private String emailClient;

  /**
   * Construit une nouvelle évaluation avec validation des données.
   *
   * <p>Ce constructeur applique une logique de bornage automatique sur la note :
   * si la note fournie est inférieure à 0, elle est ramenée à 0. Si elle est
   * supérieure à 5, elle est ramenée à 5. Cela garantit la cohérence des données.</p>
   *
   * @param note La note brute attribuée par le client (sera ajustée si hors limites).
   * @param commentaire Le texte de l'évaluation (peut être null ou vide).
   * @param emailClient L'adresse email du client auteur de l'avis.
   */
  public Evaluation(int note, String commentaire, String emailClient) {
    this.note = (note < 0) ? 0 : (note > 5) ? 5 : note;
    this.commentaire = commentaire;
    this.emailClient = emailClient;
  }

  /**
   * Récupère la note validée de l'évaluation.
   *
   * @return Un entier compris entre 0 et 5 inclus.
   */
  public int getNote() {
    return note;
  }

  /**
   * Récupère le commentaire textuel de l'évaluation.
   *
   * @return Le texte du commentaire, ou null si aucun commentaire n'a été fourni.
   */
  public String getCommentaire() {
    return commentaire;
  }

  /**
   * Récupère l'identifiant de l'auteur de l'évaluation.
   *
   * @return L'adresse email du client ayant noté la pizza.
   */
  public String getEmailClient() {
    return emailClient;
  }

  /**
   * Fournit une représentation textuelle de l'évaluation pour l'affichage.
   *
   * <p>Le format retourné est "Note: X/5" suivi éventuellement de " - Commentaire"
   * si un commentaire existe.</p>
   *
   * @return Une chaîne de caractères formatée décrivant l'évaluation.
   */
  @Override
  public String toString() {
    return "Note: " + note + "/5"
        + (commentaire != null ? " - " + commentaire : "");
  }
}