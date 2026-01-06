package pizzas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un client et son historique.
 */
public class CompteClient extends Utilisateur implements Serializable {

  /**
   * Identifiant de sérialisation.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Liste des commandes passées par le client.
   */
  private List<Commande> historiqueCommandes;

  /**
   * Constructeur du compte client.
   *
   * @param email L'email du client.
   * @param motDePasse Le mot de passe du client.
   * @param infos Les informations personnelles.
   */
  public CompteClient(String email, String motDePasse, InformationPersonnelle infos) {
    super(email, motDePasse, infos);
    this.historiqueCommandes = new ArrayList<>();
  }

  /**
   * Indique si l'utilisateur est un pizzaïolo.
   *
   * @return false car c'est un compte client.
   */
  @Override
  public boolean estPizzaiolo() {
    return false;
  }

  /**
   * Récupère l'historique complet des commandes.
   *
   * @return La liste des commandes.
   */
  public List<Commande> getHistoriqueCommandes() {
    return historiqueCommandes;
  }

  /**
   * Ajoute une nouvelle commande à l'historique du client.
   *
   * @param cmd La commande à ajouter.
   */
  public void ajouterCommande(Commande cmd) {
    this.historiqueCommandes.add(cmd);
  }
}