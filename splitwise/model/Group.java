package splitwise.model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Group {
    public List<User> members;
    public List<Expense> expenses;
    public String id;
    public String name;
    public HashMap <User,BalanceSheet> balanceSheets;
    public Group(String id, String name) {
        this.id = id;
        this.name = name;
        this.members = new ArrayList<>();
        this.expenses = new ArrayList<>();
        this.balanceSheets = new HashMap<>();
    }
    public void addMember(User user) {
        members.add(user);
        balanceSheets.putIfAbsent(user, new BalanceSheet());
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public BalanceSheet getBalanceSheet(User user) {
        return balanceSheets.get(user);
    }

    public Map <User, BalanceSheet> getBalanceSheets() {
        return balanceSheets;
    }

    public List<User> getMembers() {
        return members;
    }
}
