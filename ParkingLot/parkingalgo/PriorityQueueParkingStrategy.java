package parkingalgo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

import entities.Level;
import entities.ParkingSpot;
import entities.Vehicle;
import vehicleEnum.VehicleType;
import startegies.ParkingStrategy;

public class PriorityQueueParkingStrategy implements ParkingStrategy {

    // Map: VehicleType -> PriorityQueue of available spots
    private Map<VehicleType, PriorityBlockingQueue<ParkingSpot>> availableSpots;

    public PriorityQueueParkingStrategy(List<Level> levels) {
        availableSpots = new ConcurrentHashMap<>();
        
        // Initialize queues for each vehicle type
        for (VehicleType type : VehicleType.values()) {
            availableSpots.put(type, new PriorityBlockingQueue<>());
        }

        // Populate queues with all spots from all levels
        for (Level level : levels) {
            for (ParkingSpot spot : level.getParkingSpots()) {
                availableSpots.get(spot.type).offer(spot);
            }
        }
    }

    @Override
    public ParkingSpot findParkingSpot(Vehicle vehicle) {
        // We ignore 'levels' here because we have our own index in 'availableSpots'
        PriorityBlockingQueue<ParkingSpot> queue = availableSpots.get(vehicle.getType());
        
        // Poll retrieves and removes the head of the queue (lowest level/id)
        // Returns null if queue is empty
        return queue.poll();
    }

    @Override
    public void addSpot(ParkingSpot spot) {
        // Add the spot back to the queue when unparked
        availableSpots.get(spot.type).offer(spot);
    }
}
