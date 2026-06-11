package factory;

import beverage.Beverage;
import beverage.Cofee;
import recipe.Recipe;
import strategy.MilkStrategy;
import strategy.SugarStrategy;

/**
 * Factory that creates Coffee beverages.
 *
 * CHANGED FROM ORIGINAL:
 *  - Implements the updated BeverageFactory interface with strategy parameters.
 *  - FIXED: Original returned new Cofee() with ALL fields null (no constructor existed).
 *    Now properly wires the recipe and both strategies into the Cofee object.
 */
public class CofeeFactory implements BeverageFactory {

    @Override
    public Beverage createBeverage(SugarStrategy sugarStrategy, MilkStrategy milkStrategy) {
        // FIXED: Now passes the recipe and both strategies to the Cofee constructor
        // Original: return new Cofee()  ← returned an object with 3 null fields
        return new Cofee(Recipe.forCoffee(), sugarStrategy, milkStrategy);
    }
}
