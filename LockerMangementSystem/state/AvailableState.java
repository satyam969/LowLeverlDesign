package state;

import entity.Locker;
import entity.Package;

public class AvailableState implements LockerState {
    @Override
    public void occupyLocker(Locker context, entity.Package pkg, String code) {
        // Logic to occupy the locker with the given package
        context.setPackage(pkg, code);
        context.setState(new OccupiedState()); // Transition to OccupiedState
    }

    @Override
    public Package vacateLocker(Locker context) {
        // Logic to vacate the locker
        System.out.println("Locker is already available. No action needed.");
        return null;
    }

    @Override
    public void maintainLocker(Locker context) {
        // Logic to put the locker under maintenance
        System.out.println("Putting locker under maintenance.");
        context.setState(new MaintenanceState()); // Transition to MaintenanceState
    }
    
}
