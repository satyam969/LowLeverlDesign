package splitwise.repo;
import splitwise.model.Group;

public interface GroupRepo {
    public void save(Group group1);
    public Group findById(String id);
}
