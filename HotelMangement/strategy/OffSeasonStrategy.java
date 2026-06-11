package strategy;

public class OffSeasonStrategy implements PricingStrategy {
    final double MULTIPLY_FACTORY=0.8;
    public double calculatePrice(double actual){
        return MULTIPLY_FACTORY*actual;
    }
}
