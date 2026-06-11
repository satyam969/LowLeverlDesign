package splitwise.factory;
import splitwise.model.SplitType;
import splitwise.strategy.PercentageSplitStrategy;
import splitwise.strategy.SplitStrategy;
import splitwise.strategy.EqualSplitStrategy;


public class SplitStrategyFactory {
    public static SplitStrategy getStrategy(SplitType splitType) {
        return switch (splitType) {
            case EQUAL -> new EqualSplitStrategy();
            case PERCENTAGE -> new PercentageSplitStrategy();
        };
    }
}