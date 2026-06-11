package states;

import context.Room;
import entities.Reservation;

public class AvailableState implements RoomState {
    @Override
    public void reserve(Room context, Reservation r) {
        context.addToCalender(r);
        context.setState(new ReservedState());
        System.out.println("Room " + context.getNumber() + " is now RESERVED.");
    }
    @Override
    public void occupy(Room context) {
        throw new IllegalStateException("Cannot occupy a room that hasn't been reserved.");
    }
    @Override
    public void release(Room context) {
        System.out.println("Room " + context.getNumber() + " is already AVAILABLE.");
    }
    @Override
    public void maintenance(Room context) {
        context.setState(new MaintenanceState());
        System.out.println("Room " + context.getNumber() + " is now under MAINTENANCE.");
    }
}
