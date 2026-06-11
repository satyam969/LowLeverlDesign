package strategy;

import entities.Elevator;
import entities.Floor;
import enums.ElevatorDirection;

import java.util.TreeSet;

/**
 * LOOK Strategy (Scanner)
 * The elevator moves in the current direction as long as there are requests in that direction.
 * It only reverses direction when there are no more requests ahead.
 */
public class LookStrategy implements ElevatorStrategy {

    @Override
    public Floor getNextFloor(Elevator elevator) {
        if (elevator.getPendingRequests().isEmpty()) {
            return null;
        }

        Floor currentFloor = elevator.currentFloor;
        ElevatorDirection direction = elevator.CurrentState == enums.ElevatorState.MOVING_UP ? ElevatorDirection.MOVING_UP : ElevatorDirection.MOVING_DOWN;
        if (elevator.CurrentState == enums.ElevatorState.IDLE) {
             // If idle, default to UP unless the request is below
             direction = ElevatorDirection.MOVING_UP;
        }

        TreeSet<Floor> requests = new TreeSet<>((f1, f2) -> Integer.compare(f1.floorNumber, f2.floorNumber));
        requests.addAll(elevator.getPendingRequests());

        if (direction == ElevatorDirection.MOVING_UP) {
            // Find the closest floor ABOVE current floor
            Floor nextFloor = requests.higher(currentFloor);
            if (nextFloor != null) {
                return nextFloor;
            }
            // If none above, change direction to the lowest pending request
            return requests.first();
        } else {
            // Find the closest floor BELOW current floor
            Floor nextFloor = requests.lower(currentFloor);
            if (nextFloor != null) {
                return nextFloor;
            }
            // If none below, change direction to the highest pending request
            return requests.last();
        }
    }
}
