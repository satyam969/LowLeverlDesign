package states;

import context.Room;
import entities.Reservation;

public class ReservedState implements RoomState {
    @Override
    public void reserve(Room context, Reservation r) {
        throw new IllegalStateException("Room " + context.getNumber() + " is already RESERVED.");
    }
    @Override
    public void occupy(Room context) {
        context.setState(new OccupiedState());
        System.out.println("Room " + context.getNumber() + " is now OCCUPIED (guest checked in).");
    }
    @Override
    public void release(Room context) {
        // Called on cancellation — room returns to available
        context.setState(new AvailableState());
        System.out.println("Room " + context.getNumber() + " reservation cancelled, now AVAILABLE.");
    }
    @Override
    public void maintenance(Room context) {
        throw new IllegalStateException("Room " + context.getNumber() + " is RESERVED, cannot send to maintenance.");
    }
}
