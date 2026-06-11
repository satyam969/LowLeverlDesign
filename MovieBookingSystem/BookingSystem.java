import model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class BookingSystem {
    public List<Theater> theaters;
    public Map<String, Booking> bookings;
    public BookingSystem(List<Theater> theaters){
        this.theaters = theaters;
        this.bookings = new HashMap<>();
    }
    public void addTheater(Theater theater){
        this.theaters.add(theater);
    }
    public Booking createBooking(Show show, List<Seat> seats, String bookingId){
        // Attempt to book seats first
        if (show.bookSeats(seats)) {
            Booking booking = new Booking(show, seats, bookingId);
            bookings.put(bookingId, booking);
            return booking;
        } else {
            throw new IllegalArgumentException("Selected seats are not available for show: " + show.movie.title);
        }
    }
    public Booking getBooking(String bookingId){
        return bookings.get(bookingId);
    }
    // 100ms timeout for demonstration (faster test)
    private static final long LOCK_TIMEOUT = 100; 

    public void confirmBooking(String bookingId){
        Booking booking = bookings.get(bookingId);
        if(booking != null){
            if (booking.isExpired(LOCK_TIMEOUT)) {
                // Determine if we should automatically cancel. 
                // For this requirement: "he /she should not be able to book" -> yes, fail confirmation.
                // Optionally release seats immediately.
                cancelBooking(bookingId); 
                throw new IllegalStateException("Booking expired. Lock released.");
            }
            booking.confirmBooking();
        }
    }
    public void cancelBooking(String bookingId){
        Booking booking = bookings.get(bookingId);
        if(booking != null){
            booking.cancelBooking();
            booking.show.cancelSeats(booking.bookedSeats);
        }
    }
    public List<Theater> getTheaters(){
        return this.theaters;
    }
    public void PrintBookings(){
        for(Map.Entry<String, Booking> entry : bookings.entrySet()){
            System.out.println("Booking ID: " + entry.getKey() + ", Status: " + entry.getValue().status);
        }
    }
}
