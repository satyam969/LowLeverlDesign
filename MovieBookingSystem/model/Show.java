package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Show {
    public Movie movie;
    public String showtime;
    public int totalSeats;
    public List<Seat> seats;
    private final ReentrantLock showLock = new ReentrantLock();

    public Show(Movie movie, String showtime){
        this.movie = movie;
        this.showtime = showtime;
    }
    public void addSeats(List<Seat> seats){
        this.seats = seats;
        this.totalSeats = seats.size();
    }

    // lock the whole show while checking and bookings seats similarly for cancellation  

    public boolean bookSeats(List<Seat> seatsToBook){
        // Try to acquire the lock with a timeout of, say, 1 second
        try {
            if (showLock.tryLock(1, TimeUnit.SECONDS)) { // Attempt to acquire lock for 1 second
                try {
                    for(Seat seat : seatsToBook){
                        if(!seat.checkAvailability()){
                            return false; // Not all seats are available
                        }
                    }
                    for(Seat seat : seatsToBook){
                        seat.bookSeat();
                    }
                    return true;
                } finally {
                    showLock.unlock(); // Ensure the lock is released
                }
            }
            else {
                // Could not acquire lock within the timeout
                System.out.println("Booking for show " + movie.title + " timed out while acquiring lock.");
                return false;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Booking for show " + movie.title + " interrupted while acquiring lock.");
            return false;
        }
    }

    public void cancelSeats(List<Seat> seatsToCancel){
        // For cancel, we might still want to use a lock, but perhaps without a timeout
        // or a different timeout strategy depending on requirements.
        // For simplicity, let's assume a basic lock for now.
        showLock.lock();
        try {
            for(Seat seat : seatsToCancel){
                seat.cancelSeat();
            }
        } finally {
            showLock.unlock();
        }
    }
}
