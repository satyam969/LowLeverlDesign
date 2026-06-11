package entities;
import vehicleEnum.VehicleType;

public class Vehicle {
    public String licensePlate;
    public String color;
    public VehicleType type;
    public Vehicle(String licensePlate, String color, VehicleType type) {
        this.licensePlate = licensePlate;
        this.color = color;
        this.type = type;
    }
    public VehicleType getType() {
        return this.type;
    }
}
