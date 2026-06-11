package beverage;

import recipe.Recipe;
import strategy.MilkStrategy;
import strategy.SugarStrategy;

/**
 * Concrete beverage: Coffee.
 *
 * CHANGES FROM ORIGINAL:
 *  1. ADDED constructor — fields were all null in the original (no constructor existed).
 *     The factory was returning a broken object with null recipe, sugar, and milk.
 *  2. Fields made final — a beverage's composition is fixed at creation time.
 *  3. IMPLEMENTED prepare() — was an empty method with just a comment.
 *  4. ADDED dispense() — required by the updated Beverage interface.
 *  5. IMPLEMENTED details() — was an empty method with just a comment.
 *  6. ADDED getName() — required by the updated Beverage interface.
 *  7. ADDED getRecipe() — required for inventory validation.
 */
public class Cofee implements Beverage {

    // CHANGED: all final — a Coffee's recipe and customisations are fixed at creation
    private final Recipe        recipe;
    private final SugarStrategy sugarStrategy;
    private final MilkStrategy  milkStrategy;

    /**
     * ADDED: Constructor that injects recipe and customisation strategies.
     * Original had no constructor, leaving all fields null.
     *
     * @param recipe        ingredient requirements for this coffee
     * @param sugarStrategy selected sugar customisation (NONE/LOW/MEDIUM/HIGH)
     * @param milkStrategy  selected milk customisation (REGULAR/SKIM/ALMOND)
     */
    public Cofee(Recipe recipe, SugarStrategy sugarStrategy, MilkStrategy milkStrategy) {
        this.recipe        = recipe;
        this.sugarStrategy = sugarStrategy;
        this.milkStrategy  = milkStrategy;
    }

    @Override
    public String getName() {
        // ADDED: human-readable name for logging
        return "Coffee";
    }

    @Override
    public Recipe getRecipe() {
        // ADDED: exposes recipe so the machine can validate ingredients
        return recipe;
    }

    @Override
    public void prepare() {
        // IMPLEMENTED: was an empty body with only a comment in the original
        System.out.println("[Coffee] Heating water...");
        System.out.println("[Coffee] Brewing coffee beans...");
        sugarStrategy.apply(); // Strategy pattern in action — behaviour is polymorphic
        milkStrategy.apply();  // Strategy pattern in action
        System.out.println("[Coffee] Coffee prepared successfully!");
    }

    @Override
    public void dispense() {
        // ADDED: separate dispense step, called from DispensingState
        System.out.println("[Coffee] Dispensing your Coffee into the cup...");
        details();
        System.out.println("[Coffee] Enjoy your drink!");
    }

    @Override
    public void details() {
        // IMPLEMENTED: was an empty body with only a comment in the original
        System.out.printf("  Sugar : %-6s (%dg)%n", sugarStrategy.getType(), sugarStrategy.getQuantityGrams());
        System.out.printf("  Milk  : %-6s (%dml)%n", milkStrategy.getType(), milkStrategy.getQuantityMl());
    }
}
