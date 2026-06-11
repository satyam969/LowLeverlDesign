package states;

import context.Room;
import entities.Reservation;

public class MaintenanceState implements RoomState {
    @Override
    public void reserve(Room context, Reservation r) {
        throw new IllegalStateException("Room " + context.getNumber() + " is under MAINTENANCE, cannot reserve.");
    }
    @Override
    public void occupy(Room context) {
        throw new IllegalStateException("Room " + context.getNumber() + " is under MAINTENANCE, cannot occupy.");
    }
    @Override
    public void release(Room context) {
        // Maintenance complete — return to Available
        context.setState(new AvailableState());
        System.out.println("Room " + context.getNumber() + " maintenance complete, now AVAILABLE.");
    }
    @Override
    public void maintenance(Room context) {
        System.out.println("Room " + context.getNumber() + " is already under MAINTENANCE.");
    }
}
