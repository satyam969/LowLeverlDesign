package feealgo;

import entities.Ticket;
import startegies.FeeStrategy;

public class TimeBasedStrategy implements FeeStrategy {
    @Override
    public double calculateFee(Ticket ticket) {
        long duration = ticket.getParkingDuration();
        double amount = duration * 0.01; 
        return amount; 
    }    
}
