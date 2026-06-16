package observer;

public class SmsObserver extends Observer {
    public SmsObserver(String name, String contactInfo) {
        super(name, contactInfo);
    }

    @Override
    public void update(String message) {
        // Simulate sending an SMS
        System.out.println("SMS sent to " + super.contactInfo + ": " + message);
    }

}
