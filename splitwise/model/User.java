package splitwise.model;

public class User {
    public String id;
    public String name;
    public User(String id,String name){
        this.id=id;
        this.name=name;
    }
    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
}
