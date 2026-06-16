package service;

import java.time.LocalDateTime;

public class ExpirationService {
    private static ExpirationService instance;
    private ExpirationService() {
        // private constructor to prevent instantiation
    }
    public static ExpirationService getInstance() {
        if (instance == null) {
            synchronized (ExpirationService.class) {
                if (instance == null) {
                    instance = new ExpirationService();
                }
            }
        }
        return instance;
    }
    
    boolean isPackageExpired(entity.Package pkg) {
        LocalDateTime now = LocalDateTime.now();
        if(pkg.getEntryTime().plusDays(7).isBefore(now)) {
            return true;
        }
        return false; 
    }
}
