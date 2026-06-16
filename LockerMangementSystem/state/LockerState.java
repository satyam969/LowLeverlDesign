package state;

import entity.Locker;
import entity.Package;

public interface LockerState {
    void occupyLocker(Locker context,entity.Package pkg,String code);
    Package vacateLocker(Locker context);
    void maintainLocker(Locker context);
}
