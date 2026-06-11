package factory;

import beverage.Beverage;
import beverage.Tea;
import recipe.Recipe;
import strategy.MilkStrategy;
import strategy.SugarStrategy;

/**
 * ADDED: Factory that creates Tea beverages.
 * Required by Requirement #1 (support multiple beverage types via Factory Pattern).
 */
public class TeaFactory implements BeverageFactory {

    @Override
    public Beverage createBeverage(SugarStrategy sugarStrategy, MilkStrategy milkStrategy) {
        return new Tea(Recipe.forTea(), sugarStrategy, milkStrategy);
    }
}
