package splitwise;
import java.util.*;

import splitwise.model.SplitType;
import splitwise.model.User;
import splitwise.service.BalanceSheetService;
import splitwise.service.DebtSimplificationService;
import splitwise.service.ExpenseService;
import splitwise.service.GroupService;
import splitwise.repo.InMemoryGroupRepo;



public class Main {
    public static void main(String args[]){
        System.out.println("Hello Splitwise");
        User shubh = new User("u1", "Shubh");
        User bob   = new User("u2", "Bob");
        User tom   = new User("u3", "Tom");
        User jake  = new User("u4", "Jake");

        InMemoryGroupRepo repo  = new InMemoryGroupRepo();
        BalanceSheetService balanceSheetService = new BalanceSheetService();
        ExpenseService expenseService = new ExpenseService(balanceSheetService);
        DebtSimplificationService simplificationService = new DebtSimplificationService();

        GroupService groupService = new GroupService(repo, expenseService, simplificationService);

        /* ---------- create groups ---------- */
        String goaGroupId = groupService.createGroup("Goa Trip", List.of(shubh, bob, tom));
        String miscGroupId  = groupService.createGroup("Non-Group Expenses", List.of(shubh, bob, tom, jake));

        /* ---------- add expenses ---------- */
        groupService.addExpense(goaGroupId,
                "Lunch Day-1", 100, shubh,
                List.of(shubh, bob), SplitType.EQUAL, null);

        groupService.addExpense(goaGroupId,
                "Lunch Day-2", 100, bob,
                List.of(bob, tom), SplitType.EQUAL, null);

        /* ---------- simplify & print ---------- */
        groupService.simplifyDebts(goaGroupId);
        groupService.printBalances(goaGroupId);
    }
}
