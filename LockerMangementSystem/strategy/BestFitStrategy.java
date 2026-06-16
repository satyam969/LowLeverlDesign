package strategy;

import java.util.Map;

import entity.Locker;
import state.AvailableState;

public class BestFitStrategy implements LockerStrategy {
    public Locker getLockerForPackage(entity.Package pkg, Map<String,Locker> lockers) {
        // logic to find the best fit locker for the package
        // it will iterate through the list of lockers and find the locker which is just enough to accommodate the package
        // it will return the locker which is best fit for the package
        Locker bestFitLocker = null;
        for (Locker locker : lockers.values()) {
            // Only consider lockers that are in an AvailableState
            if (locker.getState() instanceof AvailableState && locker.getSize().ordinal() >= pkg.getSize().ordinal()) {
                if (bestFitLocker == null || locker.getSize().ordinal() < bestFitLocker.getSize().ordinal()) {
                    bestFitLocker = locker;
                }
            }
        }
        return bestFitLocker;
    }
}
