package strategy;

import java.util.Map;

import entity.Locker;

public interface LockerStrategy {
    Locker getLockerForPackage(entity.Package pkg, Map<String,Locker> lockers);
}
