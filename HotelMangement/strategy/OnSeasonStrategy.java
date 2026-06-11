package strategy;

public class OnSeasonStrategy implements PricingStrategy {
    final double MULTIPLY_FACTORY=1.5;
    public double calculatePrice(double actual){
        return MULTIPLY_FACTORY*actual;
    }
}

