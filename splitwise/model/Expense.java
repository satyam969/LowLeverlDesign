package splitwise.model;
import java.util.List;

public class Expense {
    
    public String description;
    public double amount;
    public User paidBy;
    public List<Split> splits;
    public SplitType splitType;
    
    public Expense(String description, double amount, User paidBy, List<Split> splits, SplitType splitType) {
        this.description = description;
        this.amount = amount;
        this.paidBy = paidBy;
        this.splits = splits;
        this.splitType = splitType;
    }
}
