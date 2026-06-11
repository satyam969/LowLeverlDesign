package beverage;

import recipe.Recipe;

/**
 * Core abstraction for all beverages the machine can produce.
 *
 * CHANGES FROM ORIGINAL:
 *  - ADDED getName()    — needed by states and the system to log what's being made.
 *  - ADDED getRecipe()  — needed by inventory validation before preparation.
 *  - ADDED dispense()   — separate from prepare(); called in DispensingState.
 *    Original only had prepare() which conflated brewing and serving.
 *  - details() kept: prints customization info (sugar type, milk type).
 */
public interface Beverage {

    /** Human-readable name (e.g. "Coffee", "Tea", "Latte"). */
    String getName();

    /**
     * Returns the ingredient recipe for this beverage.
     * ADDED: Required for inventory validation (Requirement #5).
     */
    Recipe getRecipe();

    /**
     * Brews / prepares the beverage (heating, brewing, frothing etc.).
     * Called in PreparingState after ingredient validation passes.
     */
    void prepare();

    /**
     * ADDED: Dispenses the ready beverage into the cup.
     * Called in DispensingState when the user collects their drink.
     * Separating dispense from prepare makes the state transitions explicit.
     */
    void dispense();

    /** Prints the customization details (sugar level, milk type, quantities). */
    void details();
}
