package states;

import context.Room;
import entities.Reservation;

public class OccupiedState implements RoomState {
    @Override
    public void reserve(Room context, Reservation r) {
        throw new IllegalStateException("Room " + context.getNumber() + " is OCCUPIED, cannot reserve.");
    }
    @Override
    public void occupy(Room context) {
        System.out.println("Room " + context.getNumber() + " is already OCCUPIED.");
    }
    @Override
    public void release(Room context) {
        // Called on checkout — room returns to available
        context.setState(new AvailableState());
        System.out.println("Room " + context.getNumber() + " guest checked out, now AVAILABLE.");
    }
    @Override
    public void maintenance(Room context) {
        context.setState(new MaintenanceState());
        System.out.println("Room " + context.getNumber() + " is now under MAINTENANCE.");
    }
}
