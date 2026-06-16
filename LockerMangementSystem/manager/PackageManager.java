package manager;

import java.util.HashMap;
import java.util.Map;

public class PackageManager {
    // package Id to LockerId Mapping
    private Map<String,String> lockerMap=new HashMap<>(); // lockerId to Locker mapping

    public PackageManager(Map<String,String> lockerMap){
        this.lockerMap=lockerMap;
    }

    public void addPackageToLocker(String packageId,String lockerId){
        lockerMap.put(packageId, lockerId);
    }

    public String getLockerIdForPackage(String packageId){
        return lockerMap.get(packageId);
    }

}
