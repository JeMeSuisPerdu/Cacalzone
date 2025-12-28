package pizzas;

import io.InterSauvegarde;
import java.io.*;

public class ServiceSauvegarde implements InterSauvegarde {
    private Menu menu;

    /**
     * Le constructeur reçoit le menu actuel pour pouvoir le sauvegarder
     * ou le mettre à jour au chargement.
     */
    public ServiceSauvegarde(Menu menu) {
        this.menu = menu;
    }

    @Override
    public void sauvegarderDonnees(String nomFichier) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomFichier))) {
            oos.writeObject(this.menu);
        }
    }

    @Override
    public void chargerDonnees(String nomFichier) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nomFichier))) {
            Menu menuCharge = (Menu) ois.readObject();

            this.menu.getPizzas().clear();
            this.menu.getPizzas().addAll(menuCharge.getPizzas());
            
            this.menu.getIngredients().clear();
            this.menu.getIngredients().addAll(menuCharge.getIngredients());
            
            this.menu.getUtilisateurs().clear();
            this.menu.getUtilisateurs().addAll(menuCharge.getUtilisateurs());
            
            this.menu.getCommandesGlobales().clear();
            this.menu.getCommandesGlobales().addAll(menuCharge.getCommandesGlobales());
            
        } catch (ClassNotFoundException e) {
            throw new IOException("Erreur de format de fichier : classe Menu non trouvée.", e);
        }
    }
}