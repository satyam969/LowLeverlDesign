package observer;

public class EmailObserver extends Observer {
    public EmailObserver(String name, String contactInfo) {
        super(name, contactInfo);
    }

    @Override
    public void update(String message) {
        // Simulate sending an email
        System.out.println("Email sent to " + super.contactInfo + ": " + message);
    }
    
}
