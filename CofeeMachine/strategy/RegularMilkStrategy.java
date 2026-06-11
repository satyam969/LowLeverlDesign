package strategy;

import enums.MilkType;

/**
 * CHANGED FROM ORIGINAL:
 *  - Now implements MilkStrategy interface instead of extending abstract class.
 *  - FIXED: Magic number 5 replaced with named constant REGULAR_MILK_ML.
 *  - ADDED: apply() method providing actual behavior.
 */
public class RegularMilkStrategy implements MilkStrategy  {

    // CHANGED: Named constant instead of magic number 5
    private static final int REGULAR_MILK_ML = 60;

    @Override
    public MilkType getType() {
        return MilkType.REGULAR;
    }

    @Override
    public int getQuantityMl() {
        return REGULAR_MILK_ML;
    }

    @Override
    public void apply() {
        // ADDED: Behavioral method
        System.out.println("  [Milk]  Adding REGULAR milk: " + REGULAR_MILK_ML + "ml");
    }
}
