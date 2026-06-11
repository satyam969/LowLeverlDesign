package parkingalgo;

import java.util.List;

import entities.Level;
import entities.ParkingSpot;
import entities.Vehicle;
import startegies.ParkingStrategy;

public class NearestFirstStrategy implements ParkingStrategy {
    private List<Level> levels;

    public NearestFirstStrategy(List<Level> levels) {
        this.levels = levels;
    }

    @Override
    public ParkingSpot findParkingSpot(Vehicle vehicle) {
        for (Level level : levels) {
            for (ParkingSpot spot : level.getParkingSpots()) {
                if (spot.isAvailable() && spot.canFitVehicle(vehicle)) {
                    return spot;
                }
            }
        }
        return null; 
    }    
    @Override
    public void addSpot(ParkingSpot spot) {
        // NearestFirstStrategy does not maintain state, it iterates every time.
        // So we don't need to explicitly add it back to a queue.
    }
}
