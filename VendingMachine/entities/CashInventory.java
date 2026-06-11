package entities;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks the count of each coin type available for change.
 */
public class CashInventory {
    // enums work as key without defining hashCode() and equals() because they are singleton
    private final Map<Coin, Integer> cashStore = new ConcurrentHashMap<>();

    public CashInventory() {
        // Initialize with 0 for all denominations
        for (Coin coin : Coin.values()) {
            cashStore.put(coin, 0);
        }
    }

    public synchronized void addCoins(List<Coin> coins) {
        for (Coin coin : coins) {
            cashStore.put(coin, cashStore.getOrDefault(coin, 0) + 1);
        }
    }

    public synchronized void addCoin(Coin coin) {
        cashStore.put(coin, cashStore.getOrDefault(coin, 0) + 1);
    }

    public synchronized boolean hasCoin(Coin coin) {
        return cashStore.getOrDefault(coin, 0) > 0;
    }

    public synchronized void deductCoin(Coin coin) {
        int count = cashStore.getOrDefault(coin, 0);
        if (count > 0) {
            cashStore.put(coin, count - 1);
        }
    }

    public int getCount(Coin coin) {
        return cashStore.getOrDefault(coin, 0);
    }

    public int getTotalValue() {
        int total = 0;
        for (Coin coin : Coin.values()) {
            total += coin.getValue() * cashStore.getOrDefault(coin, 0);
        }
        return total;
    }

    // For debugging
    public void displayInventory() {
        System.out.println("Machine Cash Vault (Total Value: " + getTotalValue() + " INR):");
        for (Coin coin : Coin.values()) {
            System.out.println(coin + ": " + cashStore.get(coin));
        }
    }
}
