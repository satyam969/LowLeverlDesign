package strategy;

import enums.SugarType;

/**
 * CHANGED FROM ORIGINAL:
 *  - Now implements SugarStrategy interface instead of extending abstract class.
 *  - FIXED: Magic number 5 replaced with named constant LOW_SUGAR_GRAMS.
 *  - ADDED: apply() method providing actual behavior.
 */
public class LowSugarStrategy implements SugarStrategy {

    // CHANGED: Named constant instead of magic number 5
    private static final int LOW_SUGAR_GRAMS = 5;

    @Override
    public SugarType getType() {
        return SugarType.LOW;
    }

    @Override
    public int getQuantityGrams() {
        return LOW_SUGAR_GRAMS;
    }

    @Override
    public void apply() {
        // ADDED: Behavioral method — the context calls this without knowing the concrete type
        System.out.println("  [Sugar] Applying LOW sugar: " + LOW_SUGAR_GRAMS + "g");
    }
}
