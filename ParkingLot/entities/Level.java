package entities;

import java.util.List;

public class Level {
    public int levelNumber;
    public List<ParkingSpot> parkingSpots;
    public Level(List<ParkingSpot> parkingSpots) {
        this.parkingSpots = parkingSpots;
    }
    public List<ParkingSpot> getParkingSpots() {
        return this.parkingSpots;
    }
}
