package startegies;
import java.util.List;

import entities.Level;
import entities.ParkingSpot;
import entities.Vehicle;

public interface ParkingStrategy {
    ParkingSpot findParkingSpot(Vehicle vehicle);
    void addSpot(ParkingSpot spot);
}
