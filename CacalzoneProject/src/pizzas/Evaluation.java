package pizzas;

/**
 * Représente une évaluation laissée par un client sur une pizza. 
 * Contient une note obligatoire et un commentaire optionnel.
 */
public class Evaluation {

    private int note;
    private String commentaire;
    private String emailClient;

    /**
     * Constructeur d'une évaluation.
     * @param note La note de 0 à 5
     * @param commentaire Le texte (peut être null)
     * @param emailClient L'identifiant du client
     */
    public Evaluation(int note, String commentaire, String emailClient) {
        this.note = (note < 0) ? 0 : (note > 5) ? 5 : note;
        this.commentaire = commentaire;
        this.emailClient = emailClient;
    }

    public int getNote() {
        return note;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public String getEmailClient() {
        return emailClient;
    }

    @Override
    public String toString() {
        return "Note: " + note + "/5" + (commentaire != null ? " - " + commentaire : "");
    }
}