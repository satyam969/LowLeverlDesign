package strategy;

import entities.Elevator;
import entities.Floor;

public interface ElevatorStrategy {
    /**
     * Determines the next floor the elevator should visit based on its current state and pending requests.
     * @param elevator The elevator instance to decide for.
     * @return The next Floor to visit, or null if no pending requests.
     */
    Floor getNextFloor(Elevator elevator);
}