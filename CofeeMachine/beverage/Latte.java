package beverage;

import recipe.Recipe;
import strategy.MilkStrategy;
import strategy.SugarStrategy;

/**
 * ADDED: Latte beverage — required by Requirement #1.
 * Latte uses more milk than a standard coffee.
 */
public class Latte implements Beverage {

    private final Recipe        recipe;
    private final SugarStrategy sugarStrategy;
    private final MilkStrategy  milkStrategy;

    public Latte(Recipe recipe, SugarStrategy sugarStrategy, MilkStrategy milkStrategy) {
        this.recipe        = recipe;
        this.sugarStrategy = sugarStrategy;
        this.milkStrategy  = milkStrategy;
    }

    @Override
    public String getName() {
        return "Latte";
    }

    @Override
    public Recipe getRecipe() {
        return recipe;
    }

    @Override
    public void prepare() {
        System.out.println("[Latte] Pulling espresso shot...");
        System.out.println("[Latte] Steaming and frothing milk...");
        sugarStrategy.apply();
        milkStrategy.apply();
        System.out.println("[Latte] Latte prepared successfully!");
    }

    @Override
    public void dispense() {
        System.out.println("[Latte] Dispensing your Latte into the cup...");
        details();
        System.out.println("[Latte] Enjoy your drink!");
    }

    @Override
    public void details() {
        System.out.printf("  Sugar : %-6s (%dg)%n", sugarStrategy.getType(), sugarStrategy.getQuantityGrams());
        System.out.printf("  Milk  : %-6s (%dml)%n", milkStrategy.getType(), milkStrategy.getQuantityMl());
    }
}
