package factory;

import beverage.Beverage;
import beverage.Latte;
import recipe.Recipe;
import strategy.MilkStrategy;
import strategy.SugarStrategy;

/**
 * ADDED: Factory that creates Latte beverages.
 * Required by Requirement #1 (support multiple beverage types via Factory Pattern).
 */
public class LatteFactory implements BeverageFactory {

    @Override
    public Beverage createBeverage(SugarStrategy sugarStrategy, MilkStrategy milkStrategy) {
        return new Latte(Recipe.forLatte(), sugarStrategy, milkStrategy);
    }
}
