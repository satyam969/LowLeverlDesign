package strategy;

import enums.SugarType;

/**
 * Strategy interface for sugar customization.
 *
 * CHANGED FROM ORIGINAL:
 *  - Was an abstract class acting as a pure data holder (no behavior).
 *  - Converted to an interface so that the Strategy pattern is used correctly:
 *    the context calls apply() and gets polymorphic behavior without knowing the concrete type.
 *  - The abstract class approach provided no behavioral benefit over simply
 *    passing an enum + int directly.
 */
public interface SugarStrategy {

    /** Returns the sugar level enum (NONE / LOW / MEDIUM / HIGH). */
    SugarType getType();

    /**
     * Returns the quantity of sugar in grams to add.
     * RENAMED: getQuantity() → getQuantityGrams() for clarity.
     */
    int getQuantityGrams();

    /**
     * ADDED: Behavioral method — encapsulates HOW this sugar option is applied.
     * This is the core of the Strategy pattern; without it, the interface is just a DTO.
     */
    void apply();
}