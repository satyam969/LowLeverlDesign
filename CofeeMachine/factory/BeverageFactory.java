package factory;

import beverage.Beverage;
import strategy.MilkStrategy;
import strategy.SugarStrategy;

/**
 * Abstract Factory interface for creating beverage instances.
 *
 * CHANGED FROM ORIGINAL:
 *  - createBeverage() now accepts sugar and milk customisation strategies.
 *  - Original took no parameters, so there was no way to pass user customisations
 *    into the created beverage — the factory was effectively useless for customisation.
 *
 * @see strategy.SugarStrategy
 * @see strategy.MilkStrategy
 */
public interface BeverageFactory {

    /**
     * Creates a fully configured beverage instance.
     *
     * CHANGED: Added parameters for customisation strategies.
     * Original signature: Beverage createBeverage()  ← no customisation possible
     *
     * @param sugarStrategy the sugar customisation to apply
     * @param milkStrategy  the milk customisation to apply
     * @return a ready-to-use Beverage with its recipe and strategies wired in
     */
    Beverage createBeverage(SugarStrategy sugarStrategy, MilkStrategy milkStrategy);
}
