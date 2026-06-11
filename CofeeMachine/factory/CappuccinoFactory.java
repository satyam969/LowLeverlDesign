package factory;

import beverage.Beverage;
import beverage.Cappuccino;
import recipe.Recipe;
import strategy.MilkStrategy;
import strategy.SugarStrategy;

/**
 * ADDED: Factory that creates Cappuccino beverages.
 * Required by Requirement #1 (support multiple beverage types via Factory Pattern).
 */
public class CappuccinoFactory implements BeverageFactory {

    @Override
    public Beverage createBeverage(SugarStrategy sugarStrategy, MilkStrategy milkStrategy) {
        return new Cappuccino(Recipe.forCappuccino(), sugarStrategy, milkStrategy);
    }
}
