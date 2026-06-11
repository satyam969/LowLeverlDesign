package model;

public class Seat {
    public String seatNumber;
    public boolean isAvailable;
    public Seat(String seatNumber, boolean isAvailable){
        this.seatNumber = seatNumber;
        this.isAvailable = isAvailable;
    }
    public synchronized void bookSeat(){
        this.isAvailable = false;
    }
    public synchronized void cancelSeat(){
        this.isAvailable = true;
    }
    public boolean checkAvailability(){
        return this.isAvailable;
    }
}