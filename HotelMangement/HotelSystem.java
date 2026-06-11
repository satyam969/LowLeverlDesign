import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import context.Room;
import entities.Guest;
import processor.ReservationProcessor;

public class HotelSystem {
    private static HotelSystem instance;
    private static ReservationProcessor reservationProcessor;
    private static final Lock lck = new ReentrantLock();
    private final List<Room> rooms = new ArrayList<>();
    private final List<Guest> guests = new ArrayList<>();
    private HotelSystem(){
        reservationProcessor = new ReservationProcessor();
    }
    public static HotelSystem getInstance(){
        if(instance==null){
            lck.lock();
            try{
                if(instance==null){
                    instance=new HotelSystem();
                }
            } finally {
                lck.unlock();
            }
        }
        return instance;
    }
    public void makeReservation(Guest g,Room r,java.time.LocalDateTime from,java.time.LocalDateTime to){
        reservationProcessor.makeReservation(g,r,from,to);
    }
    public void cancelReservation(String reservationId){
        reservationProcessor.cancelReservation(reservationId);
    }
    public void checkIn(String reservationId){
        reservationProcessor.checkIn(reservationId);
    }
    public void checkOut(String reservationId){
        reservationProcessor.checkOut(reservationId);
    }
    public void addRoom(Room r){
        rooms.add(r);
    }
    public void addGuest(Guest g){
        guests.add(g);
    }
    public List<Room> getRooms(){
        return rooms;
    }
    public List<Guest> getGuests(){
        return guests;
    }
    public List<Room> searchAvailableRooms(LocalDateTime from, LocalDateTime to){
        List<Room> available = new ArrayList<>();
        for(Room r : rooms){
            if(r.isAvailable(from, to)){
                available.add(r);
            }
        }
        return available;
    }

}
