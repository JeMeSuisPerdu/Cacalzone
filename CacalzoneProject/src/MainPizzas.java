import java.util.HashSet;
import pizzas.*;

/**
 * Classe d'essai de fonctionnement de l'application.
 *
 * @author Eric Cariou
 *
 */
public class MainPizzas {
  
  /**
   * Si le main() s'exécute, c'est que le projet est fonctionnel.
   *
   * @param args inutiles ici.
   */
  public static void main(String[] args) {
    System.out.println("\n------------- TEST TEMPORAIRE PIZZA -------------");
    
    Pizza pizza = new Pizza("Margherita",TypePizza.Vegetarienne);
    
    HashSet<TypePizza> typeNonAutorise = new HashSet<>();
    typeNonAutorise.add(TypePizza.Regionale);
    typeNonAutorise.add(TypePizza.Viande);

    Ingredient i = new Ingredient("Camembert",6.3,typeNonAutorise);
    if(pizza.ajouterIngredient(i)) {
      System.out.println("INGREDIENT AJOUTE AVEC SUCCES A LA PIZZA");
      }else {
        System.out.println("INGREDIENT NON CONFORME AVEC LE TYPE DE LA PIZZA");
      }
    
    System.out.println("-------------- FIN -----------------");
  }
}
