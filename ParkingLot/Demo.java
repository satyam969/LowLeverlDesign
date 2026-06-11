import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import entities.Level;
import entities.ParkingSpot;
import entities.Ticket;
import entities.Vehicle;
import vehicleEnum.VehicleType;
import parkingalgo.PriorityQueueParkingStrategy;
import feealgo.TimeBasedStrategy;

public class Demo {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 1. Testing Priority Order (Level 1 vs Level 2) ===");
        testPriorityOrder();

        System.out.println("\n=== 2. Testing Concurrency (Race Conditions) ===");
        testConcurrency();
    }

    public static void testPriorityOrder() {
        // Setup: 2 Spots in Level 1, 2 Spots in Level 2
        ParkingSpot s1_1 = new ParkingSpot(1, 1, VehicleType.CAR);
        ParkingSpot s1_2 = new ParkingSpot(2, 1, VehicleType.CAR);
        ParkingSpot s2_1 = new ParkingSpot(1, 2, VehicleType.CAR);
        ParkingSpot s2_2 = new ParkingSpot(2, 2, VehicleType.CAR);

        List<Level> levels = List.of(
            new Level(List.of(s1_1, s1_2)),
            new Level(List.of(s2_1, s2_2))
        );

        ParkingSystem keySystem = new ParkingSystem(
            levels, 
            new PriorityQueueParkingStrategy(levels), 
            new TimeBasedStrategy()
        );

        // Park 3 cars
        Vehicle v1 = new Vehicle("CAR1", "Red", VehicleType.CAR);
        Vehicle v2 = new Vehicle("CAR2", "Blue", VehicleType.CAR);
        Vehicle v3 = new Vehicle("CAR3", "Green", VehicleType.CAR);

        Ticket t1 = keySystem.parkVehicle(v1);
        Ticket t2 = keySystem.parkVehicle(v2);
        Ticket t3 = keySystem.parkVehicle(v3);

        // Verify t1 and t2 are in Level 1, t3 is in Level 2
        printTicket(t1);
        printTicket(t2);
        printTicket(t3);
    }

    public static void printTicket(Ticket t) {
        if (t != null) {
            System.out.println("Ticket: " + t.licensePlate + " | Level: " + t.parkingSpot.levelNumber + " | SpotID: " + t.parkingSpot.id);
        } else {
            System.out.println("Ticket: Failed to park");
        }
    }

    public static void testConcurrency() throws InterruptedException {
        // Setup: 1 Level with 10 Spots
        List<ParkingSpot> spots = new ArrayList<>();
        for(int i=1; i<=10; i++) {
            spots.add(new ParkingSpot(i, 1, VehicleType.CAR));
        }
        Level level1 = new Level(spots);
        List<Level> levels = List.of(level1);

        ParkingSystem concurrentSystem = new ParkingSystem(
            levels,
            new PriorityQueueParkingStrategy(levels),
            new TimeBasedStrategy()
        );

        // Simulate 20 threads trying to park 20 cars (Only 10 spots available)
        ExecutorService executor = Executors.newFixedThreadPool(20);
        
        for (int i = 0; i < 20; i++) {
            final int id = i;
            executor.submit(() -> {
                Vehicle v = new Vehicle("CAR_THREAD_" + id, "X", VehicleType.CAR);
                Ticket t = concurrentSystem.parkVehicle(v);
                if (t != null) {
                    System.out.println("Thread " + id + ": Parked successfully at Spot " + t.parkingSpot.id);
                } else {
                    System.out.println("Thread " + id + ": FAILED (Lot Full)");
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("Concurrency Test Final Count: " + concurrentSystem.parkedVehicles.size() + "/10 spots occupied.");
    }
}
