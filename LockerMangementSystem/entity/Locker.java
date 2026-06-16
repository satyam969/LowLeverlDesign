package entity;

import enums.LockerSize;
import state.AvailableState;
import state.LockerState;


public class Locker {
    private Package pkg;
    private String LockerId;
    private LockerSize LockerSize;
    private boolean isOccupied;
    private LockerState state;
    private String Code;

    public Locker(String lockerId, LockerSize lockerSize) {
        this.LockerId = lockerId;
        this.LockerSize = lockerSize;
        this.isOccupied = false;
        this.state = new AvailableState(); 
    }


    public void occupyLocker(Package pkg,String code) {
        state.occupyLocker(this, pkg,code);
    }

    public Package vacateLocker() {
        return state.vacateLocker(this);
    }
    public String getId(){
        return LockerId;
    }

    public void setState(LockerState state) {
        this.state = state;
    }

    public void setPackage(Package pkg,String code) {
        this.pkg=pkg;
        this.Code = code;
        isOccupied = (pkg != null);

    }

    public Package getPackage(String code) {
        if (this.Code.equals(code)) {
            return pkg;
        } else {
            System.out.println("Invalid code. Access denied.");
            return null;
        }
    }
    
    public void maintainLocker() {
        state.maintainLocker(this);
    }

    public LockerSize getSize() {
        return LockerSize;
    }

    public void removePackage() {
        this.pkg = null;
        isOccupied = false;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public Package getPackage() {
        return pkg;
    }

    public String getLockerId() {
        return LockerId;
    }

    public LockerState getState() {
        return this.state;
    }

}
