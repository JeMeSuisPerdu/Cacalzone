package pizzas;

import java.io.Serializable;

/**
 * Représente une évaluation laissée par un client sur une pizza. 
 * Contient une note obligatoire et un commentaire optionnel.
 */
public class Evaluation implements Serializable {

  /**
   * Identifiant de sérialisation.
   */
  private static final long serialVersionUID = 1L;

  /**
   * La note attribuée à la pizza (entre 0 et 5).
   */
  private int note;

  /**
   * Le commentaire textuel associé à l'évaluation.
   */
  private String commentaire;

  /**
   * L'adresse email du client ayant laissé l'évaluation.
   */
  private String emailClient;

  /**
   * Constructeur d'une évaluation.
   *
   * @param note La note de 0 à 5.
   * @param commentaire Le texte de l'évaluation (peut être null).
   * @param emailClient L'identifiant du client.
   */
  public Evaluation(int note, String commentaire, String emailClient) {
    this.note = (note < 0) ? 0 : (note > 5) ? 5 : note;
    this.commentaire = commentaire;
    this.emailClient = emailClient;
  }

  /**
   * Récupère la note de l'évaluation.
   *
   * @return La note sur 5.
   */
  public int getNote() {
    return note;
  }

  /**
   * Récupère le commentaire de l'évaluation.
   *
   * @return Le texte du commentaire.
   */
  public String getCommentaire() {
    return commentaire;
  }

  /**
   * Récupère l'email du client auteur.
   *
   * @return L'identifiant email.
   */
  public String getEmailClient() {
    return emailClient;
  }

  /**
   * Retourne une représentation textuelle de l'évaluation.
   *
   * @return Une chaîne contenant la note et le commentaire s'il existe.
   */
  @Override
  public String toString() {
    return "Note: " + note + "/5" 
        + (commentaire != null ? " - " + commentaire : "");
  }
}