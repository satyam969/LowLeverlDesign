import java.util.List;

import entities.Level;
import entities.ParkingSpot;
import entities.Ticket;
import entities.Vehicle;
import feealgo.TimeBasedStrategy;
import parkingalgo.NearestFirstStrategy;
import vehicleEnum.VehicleType;

public class main {
    public static void main(String[] args) {
        // Level 1 spots
        ParkingSpot spot1_1 = new ParkingSpot(1, 1, VehicleType.CAR);
        ParkingSpot spot1_2 = new ParkingSpot(2, 1, VehicleType.BIKE);
        ParkingSpot spot1_3 = new ParkingSpot(3, 1, VehicleType.TRUCK);
        
        // Level 2 spots
        ParkingSpot spot2_1 = new ParkingSpot(1, 2, VehicleType.CAR);
        ParkingSpot spot2_2 = new ParkingSpot(2, 2, VehicleType.BIKE);
        ParkingSpot spot2_3 = new ParkingSpot(3, 2, VehicleType.TRUCK);
        ParkingSpot spot2_4 = new ParkingSpot(4, 2, VehicleType.CAR);

        // Create a parking lot with 2 levels
        List<Level> levels = List.of(
            new Level(List.of(spot1_1, spot1_2, spot1_3)),
            new Level(List.of(spot2_1, spot2_2, spot2_3, spot2_4))
        );

        ParkingSystem parkingLot = new ParkingSystem(
            levels,
            // Use the new PriorityQueue Strategy
            new parkingalgo.PriorityQueueParkingStrategy(levels),
            new TimeBasedStrategy()
        );

        // Create some vehicles
        Vehicle car1 = new Vehicle("ABC123", "Red", VehicleType.CAR);
        Vehicle bike1 = new Vehicle("BIKE456", "Blue", VehicleType.BIKE);
        Vehicle truck1 = new Vehicle("TRUCK789", "Black", VehicleType.TRUCK);

        // Park the vehicles
        Ticket ticket1 = parkingLot.parkVehicle(car1);
        System.out.println("Parked Car: " + ticket1.licensePlate + " at spot: " + ticket1.parkingSpot.type);

        Ticket ticket2 = parkingLot.parkVehicle(bike1);
        System.out.println("Parked Bike: " + ticket2.licensePlate + " at spot: " + ticket2.parkingSpot.type);

        Ticket ticket3 = parkingLot.parkVehicle(truck1);
        System.out.println("Parked Truck: " + ticket3.licensePlate + " at spot: " + ticket3.parkingSpot.type);

        // Unpark the vehicles
        parkingLot.unParkVehicle(car1.licensePlate);
        System.out.println("Unparked Car: " + ticket1.licensePlate);

        parkingLot.unParkVehicle(bike1.licensePlate);
        System.out.println("Unparked Bike: " + ticket2.licensePlate);

        parkingLot.unParkVehicle(truck1.licensePlate);
        System.out.println("Unparked Truck: " + ticket3.licensePlate);
    }
}
