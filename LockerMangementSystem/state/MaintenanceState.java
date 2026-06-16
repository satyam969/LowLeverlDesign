package state;

import entity.Locker;
import entity.Package;

public class MaintenanceState implements LockerState {
    @Override
    public void occupyLocker(Locker context, entity.Package pkg, String code) {
        System.out.println("Cannot occupy a locker under maintenance.");
    }

    @Override
    public Package vacateLocker(Locker context) {
        System.out.println("Vacating the locker...");
        context.setPackage(null,"");
        context.setState(new AvailableState());
        return context.getPackage();
    }

    @Override
    public void maintainLocker(Locker context) {
        System.out.println("Locker is already under maintenance.");
    }
}
