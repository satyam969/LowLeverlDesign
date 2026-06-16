package service;

import manager.PackageManager;

public class SearchService {
    private PackageManager packageManager;
    public SearchService(PackageManager packageManager){
        this.packageManager=packageManager;
    }
    public void addPackage(String pkgId,String lockerId){
        packageManager.addPackageToLocker(pkgId, lockerId);
    }
    public String getLockerIdForPackage(String pkgId){
        return packageManager.getLockerIdForPackage(pkgId);
    }
}
