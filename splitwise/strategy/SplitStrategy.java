package splitwise.strategy;
import splitwise.model.Split;
import java.util.List;
import java.util.Map;

import splitwise.model.User;

public interface SplitStrategy {

    public List<Split> split(double totalAmount, List<User> participants, Map<User, Double> metadata);
    
} 
