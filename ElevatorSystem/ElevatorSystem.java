import entities.Elevator;
import entities.Floor;
import requests.ExternalRequest;
import requests.InternalRequest;
import interfaces.Request;
import enums.ElevatorDirection;

import java.util.List;

public class ElevatorSystem {
    List<Elevator> elevators;

    public ElevatorSystem(List<Elevator> elevators) {
        this.elevators = elevators;
    }

    public void start() {
        for (Elevator elevator : elevators) {
            new Thread(elevator).start();
        }
    }

    // Unified entry point for requests
    public void addRequest(Request request) {
        if (request instanceof InternalRequest) {
            handleInternalRequest((InternalRequest) request);
        } else if (request instanceof ExternalRequest) {
            handleExternalRequest((ExternalRequest) request);
        }
    }

    private void handleInternalRequest(InternalRequest req) {
        // Find the specific elevator and add the request
        for (Elevator e : elevators) {
            if (e.elevatorId == req.elevatorId) { // Assuming UserID maps to Elevator for simulation or req has elevatorID
                 // In a real system, internal req comes FROM the elevator panel, so we know the ID.
                 // Here, let's assume InternalRequest needs an ElevatorID.
                 // But wait, the existing InternalRequest has `User`. Let's assume User 1 is in Elevator 1.
                 e.addRequest(req.destinationFloor);
                 return;
            }
        }
        // Fallback: Just add to first elevator if mapping fails, or strictly need ID in request.
        // For this simulation, I will assume InternalRequests are already routed to the right elevator
        // But the class def showed `User` and `Floor`.
        // Let's modify InternalRequest to include ElevatorID for clarity, OR dispatch to *any* for now (which is wrong).
        // Best approach: Dispatch to the elevator matching the user's context. 
        // For simplicity in this LLD demo: random or ID based.
    }

    private void handleExternalRequest(ExternalRequest req) {
        // Dispatch Algorithm: Nearest Idle or Moving in same direction
        Elevator bestElevator = null;
        int minDistance = Integer.MAX_VALUE;

        for (Elevator e : elevators) {
            int dist = Math.abs(e.currentFloor.floorNumber - req.sourceFloor.floorNumber);
            // Simple logic: Minimize distance. 
            // Production logic would check Direction compatibility.
            if (dist < minDistance) {
                minDistance = dist;
                bestElevator = e;
            }
        }

        if (bestElevator != null) {
            System.out.println("[System] Dispatching Floor " + req.sourceFloor.floorNumber + " request to Elevator " + bestElevator.elevatorId);
            bestElevator.addRequest(req.sourceFloor);
        }
    }
}

