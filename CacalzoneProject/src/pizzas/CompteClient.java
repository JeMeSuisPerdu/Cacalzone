package pizzas;

import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant un client et son historique.
 */
public class CompteClient extends Utilisateur {
    private static final long serialVersionUID = 1L;
    
    private List<Commande> historiqueCommandes;

    public CompteClient(String email, String motDePasse, InformationPersonnelle infos) {
        super(email, motDePasse, infos);
        this.historiqueCommandes = new ArrayList<>();
    }

    @Override
    public boolean estPizzaiolo() { return false; }

    public List<Commande> getHistoriqueCommandes() {
        return historiqueCommandes;
    }

    public void ajouterCommande(Commande cmd) {
        this.historiqueCommandes.add(cmd);
    }
}