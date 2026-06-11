package strategy;

import enums.MilkType;

/**
 * ADDED: Skim-milk strategy — required by Requirement #7 (milk type: skim).
 */
public class SkimMilkStrategy implements MilkStrategy {

    private static final int SKIM_MILK_ML = 60;

    @Override
    public MilkType getType() {
        return MilkType.SKIM;
    }

    @Override
    public int getQuantityMl() {
        return SKIM_MILK_ML;
    }

    @Override
    public void apply() {
        System.out.println("  [Milk]  Adding SKIM milk: " + SKIM_MILK_ML + "ml");
    }
}
