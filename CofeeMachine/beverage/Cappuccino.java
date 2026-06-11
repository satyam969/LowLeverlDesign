package beverage;

import recipe.Recipe;
import strategy.MilkStrategy;
import strategy.SugarStrategy;

/**
 * ADDED: Cappuccino beverage — required by Requirement #1.
 * Cappuccino uses equal parts espresso, steamed milk, and milk foam.
 */
public class Cappuccino implements Beverage {

    private final Recipe        recipe;
    private final SugarStrategy sugarStrategy;
    private final MilkStrategy  milkStrategy;

    public Cappuccino(Recipe recipe, SugarStrategy sugarStrategy, MilkStrategy milkStrategy) {
        this.recipe        = recipe;
        this.sugarStrategy = sugarStrategy;
        this.milkStrategy  = milkStrategy;
    }

    @Override
    public String getName() {
        return "Cappuccino";
    }

    @Override
    public Recipe getRecipe() {
        return recipe;
    }

    @Override
    public void prepare() {
        System.out.println("[Cappuccino] Pulling double espresso shot...");
        System.out.println("[Cappuccino] Steaming and frothing milk to stiff foam...");
        sugarStrategy.apply();
        milkStrategy.apply();
        System.out.println("[Cappuccino] Cappuccino prepared successfully!");
    }

    @Override
    public void dispense() {
        System.out.println("[Cappuccino] Dispensing your Cappuccino into the cup...");
        details();
        System.out.println("[Cappuccino] Enjoy your drink!");
    }

    @Override
    public void details() {
        System.out.printf("  Sugar : %-6s (%dg)%n", sugarStrategy.getType(), sugarStrategy.getQuantityGrams());
        System.out.printf("  Milk  : %-6s (%dml)%n", milkStrategy.getType(), milkStrategy.getQuantityMl());
    }
}
