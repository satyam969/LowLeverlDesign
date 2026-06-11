package entities;

import vehicleEnum.VehicleType;

public class ParkingSpot implements Comparable<ParkingSpot> {
    public int id;
    public int levelNumber;
    public VehicleType type;
    public boolean isOccupied;
    public Vehicle vehicle;
    public ParkingSpot(int id, int levelNumber, VehicleType type) {
        this.id = id;
        this.levelNumber = levelNumber;
        this.type = type;
        this.isOccupied = false;
    }
    
    @Override
    public int compareTo(ParkingSpot other) {
        if (this.levelNumber != other.levelNumber) {
            return this.levelNumber - other.levelNumber;
        }
        return this.id - other.id;
    }
    public synchronized boolean isAvailable() {
        return !isOccupied;
    }

    public synchronized boolean canFitVehicle(Vehicle vehicle) {
        return this.type == vehicle.getType();
    }

    public synchronized boolean park(Vehicle vehicle) {
        if (isOccupied) {
            return false;
        }
        this.vehicle = vehicle;
        this.isOccupied = true;
        return true;
    }
    
    public synchronized Vehicle getVehicle() {
        return this.vehicle;
    }

    public synchronized void unPark() {
        this.vehicle = null;
        this.isOccupied = false;
    }

}
