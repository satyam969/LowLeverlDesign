package entities;

/**
 * Represents a coin/currencydenomination.
 */
public enum Coin {
    INR_10(10),
    INR_20(20),
    INR_50(50),
    INR_100(100),
    INR_500(500);

    private final int value;
    Coin(int value) { this.value = value; }
    public int getValue() { return value; }
}
