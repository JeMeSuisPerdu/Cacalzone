package pizzas;

import java.io.Serializable;

/**
 * Classe abstraite représentant un utilisateur (client ou pizzaiolo).
 */
public abstract class Utilisateur implements Serializable {

    protected String email;
    protected String motDePasse;
    protected InformationPersonnelle infos;

    public Utilisateur(String email, String motDePasse, InformationPersonnelle infos) {
        this.email = email;
        this.motDePasse = motDePasse;
        this.infos = infos;
    }

    public String getEmail() { return email; }
    public String getMotDePasse() { return motDePasse; }
    public InformationPersonnelle getInfos() { return infos; }
    
    public abstract boolean estPizzaiolo();
}
