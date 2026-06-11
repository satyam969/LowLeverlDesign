package strategy;

import entities.Elevator;
import entities.Floor;
import enums.ElevatorDirection;

import java.util.TreeSet;

/**
 * SCAN Strategy (Elevator Algorithm)
 * The elevator moves all the way to the top/bottom bounds before reversing,
 * serving requests along the way.
 */
public class ScanStrategy implements ElevatorStrategy {

    @Override
    public Floor getNextFloor(Elevator elevator) {
        if (elevator.getPendingRequests().isEmpty()) {
            return null;
        }

        Floor currentFloor = elevator.currentFloor;
        ElevatorDirection direction = elevator.CurrentState == enums.ElevatorState.MOVING_DOWN ? ElevatorDirection.MOVING_DOWN : ElevatorDirection.MOVING_UP;

        TreeSet<Floor> requests = new TreeSet<>((f1, f2) -> Integer.compare(f1.floorNumber, f2.floorNumber));
        requests.addAll(elevator.getPendingRequests());

        if (direction == ElevatorDirection.MOVING_UP) {
            // Continue UP if possible, or just go to the highest request
            Floor next = requests.higher(currentFloor);
             if (next != null) return next;
             // If no more up, reverse to lowest
             return requests.first();
        } else {
             // Continue DOWN if possible
             Floor next = requests.lower(currentFloor);
             if (next != null) return next;
             // If no more down, reverse to highest
             return requests.last();
        }
    }
}
