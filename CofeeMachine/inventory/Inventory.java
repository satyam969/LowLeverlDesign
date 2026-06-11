package inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages ingredient stock levels and notifies observers when stock is low.
 *
 * CHANGES FROM ORIGINAL:
 *  1. Field names corrected to camelCase (was Ingredients, Observers, Capacity).
 *  2. Fields made final for immutability of references.
 *  3. FIXED: 'capacity' map was declared but NEVER initialized in the constructor —
 *     any call to reduceQuantity() or addQuantity() would throw NullPointerException.
 *     Capacity is now a required constructor parameter.
 *  4. ADDED: hasEnough() — single-item availability check.
 *  5. ADDED: validateRecipe() — validates all recipe ingredients atomically before
 *     any are consumed (prevents partial consumption on failure).
 *  6. FIXED: reduceQuantity() now guards against going below zero.
 *  7. CHANGED: observer.warn() now passes ingredient context (name, qty, capacity).
 *  8. CHANGED: LOW_THRESHOLD extracted to a named constant (was inline 0.2).
 */
public class Inventory {

    // CHANGED: camelCase field names, all final
    private final Map<String, Integer> ingredients;
    private final List<Observer>       observers;
    private final Map<String, Integer> capacity; // FIXED: was never initialized

    /** Inventory falls below this fraction of capacity → alert observers */
    private static final double LOW_THRESHOLD = 0.20;

    /**
     * FIXED: Constructor now requires the capacity map.
     * Original constructor only took ingredients + observers; capacity was always null.
     *
     * @param ingredients initial stock levels  (ingredient → quantity)
     * @param capacity    max capacity per slot  (ingredient → max quantity)
     * @param observers   list of low-stock observers
     */
    public Inventory(Map<String, Integer> ingredients,
                     Map<String, Integer> capacity,
                     List<Observer>       observers) {
        this.ingredients = new HashMap<>(ingredients); // defensive copy
        this.capacity    = new HashMap<>(capacity);
        this.observers   = new ArrayList<>(observers);
    }

    // ── Query ─────────────────────────────────────────────────────────────────

    public int getQuantity(String item) {
        return ingredients.getOrDefault(item, 0);
    }

    /**
     * ADDED: Check whether a single ingredient has at least 'required' units.
     * Used during per-ingredient validation in the state machine.
     */
    public boolean hasEnough(String item, int required) {
        return ingredients.getOrDefault(item, 0) >= required;
    }

    /**
     * ADDED: Validates ALL ingredients in a recipe before consuming any of them.
     * This satisfies Requirement #5 — validate availability BEFORE preparation begins.
     * Returns the name of the first failing ingredient, or null if all pass.
     */
    public String findMissingIngredient(Map<String, Integer> recipe) {
        for (Map.Entry<String, Integer> entry : recipe.entrySet()) {
            if (!hasEnough(entry.getKey(), entry.getValue())) {
                return entry.getKey();
            }
        }
        return null; // all ingredients available
    }

    // ── Mutation ──────────────────────────────────────────────────────────────

    /**
     * Reduces stock for one ingredient and notifies observers if it crosses the
     * low-threshold.
     *
     * FIXED: Added Math.max(0, ...) guard — original could silently go negative.
     */
    public void reduceQuantity(String item, int byQty) {
        int curr   = ingredients.getOrDefault(item, 0);
        int newQty = Math.max(0, curr - byQty); // FIXED: never go below zero
        ingredients.put(item, newQty);

        // Notify if stock dropped below threshold
        if (capacity.containsKey(item)) {
            int cap = capacity.get(item);
            if (newQty < LOW_THRESHOLD * cap) {
                // CHANGED: pass ingredient context so observers know WHAT is low
                notifyObservers(item, newQty, cap);
            }
        }
    }

    /**
     * Refills an ingredient, capped at its maximum capacity.
     * Satisfies Requirement #11.
     */
    public void addQuantity(String item, int toAdd) {
        int cap    = capacity.getOrDefault(item, Integer.MAX_VALUE);
        int curr   = ingredients.getOrDefault(item, 0);
        int newQty = Math.min(cap, curr + toAdd);
        ingredients.put(item, newQty);
        System.out.printf("[Inventory] Refilled '%s': %d → %d (cap: %d)%n",
                item, curr, newQty, cap);
    }

    // ── Observer notification ─────────────────────────────────────────────────

    private void notifyObservers(String ingredient, int currentQty, int cap) {
        for (Observer observer : observers) {
            // CHANGED: now passes meaningful context, not just a no-arg warn()
            observer.warn(ingredient, currentQty, cap);
        }
    }
}
