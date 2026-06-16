package manager;

import java.util.HashMap;

import java.util.Map;

import entity.Locker;

public class LockerHub {

    // class managing locker area wise

    private Map<String,Map<String,Locker>> Lockers=new HashMap<>();
    
    public LockerHub(Map<String,Map<String,Locker>> Lockers){
        this.Lockers=Lockers;
    }

    public Map<String,Map<String,Locker>> getAllLockers(){
        return Lockers;
    }

    public Map<String,Locker> getLockersByArea(String area){
        return Lockers.get(area);
    }

    public void addLocker(String area,Locker locker){
        Lockers.get(area).put(locker.getLockerId(), locker);
    }
    
}
