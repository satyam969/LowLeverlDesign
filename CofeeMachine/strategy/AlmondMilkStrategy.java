package strategy;

import enums.MilkType;

/**
 * ADDED: Almond-milk strategy — required by Requirement #7 (milk type: almond).
 */
public class AlmondMilkStrategy implements MilkStrategy {

    private static final int ALMOND_MILK_ML = 60;

    @Override
    public MilkType getType() {
        return MilkType.ALMOND;
    }

    @Override
    public int getQuantityMl() {
        return ALMOND_MILK_ML;
    }

    @Override
    public void apply() {
        System.out.println("  [Milk]  Adding ALMOND milk: " + ALMOND_MILK_ML + "ml");
    }
}
