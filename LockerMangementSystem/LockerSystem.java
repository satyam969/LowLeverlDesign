import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import entity.AccessCode;
import entity.Locker;
import service.LockerService;
import service.NotificationService;
import service.SearchService;
import entity.Package;
import observer.EmailObserver;
import observer.SmsObserver;

public class LockerSystem {
    private static LockerSystem instance;
    private static Lock lck = new ReentrantLock();
    private LockerService lockerService;
    private NotificationService notificationService;
    private SearchService searchService;
    private AccessCode accessCode;
    private Map<String, List<String>> packageNotificationUids = new HashMap<>(); // To store UIDs for notifications per package
    private LockerSystem() {
        // private constructor to prevent instantiation
    }
    public static LockerSystem getInstance() {
        if (instance == null) {
            lck.lock();
            try {
                if (instance == null) {
                    instance = new LockerSystem();
                }
            } finally {
                lck.unlock();
            }
        }
        return instance;
    }

    public void initializeSystem(LockerService lockerService, NotificationService notificationService,SearchService searchService) {
        this.lockerService = lockerService;
        this.notificationService = notificationService;
        this.searchService = searchService;
        this.accessCode = AccessCode.getInstance(); // Initialize AccessCode here
    }

    public void assignLocker(Package pkg,String area) {
        String code = accessCode.getAccessCode();
        Locker locker=lockerService.assignLocker(pkg,area,code);
        if (locker != null) {
            searchService.addPackage(pkg.getId(), locker.getId());
        } else {
            System.out.println("Package " + pkg.getPackageId() + " could not be assigned to a locker.");
            return; // Exit if no locker was assigned
        }
        String Email = pkg.getRecipientEmail();
        String phone = pkg.getRecipientNumber();
        String uid1 = notificationService.addObserver(new EmailObserver(pkg.getRecipientName(), Email));
        String uid2 = notificationService.addObserver(new SmsObserver(pkg.getRecipientName(), phone));
        
        List<String> uids = new ArrayList<>();
        uids.add(uid1);
        uids.add(uid2);
        packageNotificationUids.put(pkg.getId(), uids);

        // it should be a specific oberver for the package and it should be added to the notification service when the locker is assigned to the package and then the notification service should notify the observer with the access code and locker details
        notificationService.notifyObservers("Your package has been assigned to a locker. Access code: " + code, uid1, uid2);
    }

    public void vacateLocker(String lockerId, String area) {
        Package pkg = lockerService.vacateLocker(lockerId, area);
        // it should also remove the observer for the package which is vacating the locker
        if (pkg != null) {
            List<String> uids = packageNotificationUids.get(pkg.getId());
            if (uids != null) {
                notificationService.notifyObservers("Your package has been vacated from the locker. Locker ID: " + lockerId, uids.toArray(new String[0]));
                for (String uid : uids) {
                    notificationService.removeObserver(uid);
                }
                packageNotificationUids.remove(pkg.getId());
            }
        }
    }

    public void maintainLocker(String lockerId, String area) {
        lockerService.maintainLocker(lockerId, area);
    }

    public void checkForExpiredPackages() {
        lockerService.checkForExpiredPackages(notificationService);
    }

}
