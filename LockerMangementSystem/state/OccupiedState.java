package state;

import entity.Locker;
import entity.Package;

public class OccupiedState implements LockerState {
    @Override
    public void occupyLocker(Locker context, entity.Package pkg, String code) {
        System.out.println("Locker is already occupied.");
    }

    @Override
    public Package vacateLocker(Locker context) {
        System.out.println("Vacating the locker...");
        Package vacatedPackage = context.getPackage(); // Get package before nulling it
        context.setPackage(null,"");
        context.setState(new AvailableState());
        return vacatedPackage;
    }

    @Override
    public void maintainLocker(Locker context) {
        System.out.println("Cannot maintain an occupied locker. Please vacate it first.");
    }
    
}
