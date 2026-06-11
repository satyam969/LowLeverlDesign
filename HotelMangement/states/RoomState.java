package states;

import context.Room;
import entities.Reservation;

public interface RoomState {
    void reserve(Room context,Reservation r);
    void occupy(Room context);
    void release(Room context);
    void maintenance(Room context);    
} 
