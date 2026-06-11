package context;

import java.time.LocalDateTime;

import entities.Calender;
import entities.Reservation;
import enums.RoomType;
import states.AvailableState;
import states.RoomState;
import strategy.PricingStrategy;

public class Room {
    private int number;
    private RoomType type;
    private RoomState state;
    private double basePrice;
    private Calender calendar;
    private PricingStrategy pricing;

    public Room(int number, RoomType type, double basePrice, PricingStrategy pricing) {
        this.number = number;
        this.type = type;
        this.basePrice = basePrice;
        this.pricing = pricing;
        this.calendar = new Calender();
        this.state = new AvailableState(); // Default state
    }

    public void reserve(Reservation r) {
        state.reserve(this, r);
    }

    public void addToCalender(Reservation r) {
        calendar.addReservation(r);
    }

    public boolean isAvailable(LocalDateTime checkIn, LocalDateTime checkOut) {
        return calendar.isAvailable(checkIn, checkOut);
    }

    public void setState(RoomState nextState) {
        this.state = nextState;
    }

    public void occupy() {
        state.occupy(this);
    }

    public void release() {
        state.release(this);
    }

    public void maintenance() {
        state.maintenance(this);
    }

    public double finalPrice() {
        return pricing.calculatePrice(basePrice);
    }

    public Calender getCalendar() {
        return calendar;
    }

    public int getNumber() {
        return number;
    }

    public RoomType getType() {
        return type;
    }

    public double getBasePrice() {
        return basePrice;
    }

    @Override
    public String toString() {
        return "Room{number=" + number + ", type=" + type + ", basePrice=" + basePrice + "}";
    }
}
