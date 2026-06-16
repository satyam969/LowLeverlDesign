package service;

import java.rmi.server.UID;
import java.util.HashMap;
import java.util.Map;

import observer.Observer;

public class NotificationService {

    private Map<String, Observer> observers = new HashMap<>();

    public NotificationService() {
        // Default constructor to initialize the observers map
    }

    public String addObserver(Observer observer) {
        String uid = new UID().toString();
        observers.put(uid, observer);
        return uid;
    }

    public void removeObserver(String uid) {
        observers.remove(uid);
    }

    public void notifyAllObservers(String message) {
        for (Observer observer : observers.values()) {
            observer.update(message);
        }
    }

    public void notifyObservers(String message, String... uids) {
        for (String uid : uids) {
            Observer observer = observers.get(uid);
            if (observer != null) {
                observer.update(message);
            }
        }
    }
    
}
