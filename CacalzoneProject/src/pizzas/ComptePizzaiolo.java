package pizzas;

import java.io.Serializable;

/**
 * Class représentant le Pizzaiolo unique.
 */
public class ComptePizzaiolo extends Utilisateur implements Serializable {

    public ComptePizzaiolo(String email, String motDePasse, InformationPersonnelle infos) {
        super(email, motDePasse, infos);
    }

    @Override
    public boolean estPizzaiolo() { return true; }
}