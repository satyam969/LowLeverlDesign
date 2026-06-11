package splitwise.strategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import splitwise.model.Split;
import splitwise.model.User;

public class PercentageSplitStrategy implements SplitStrategy {
    public List<Split> split(double totalAmount, List<User> participants, Map<User, Double> metadata) {
        double totalPercent = metadata.values().stream().mapToDouble(Double::doubleValue).sum();
        if (totalPercent != 100.0) throw new IllegalArgumentException("Total percent should be 100");

        List<Split> splits = new ArrayList<>();
        for (User user : participants) {
            splits.add(new Split(user, totalAmount * metadata.getOrDefault(user, 0.0) / 100));
        }
        return splits;
    }
}
