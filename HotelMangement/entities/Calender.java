package entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Calender {
    private List<Reservation> reservations = new ArrayList<>();
    public boolean isAvailable(LocalDateTime checkIn,LocalDateTime checkOut){
        // extract Date Time and use To To Find a Slot 
        for(Reservation existing : reservations){
            if(existing.getCheckIn().isBefore(checkOut) && existing.getCheckOut().isAfter(checkIn)){
                return false;
            }
        }
        return true;
    }
    public void addReservation(Reservation r){
        reservations.add(r);
    }
    public void removeReservation(Reservation r){
        reservations.removeIf(reserv -> reserv.getReservationId().equals(r.getReservationId()));
    }
}
