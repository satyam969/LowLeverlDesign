package splitwise.service;

import java.util.List;
import java.util.Map;

import splitwise.factory.SplitStrategyFactory;
import splitwise.model.Expense;
import splitwise.model.Split;
import splitwise.model.SplitType;
import splitwise.model.User;
import splitwise.strategy.SplitStrategy;
import splitwise.model.Group;


public  class ExpenseService {
    private  BalanceSheetService balanceSheetService;

    public ExpenseService(BalanceSheetService balanceSheetService) {
        this.balanceSheetService = balanceSheetService;
    }

    public void addExpense(Group group, String description, double amount, User paidBy,
                           List<User> participants, SplitType splitType, Map<User, Double> metadata) {

        SplitStrategy strategy = SplitStrategyFactory.getStrategy(splitType);
        List<Split> splits = strategy.split(amount, participants, metadata);
        Expense expense = new Expense(description, amount, paidBy, splits, splitType);
        group.addExpense(expense);
        balanceSheetService.updateBalances(group, paidBy, splits);
    }
} 
