package entities;

import enums.ElevatorDirection;
import enums.ElevatorState;
import strategy.ElevatorStrategy;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Elevator implements Runnable {
    public int elevatorId;
    public Floor currentFloor;
    public ElevatorState CurrentState;
    public ElevatorDirection currentDirection;
    private final Set<Floor> pendingRequests; // Thread-safe set of requests
    public ElevatorStrategy strategy;

    public Elevator(int elevatorId, Floor currentFloor, ElevatorStrategy strategy) {
        this.elevatorId = elevatorId;
        this.currentFloor = currentFloor;
        this.strategy = strategy;
        this.CurrentState = ElevatorState.IDLE;
        this.currentDirection = ElevatorDirection.MOVING_UP;
        this.pendingRequests = Collections.synchronizedSet(new HashSet<>());
    }

    // Thread-safe method to add a request
    public synchronized void addRequest(Floor floor) {
        System.out.println("[Elevator " + elevatorId + "] Request added for Floor " + floor.floorNumber);
        pendingRequests.add(floor);
        notify(); // Wake up the elevator thread if it's waiting
    }

    public Set<Floor> getPendingRequests() {
        return pendingRequests;
    }

    @Override
    public void run() {
        while (true) {
            Floor nextFloor = null;
            synchronized (this) {
                while (pendingRequests.isEmpty()) {
                    try {
                        CurrentState = ElevatorState.IDLE;
                        System.out.println("[Elevator " + elevatorId + "] IDLE at Floor " + currentFloor.floorNumber);
                        wait(); // Wait for a new request
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            
            // Determine next immediate destination
            // We call strategy inside the loop effectively re-evaluating decisions at every floor
            nextFloor = strategy.getNextFloor(this);

            if (nextFloor != null) {
                if (nextFloor.floorNumber != currentFloor.floorNumber) {
                    moveOneFloorTowards(nextFloor);
                } else {
                    // We are at the target floor
                    openDoors();
                }
            }
        }
    }

    private void moveOneFloorTowards(Floor targetFloor) {
        // Determine direction
        if (targetFloor.floorNumber > currentFloor.floorNumber) {
            CurrentState = ElevatorState.MOVING_UP;
            currentDirection = ElevatorDirection.MOVING_UP;
            // Simulate move
            try { Thread.sleep(500); } catch (InterruptedException e) {}
            // Update floor
            currentFloor = new Floor(currentFloor.floorNumber + 1);
        } else {
            CurrentState = ElevatorState.MOVING_DOWN;
            currentDirection = ElevatorDirection.MOVING_DOWN;
            // Simulate move
            try { Thread.sleep(500); } catch (InterruptedException e) {}
            // Update floor
            currentFloor = new Floor(currentFloor.floorNumber - 1);
        }
        System.out.println("[Elevator " + elevatorId + "] Reached Floor " + currentFloor.floorNumber);
    }

    private void openDoors() {
        System.out.println("[Elevator " + elevatorId + "] Doors Opening at Floor " + currentFloor.floorNumber);
        try { Thread.sleep(200); } catch (InterruptedException e) {} // Door logic delay
        synchronized (this) {
            pendingRequests.remove(currentFloor);
        }
    }
}
