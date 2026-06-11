import entities.Elevator;
import entities.Floor;
import entities.User;
import enums.ElevatorDirection;
import requests.ExternalRequest;
import requests.InternalRequest;
import strategy.LookStrategy;
import strategy.NearestFirstStrategy;
import strategy.ScanStrategy;

import java.util.ArrayList;
import java.util.List;

public class ElevatorSimulation {
    public static void main(String[] args) {
        System.out.println("Starting Elevator System Simulation...");

        // 1. Create Floors
        Floor floor0 = new Floor(0);
        Floor floor1 = new Floor(1);
        Floor floor5 = new Floor(5);
        Floor floor10 = new Floor(10);

        // 2. Create Elevators with different strategies
        // Elevator 0: LOOK Strategy (Efficient)
        Elevator e0 = new Elevator(0, floor0, new LookStrategy());
        
        // Elevator 1: SCAN Strategy (Systematic)
        Elevator e1 = new Elevator(1, floor10, new ScanStrategy());

        // Elevator 2: Nearest First (Greedy)
        Elevator e2 = new Elevator(2, floor5, new NearestFirstStrategy());

        List<Elevator> elevators = new ArrayList<>();
        elevators.add(e0);
        elevators.add(e1);
        elevators.add(e2);

        // 3. Initialize Controller
        ElevatorSystem system = new ElevatorSystem(elevators);
        
        // 4. Start Threads
        system.start();

        // 5. Simulate Requests
        try {
            // Scenario 1: External Request at Floor 5 (UP)
            // Expectation: Nearest elevator should pick it up. E2 is at 5, so E2 should take it instantly.
            System.out.println("\n--- sending request: Floor 5 UP ---");
            system.addRequest(new ExternalRequest(floor5, ElevatorDirection.MOVING_UP));
            Thread.sleep(1000);

            // Scenario 2: Internal Request for Elevator 0 (User wants to go to Floor 10)
            System.out.println("\n--- sending request: User in E0 wants Floor 10 ---");
            User u1 = new User(0); // User mapping to E0
            system.addRequest(new InternalRequest(floor10, u1, 0));
            Thread.sleep(1000);

            // Scenario 3: External Request at Floor 1 (DOWN)
            // E0 is moving Up to 10. E1 is at 10. E2 is at 5.
            // Dispatcher should likely pick E0 (dist 1) or E2 (dist 4) or E1 (dist 9).
            // E0 is at 0. Dist to 1 is 1.
            System.out.println("\n--- sending request: Floor 1 DOWN ---");
            system.addRequest(new ExternalRequest(floor1, ElevatorDirection.MOVING_DOWN));

            // Allow simulation to run
            Thread.sleep(10000);
            System.out.println("\n--- Simulation Ending ---");
            System.exit(0);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
