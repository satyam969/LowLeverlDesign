package splitwise.model;
import java.util.HashMap;

public class BalanceSheet{
    // it will be for every person in the group 
    
    public HashMap<User,Double> balances=new HashMap<>();
    public double totalPaid=0.0;
    public double totalExpense=0.0;

    public void addTotalPaid(double amount)
    {
        totalPaid+=amount;
    }

    public double getTotalExpense(){
        return totalExpense;
    }

    public double getTotalPaid(){
        return totalPaid;
    }

    public HashMap<User, Double> getBalances() {
        return balances;
    }

    public void addTotalExpense(double amount){
        totalExpense+=amount;
    }

    public void addBalance(User other,double amount){
        balances.put(other,balances.getOrDefault(other, 0.0)+amount);
        if(balances.get(other)<1e-6)
            balances.remove(other);
    }

    public void clearBalance(){
        balances.clear();
    }
    
    public void print(User me){
        double youOwe=0.0,youGetBack=0.0;
        for(double amount : balances.values()){
            if(amount<0)
                youOwe+=-amount;
            else
                youGetBack+=amount;
        }

        System.out.println("You owe: "+youOwe);
        System.out.println("You get back: "+youGetBack);
        
        for(User user : balances.keySet()){
            double amount = balances.get(user);
            if(amount>0)
                System.out.println(user.name+" owes you: "+amount);
            else
                System.out.println("You owe "+user.name+": "+(amount) );
        }

    }

}