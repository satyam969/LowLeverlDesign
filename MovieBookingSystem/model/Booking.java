package model;
import java.util.List;

public class Booking {
    public Show show;
    public List<Seat> bookedSeats;
    public String bookingId;
    public BookingStatus status;
    public Booking(Show show, List<Seat> bookedSeats, String bookingId){
        this.show = show;
        this.bookedSeats = bookedSeats;
        this.bookingId = bookingId;
        this.status = BookingStatus.PENDING;
        this.creationTime = System.currentTimeMillis();
    }

    private final long creationTime;

    public boolean isExpired(long timeoutMillis) {
        return (System.currentTimeMillis() - creationTime) > timeoutMillis;
    }
    
    public synchronized void confirmBooking(){
        if(this.status != BookingStatus.PENDING){
            throw new IllegalStateException("Booking is not in PENDING state");
        }
        this.status = BookingStatus.CONFIRMED;
    }

    public synchronized void cancelBooking(){
        this.status = BookingStatus.CANCELLED;
    }
}
