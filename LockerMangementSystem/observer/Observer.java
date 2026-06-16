package observer;

public abstract class Observer {
    protected String name;
    protected String contactInfo;
    public Observer(String name, String contactInfo) {
        this.name = name;
        this.contactInfo = contactInfo;
    }
    public abstract void update(String message);
}
