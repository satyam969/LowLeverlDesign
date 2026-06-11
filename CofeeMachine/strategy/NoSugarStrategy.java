package strategy;

import enums.SugarType;

/**
 * ADDED: No-sugar strategy — required by Requirement #7 (sugar level: none).
 */
public class NoSugarStrategy implements SugarStrategy {

    @Override
    public SugarType getType() {
        return SugarType.NONE;
    }

    @Override
    public int getQuantityGrams() {
        return 0;
    }

    @Override
    public void apply() {
        System.out.println("  [Sugar] No sugar added.");
    }
}
