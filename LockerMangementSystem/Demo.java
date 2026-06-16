
import entity.Locker;
import entity.Package;
import enums.LockerSize;
import manager.LockerHub;
import manager.PackageManager;

import service.ExpirationService;
import service.LockerService;
import service.NotificationService;
import service.SearchService;
import strategy.BestFitStrategy;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Demo {
    public static void main(String[] args) {
        // Initialize dependencies
        Map<String, Map<String, Locker>> allLockers = new HashMap<>();
        Map<String, Locker> areaALockers = new HashMap<>();
        areaALockers.put("L1", new Locker("L1", LockerSize.SMALL));
        areaALockers.put("L2", new Locker("L2", LockerSize.MEDIUM));
        areaALockers.put("L3", new Locker("L3", LockerSize.LARGE));
        areaALockers.put("L4", new Locker("L4", LockerSize.XL));
        allLockers.put("AreaA", areaALockers);

        Map<String, Locker> areaBLockers = new HashMap<>();
        areaBLockers.put("L5", new Locker("L5", LockerSize.SMALL));
        areaBLockers.put("L6", new Locker("L6", LockerSize.MEDIUM));
        allLockers.put("AreaB", areaBLockers);

        LockerHub lockerHub = new LockerHub(allLockers);
        BestFitStrategy bestFitStrategy = new BestFitStrategy();
        ExpirationService expirationService = ExpirationService.getInstance();
        PackageManager packageManager = new PackageManager(new HashMap<>());
        
        NotificationService notificationService = new NotificationService(); 

        LockerService lockerService = new LockerService(lockerHub, bestFitStrategy, expirationService);
        SearchService searchService = new SearchService(packageManager);

        // Initialize LockerSystem
        LockerSystem lockerSystem = LockerSystem.getInstance();
        lockerSystem.initializeSystem(lockerService, notificationService, searchService);

        System.out.println("--- Initializing Locker System ---");
        System.out.println("Lockers in Area A: " + lockerHub.getLockersByArea("AreaA").keySet());
        System.out.println("Lockers in Area B: " + lockerHub.getLockersByArea("AreaB").keySet());

        System.out.println("\n--- Assigning Packages ---");
        Package pkg1 = new Package("P001", "Alice", "1234567890", "alice@example.com", LockerSize.MEDIUM);
        lockerSystem.assignLocker(pkg1, "AreaA");

        Package pkg2 = new Package("P002", "Bob", "0987654321", "bob@example.com", LockerSize.SMALL);
        lockerSystem.assignLocker(pkg2, "AreaA");

        Package pkg3 = new Package("P003", "Charlie", "1122334455", "charlie@example.com", LockerSize.XL);
        lockerSystem.assignLocker(pkg3, "AreaB");

        // Try to assign a package that cannot be accommodated
        Package pkg4 = new Package("P004", "David", "5544332211", "david@example.com", LockerSize.XL);
        lockerSystem.assignLocker(pkg4, "AreaA"); 

        System.out.println("\n--- Searching for Packages ---");
        String lockerIdForPkg1 = searchService.getLockerIdForPackage("P001");
        System.out.println("Package P001 is in Locker: " + (lockerIdForPkg1 != null ? lockerIdForPkg1 : "Not Found"));

        String lockerIdForPkg3 = searchService.getLockerIdForPackage("P003");
        System.out.println("Package P003 is in Locker: " + (lockerIdForPkg3 != null ? lockerIdForPkg3 : "Not Found"));

        String lockerIdForNonExistentPkg = searchService.getLockerIdForPackage("P999");
        System.out.println("Package P999 is in Locker: " + (lockerIdForNonExistentPkg != null ? lockerIdForNonExistentPkg : "Not Found"));

        System.out.println("\n--- Retrieving Packages (Vacating Lockers) ---");
        Package retrievedPkg2 = lockerService.vacateLocker("L2", "AreaA");
        if (retrievedPkg2 != null) {
            System.out.println("Retrieved package: " + retrievedPkg2.getPackageId() + " from L2.");
        } else {
            System.out.println("Failed to retrieve package from L2.");
        }

        System.out.println("\n--- Locker Maintenance ---");
        lockerSystem.maintainLocker("L3", "AreaA"); 
        lockerSystem.maintainLocker("L5", "AreaB"); 

        // Try to assign to a locker under maintenance
        Package pkg5 = new Package("P005", "Eve", "9988776655", "eve@example.com", LockerSize.SMALL);
        lockerSystem.assignLocker(pkg5, "AreaB"); 

        System.out.println("\n--- Checking for Expired Packages ---");

        // Manually setting an entry time to simulate expiration for pkg3
        // This is not ideal for a real system but works for a demo
        Locker lockerForPkg3 = lockerHub.getLockersByArea("AreaB").get(searchService.getLockerIdForPackage("P003"));
        if (lockerForPkg3 != null && lockerForPkg3.getPackage() != null) {
            // Accessing pkg3 and modifying its entry time
            Package originalPkg3 = lockerForPkg3.getPackage();
            originalPkg3.entryTime = LocalDateTime.now().minusDays(8); // Set to 8 days ago
            System.out.println("Simulating expiration for P003 (in L" + lockerForPkg3.getId() + "): Entry Time set to " + originalPkg3.getEntryTime());
        }

        lockerSystem.checkForExpiredPackages();

        Package retrievedPkg1 = lockerService.vacateLocker("L1", "AreaA"); 
        if (retrievedPkg1 != null) {
            System.out.println("Retrieved package: " + retrievedPkg1.getPackageId() + " from L1.");
        } else {
            System.out.println("Failed to retrieve package from L1 (might be already empty or under maintenance).");
        }
    }
}
