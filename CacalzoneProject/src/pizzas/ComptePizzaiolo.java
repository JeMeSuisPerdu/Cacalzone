package pizzas;

import java.io.Serializable;

/**
 * Class représentant le Pizzaiolo unique.
 */
public class ComptePizzaiolo extends Utilisateur implements Serializable {

  /**
   * Identifiant de sérialisation.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Constructeur du compte pizzaïolo.
   *
   * @param email L'adresse email du pizzaïolo.
   * @param motDePasse Le mot de passe associé au compte.
   * @param infos Les informations personnelles du pizzaïolo.
   */
  public ComptePizzaiolo(String email, String motDePasse, InformationPersonnelle infos) {
    super(email, motDePasse, infos);
  }

  /**
   * Indique si l'utilisateur possède les droits de pizzaïolo.
   *
   * @return true car ce compte appartient au pizzaïolo.
   */
  @Override
  public boolean estPizzaiolo() {
    return true;
  }
}
