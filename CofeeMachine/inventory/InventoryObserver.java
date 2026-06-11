package inventory;

/**
 * Concrete observer that prints a low-stock alert to the console.
 *
 * CHANGED FROM ORIGINAL:
 *  - Implements the updated Observer interface with ingredient context parameters.
 *  - Original only printed "Low Resource" — now prints exactly which ingredient is low,
 *    how much is remaining, and the percentage vs capacity.
 */
public class InventoryObserver implements Observer {

    @Override
    public void warn(String ingredient, int currentQty, int capacity) {
        // CHANGED: Meaningful alert with ingredient name, quantity, and percentage
        double pct = capacity > 0 ? (currentQty * 100.0 / capacity) : 0;
        System.out.printf("[ALERT] Low stock for '%s': %d units remaining (%.1f%% of %d capacity). Please refill soon.%n",
                ingredient, currentQty, pct, capacity);
    }
}
