package strategy;

import entities.Elevator;
import entities.Floor;

/**
 * Nearest First Strategy (Shortest Seek Time First)
 * Selects the request closest to the current floor, regardless of direction.
 * WARNING: Can cause starvation.
 */
public class NearestFirstStrategy implements ElevatorStrategy {

    @Override
    public Floor getNextFloor(Elevator elevator) {
        if (elevator.getPendingRequests().isEmpty()) {
            return null;
        }

        Floor current = elevator.currentFloor;
        Floor nearest = null;
        int minDistance = Integer.MAX_VALUE;

        for (Floor req : elevator.getPendingRequests()) {
            int distance = Math.abs(req.floorNumber - current.floorNumber);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = req;
            }
        }
        return nearest;
    }
}
