import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import entities.Level;
import entities.Ticket;
import entities.Vehicle;
import startegies.FeeStrategy;
import startegies.ParkingStrategy;
import java.util.UUID;

public class ParkingSystem {
    public List<Level> levels;
    public ParkingStrategy parkingStrategy;
    public FeeStrategy feeStrategy;
    public ConcurrentHashMap<String,Ticket> parkedVehicles = new ConcurrentHashMap<>();
    public ParkingSystem(List<Level> levels, ParkingStrategy parkingStrategy, FeeStrategy feeStrategy) {
        this.levels = levels;
        this.parkingStrategy = parkingStrategy;
        this.feeStrategy = feeStrategy;
    }
    public String generateTicketId() {
        return UUID.randomUUID().toString();
    }
    public Ticket parkVehicle(Vehicle vehicle) {
        entities.ParkingSpot spot = parkingStrategy.findParkingSpot(vehicle);
        // Optimistic Locking: Try to park. If another thread took it, park() returns false.
        if (spot != null && spot.park(vehicle)) {
            Ticket ticket = new Ticket(generateTicketId(), vehicle.licensePlate, System.currentTimeMillis(), spot, vehicle);
            parkedVehicles.put(vehicle.licensePlate, ticket);
            return ticket;
        }
        // Ideally, we should retry finding another spot here if the first one was taken.
        return null;
    }
    public double unParkVehicle(String licensePlate) {
        Ticket ticket = parkedVehicles.get(licensePlate);
        if (ticket != null) {
            ticket.setExitTime(System.currentTimeMillis());
            double fee = feeStrategy.calculateFee(ticket);
            ticket.parkingSpot.unPark();
            // Critical: Add the spot back to the strategy (for PQ strategy)
            parkingStrategy.addSpot(ticket.parkingSpot);
            parkedVehicles.remove(licensePlate);
            return fee;
        }
        return -1; 
    }

}
