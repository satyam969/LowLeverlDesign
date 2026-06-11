package splitwise.strategy;
import splitwise.model.User;
import splitwise.model.Split;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class EqualSplitStrategy implements SplitStrategy{
    public List<Split> split(double amount,List<User> participants,Map<User,Double> metadata){
        int total=participants.size();
        double perhead=amount/total;
        List<Split> res=new ArrayList<>(total);
        for(User user: participants){
            Split curr=new Split(user,perhead);
            res.add(curr);
        }
        return res;
    }
}
