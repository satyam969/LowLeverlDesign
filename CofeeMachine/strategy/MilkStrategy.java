package strategy;

import enums.MilkType;

/**
 * Strategy interface for milk customization.
 *
 * CHANGED FROM ORIGINAL:
 *  - Was an abstract class acting as a pure data holder (no behavior).
 *  - Converted to interface for the same reasons as SugarStrategy.
 *  - apply() method added to give the strategy actual behavioral meaning.
 */
public interface MilkStrategy {

    /** Returns the milk type enum (REGULAR / SKIM / ALMOND). */
    MilkType getType();

    /**
     * Returns the quantity of milk in millilitres.
     * RENAMED: getQuantity() → getQuantityMl() for clarity.
     */
    int getQuantityMl();

    /**
     * ADDED: Behavioral method encapsulating how this milk option is applied.
     */
    void apply();
}
