package requests;

import entities.Floor;
import entities.User;
import interfaces.Request;

public class InternalRequest implements Request {

    public Floor destinationFloor;
    public User user;
    public int elevatorId; 

    public InternalRequest(Floor destinationFloor, User user, int elevatorId) {
        this.destinationFloor = destinationFloor;
        this.user = user;
        this.elevatorId = elevatorId;
    }
    @Override
    public Request getRequest() {
        return this;
    }
    
}
