package pizzas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un compte client de la pizzeria.
 *
 * <p>Elle hérite de la classe abstraite Utilisateur et ajoute la gestion
 * spécifique de l'historique des commandes passées par ce client.</p>
 */
public class CompteClient extends Utilisateur implements Serializable {

  /**
   * Identifiant de sérialisation.
   */
  private static final long serialVersionUID = 1L;

  /**
   * La liste chronologique des commandes effectuées par le client.
   */
  private List<Commande> historiqueCommandes;

  /**
   * Constructeur initialisant un nouveau compte client.
   *
   * <p>Ce constructeur appelle celui de la classe mère pour les données
   * d'authentification et d'identité, et initialise la liste de l'historique
   * des commandes à vide.</p>
   *
   * @param email L'adresse email servant d'identifiant.
   * @param motDePasse Le mot de passe de connexion.
   * @param infos Les informations personnelles du client.
   */
  public CompteClient(String email, String motDePasse,
      InformationPersonnelle infos) {
    super(email, motDePasse, infos);
    this.historiqueCommandes = new ArrayList<>();
  }

  /**
   * Indique si l'utilisateur possède le rôle de pizzaïolo.
   *
   * <p>Cette méthode est une implémentation de la méthode abstraite définie
   * dans Utilisateur. Pour un CompteClient, elle renvoie toujours false.</p>
   *
   * @return false, car cet utilisateur est un client standard.
   */
  @Override
  public boolean estPizzaiolo() {
    return false;
  }

  /**
   * Récupère l'historique des commandes du client.
   *
   * @return La liste des commandes passées.
   */
  public List<Commande> getHistoriqueCommandes() {
    return historiqueCommandes;
  }

  /**
   * Ajoute une commande validée à l'historique du client.
   *
   * <p>Cette méthode est généralement appelée lorsqu'une commande passe
   * à l'état validé, afin de la conserver dans le dossier du client.</p>
   *
   * @param cmd La commande à ajouter à l'historique.
   */
  public void ajouterCommande(Commande cmd) {
    this.historiqueCommandes.add(cmd);
  }
}