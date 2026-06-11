package strategies;

import entities.CashInventory;
import entities.Coin;
import java.util.ArrayList;
import java.util.List;

/**
 * Strategy for calculating change using available physical coins.
 */
public class ChangeStrategy {
    
    /**
     * Calculates change using a greedy approach based on current cash inventory.
     * @param amount The total amount to return.
     * @param inventory The available physical coins.
     * @return List of Coins to return, or null if sufficient change cannot be formed.
     */
    public List<Coin> calculateChange(int amount, CashInventory inventory) {
        List<Coin> result = new ArrayList<>();
        int remaining = amount;

        // Greedy approach: Start with highest denomination
        Coin[] denominations = Coin.values();
        for (int i = denominations.length - 1; i >= 0; i--) {
            Coin coin = denominations[i];
            int val = coin.getValue();

            while (remaining >= val && inventory.getCount(coin) > 0) {
                result.add(coin);
                remaining -= val;
                // Note: We don't deduct from inventory yet, just check availability
                // We'll simulate its depletion in a temp structure or deduct later
                // To keep it simple, we simulate by checking count against what we've 'picked'
                if (countPicked(result, coin) >= inventory.getCount(coin)) {
                    break; 
                }
            }
        }

        if (remaining == 0) {
            return result;
        }
        return null; // Cannot give exact change
    }

    private int countPicked(List<Coin> list, Coin target) {
        int count = 0;
        for (Coin c : list) {
            if (c == target) count++;
        }
        return count;
    }
}
