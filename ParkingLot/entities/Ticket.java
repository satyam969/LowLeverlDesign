package entities;

public class Ticket {
    public String ticketId;
    public String licensePlate;
    public long entryTime;
    public long exitTime;
    public ParkingSpot parkingSpot;
    public Vehicle vehicle;
    public Ticket(String ticketId, String licensePlate, long entryTime, ParkingSpot parkingSpot, Vehicle vehicle) {
        this.ticketId = ticketId;
        this.licensePlate = licensePlate;
        this.entryTime = entryTime;
        this.parkingSpot = parkingSpot;
        this.vehicle = vehicle;
    }
    public void setExitTime(long exitTime) {
        this.exitTime = exitTime;
    }
    public long getParkingDuration() {
        return exitTime - entryTime;
    }
}
