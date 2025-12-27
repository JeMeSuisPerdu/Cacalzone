package pizzas;

/**
 * Entité représentant le Pizzaiolo unique.
 */
public class ComptePizzaiolo extends Utilisateur {
    private static final long serialVersionUID = 1L;

    public ComptePizzaiolo(String email, String motDePasse, InformationPersonnelle infos) {
        super(email, motDePasse, infos);
    }

    @Override
    public boolean estPizzaiolo() { return true; }
}