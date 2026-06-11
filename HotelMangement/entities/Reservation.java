package entities;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import context.Room;
import enums.ReserveStatus;

public class Reservation {
    private String reservationId;
    private Guest guest;
    private Room room;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private ReserveStatus status;
    private double totalPrice;
    public Reservation(String reservationId,Guest guest,Room room,LocalDateTime checkIn,LocalDateTime checkOut){
        this.reservationId=reservationId;
        this.guest=guest;
        this.room=room;
        this.checkIn=checkIn;
        this.checkOut=checkOut;
        this.totalPrice=room.finalPrice() * ChronoUnit.DAYS.between(checkIn.toLocalDate(), checkOut.toLocalDate());
        this.status=ReserveStatus.PENDING;
    }
    public void confirm(){
        status=ReserveStatus.CONFIRMED;
    }
    public void complete(){
        status=ReserveStatus.COMPLETED;
    }
    public void cancel(){
        status=ReserveStatus.CANCELLED;
        room.getCalendar().removeReservation(this);
        room.release();
    }
    public String getReservationId(){
        return reservationId;
    }
    public Guest getGuest(){
        return guest;
    }
    public Room getRoom(){
        return room;
    }
    public LocalDateTime getCheckIn(){
        return checkIn;
    }
    public LocalDateTime getCheckOut(){
        return checkOut;
    }
    public ReserveStatus getStatus(){
        return status;
    }
}
