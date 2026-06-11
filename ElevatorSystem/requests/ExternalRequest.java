package requests;


import entities.Floor;
import enums.ElevatorDirection;
import interfaces.Request;

public class ExternalRequest implements Request {

    public Floor sourceFloor;
    public ElevatorDirection direction;

    public ExternalRequest(Floor sourceFloor, ElevatorDirection direction) {
        this.sourceFloor = sourceFloor;
        this.direction = direction;
    }

    @Override
    public Request getRequest() {
        return this;
    }
    
}
