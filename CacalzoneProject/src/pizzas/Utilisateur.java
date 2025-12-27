package pizzas;

import java.io.Serializable;

/**
 * Classe abstraite représentant un utilisateur du système (Client ou Pizzaiolo).
 */
public abstract class Utilisateur implements Serializable {
    private static final long serialVersionUID = 1L;

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
    
    /**
     * Permet de distinguer les rôles sans utiliser instanceof partout.
     */
    public abstract boolean estPizzaiolo();
}
