package processor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import context.Room;
import entities.Guest;
import entities.Reservation;

public class ReservationProcessor {
    private final Map<String,Reservation> reservations = new HashMap<>();
    public void makeReservation(Guest g, Room r, LocalDateTime from, LocalDateTime to){
        if(!r.isAvailable(from, to)){
            System.out.println("Room " + r.getNumber() + " is not available for the selected dates.");
            return;
        }
        Reservation res = new Reservation(UUID.randomUUID().toString(), g, r, from, to);
        reservations.put(res.getReservationId(), res);
        g.addReservation(res);
        r.reserve(res);
    }
    public void cancelReservation(String reservationId){
        if(reservations.containsKey(reservationId)){
            reservations.get(reservationId).cancel();
        }
    }
    public void checkIn(String reservationId){
        if(reservations.containsKey(reservationId)){
            reservations.get(reservationId).confirm();
            reservations.get(reservationId).getRoom().occupy();
        }
    }
    public void checkOut(String reservationId){
        if(reservations.containsKey(reservationId)){
            reservations.get(reservationId).complete();
            reservations.get(reservationId).getRoom().release();
        }
    }
}
