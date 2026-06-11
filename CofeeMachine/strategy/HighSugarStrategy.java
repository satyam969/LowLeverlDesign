package strategy;

import enums.SugarType;

/**
 * ADDED: High-sugar strategy — required by Requirement #7 (sugar level: high).
 */
public class HighSugarStrategy implements SugarStrategy {

    private static final int HIGH_SUGAR_GRAMS = 15;

    @Override
    public SugarType getType() {
        return SugarType.HIGH;
    }

    @Override
    public int getQuantityGrams() {
        return HIGH_SUGAR_GRAMS;
    }

    @Override
    public void apply() {
        System.out.println("  [Sugar] Applying HIGH sugar: " + HIGH_SUGAR_GRAMS + "g");
    }
}
