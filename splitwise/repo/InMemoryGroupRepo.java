package splitwise.repo;
import java.util.HashMap;

import splitwise.model.Group;

public class InMemoryGroupRepo implements GroupRepo{
    public HashMap<String,Group> store= new HashMap<>();
    public  Group findById(String id){
        return store.get(id);
    }
    public void save(Group group){
        store.putIfAbsent(group.id,group);
    }
}