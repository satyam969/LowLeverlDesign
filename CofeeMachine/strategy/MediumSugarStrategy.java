package strategy;

import enums.SugarType;

/**
 * ADDED: Medium-sugar strategy — required by Requirement #7 (sugar level: medium).
 */
public class MediumSugarStrategy implements SugarStrategy {

    private static final int MEDIUM_SUGAR_GRAMS = 10;

    @Override
    public SugarType getType() {
        return SugarType.MEDIUM;
    }

    @Override
    public int getQuantityGrams() {
        return MEDIUM_SUGAR_GRAMS;
    }

    @Override
    public void apply() {
        System.out.println("  [Sugar] Applying MEDIUM sugar: " + MEDIUM_SUGAR_GRAMS + "g");
    }
}
