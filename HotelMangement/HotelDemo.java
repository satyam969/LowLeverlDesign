import context.Room;
import entities.Guest;
import entities.Reservation;
import enums.RoomType;
import strategy.OnSeasonStrategy;
import strategy.OffSeasonStrategy;

import java.time.LocalDateTime;
import java.util.List;

public class HotelDemo {
    // demo code for hotel management system
    public static void main(String[] args) {
        HotelSystem hotel = HotelSystem.getInstance();

        // Add rooms with pricing strategy
        hotel.addRoom(new Room(101, RoomType.SINGLE, 100, new OnSeasonStrategy()));
        hotel.addRoom(new Room(102, RoomType.DOUBLE, 150, new OffSeasonStrategy()));
        hotel.addRoom(new Room(201, RoomType.SUITE,  300, new OnSeasonStrategy()));

        // Add guest
        Guest guest1 = new Guest("G001", "Alice", "alice@example.com", "9999999999");
        hotel.addGuest(guest1);

        // Search available rooms
        LocalDateTime checkIn  = LocalDateTime.of(2024, 7, 1, 14, 0);
        LocalDateTime checkOut = LocalDateTime.of(2024, 7, 5, 11, 0);
        System.out.println("=== Available Rooms ===");
        List<Room> available = hotel.searchAvailableRooms(checkIn, checkOut);
        for (Room room : available) {
            System.out.println(room + " | Price/night: " + room.finalPrice());
        }

        // Make a reservation
        System.out.println("\n=== Making Reservation ===");
        hotel.makeReservation(guest1, hotel.getRooms().get(0), checkIn, checkOut);

        // Try to double-book same room (should be blocked)
        System.out.println("\n=== Attempting Double-booking ===");
        hotel.makeReservation(guest1, hotel.getRooms().get(0), checkIn, checkOut);

        // Check guest reservation history
        System.out.println("\n=== Guest Reservation History ===");
        for (Reservation r : guest1.getReservations()) {
            System.out.println("  Reservation: " + r.getReservationId() + " | Status: " + r.getStatus());
        }

        // Check-in
        System.out.println("\n=== Check-In ===");
        String resId = guest1.getReservations().get(0).getReservationId();
        hotel.checkIn(resId);

        // Check-out
        System.out.println("\n=== Check-Out ===");
        hotel.checkOut(resId);
    }
}
