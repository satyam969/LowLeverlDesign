package startegies;

import entities.Ticket;

public interface FeeStrategy {
    double calculateFee(Ticket ticket);
}