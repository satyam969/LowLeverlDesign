package beverage;

import recipe.Recipe;
import strategy.MilkStrategy;
import strategy.SugarStrategy;

/**
 * ADDED: Tea beverage — required by Requirement #1 (support tea, cappuccino, latte, coffee).
 * Mirrors the Cofee class structure but with tea-specific preparation steps.
 */
public class Tea implements Beverage {

    private final Recipe        recipe;
    private final SugarStrategy sugarStrategy;
    private final MilkStrategy  milkStrategy;

    public Tea(Recipe recipe, SugarStrategy sugarStrategy, MilkStrategy milkStrategy) {
        this.recipe        = recipe;
        this.sugarStrategy = sugarStrategy;
        this.milkStrategy  = milkStrategy;
    }

    @Override
    public String getName() {
        return "Tea";
    }

    @Override
    public Recipe getRecipe() {
        return recipe;
    }

    @Override
    public void prepare() {
        System.out.println("[Tea] Boiling water...");
        System.out.println("[Tea] Steeping tea leaves...");
        sugarStrategy.apply();
        milkStrategy.apply();
        System.out.println("[Tea] Tea prepared successfully!");
    }

    @Override
    public void dispense() {
        System.out.println("[Tea] Dispensing your Tea into the cup...");
        details();
        System.out.println("[Tea] Enjoy your drink!");
    }

    @Override
    public void details() {
        System.out.printf("  Sugar : %-6s (%dg)%n", sugarStrategy.getType(), sugarStrategy.getQuantityGrams());
        System.out.printf("  Milk  : %-6s (%dml)%n", milkStrategy.getType(), milkStrategy.getQuantityMl());
    }
}
