package splitwise.model;
public class Split {
    public User user;
    public double amount;
    public Split(User user,double amount){
        this.user=user;
        this.amount=amount;
    }
    public User getUser(){
        return user;
    }
    public double getAmount(){
        return amount;
    }
}
