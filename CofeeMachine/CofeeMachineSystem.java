import beverage.Beverage;
import context.CofeeMachine;
import exceptions.InsufficientIngredientException;
import inventory.Inventory;
import inventory.InventoryObserver;
import inventory.Observer;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Singleton facade over CofeeMachine — the public entry point for clients.
 *
 * CHANGES FROM ORIGINAL:
 *  1. FIXED: Added 'volatile' to the instance field.
 *     Without volatile, the JVM may reorder instructions inside the DCL block,
 *     causing another thread to observe a partially constructed object.
 *  2. RENAMED: 'lck' → 'lock' (camelCase convention).
 *  3. CHANGED: Constructor now requires an Inventory argument to properly
 *     initialise the CofeeMachine. Original constructor ignored the machine field.
 *  4. CHANGED: Two-phase initialisation:
 *       - initialize(inventory) — called once at startup
 *       - getInstance()         — called thereafter
 *  5. IMPLEMENTED: All public facade methods (was just a TODO comment originally).
 *  6. ADDED: Exception handling for InsufficientIngredientException at the
 *     system boundary, satisfying Requirement #10 (provide appropriate feedback).
 *  7. ADDED: refillIngredient() satisfying Requirement #11.
 *  8. ADDED: enterMaintenance() / exitMaintenance() for Maintenance state.
 */
public class CofeeMachineSystem {

    // ★ FIXED: Must be 'volatile' for safe DCL in Java.
    // Without it, another thread may read a partially-initialised instance.
    private static volatile CofeeMachineSystem instance = null;

    private final CofeeMachine machine;

    // RENAMED: lck → lock (camelCase)
    private static final Lock lock = new ReentrantLock();

    /**
     * CHANGED: Private constructor now accepts Inventory so the machine is
     * properly initialised. Original constructor ignored the 'machine' field.
     */
    private CofeeMachineSystem(Inventory inventory) {
        this.machine = new CofeeMachine(inventory);
        System.out.println("[System] CofeeMachineSystem singleton created.");
    }

    // ── Singleton lifecycle ───────────────────────────────────────────────────

    /**
     * ADDED: One-time initialisation — call this at application startup.
     * Subsequent calls are no-ops (instance already created).
     *
     * @param inventory the pre-configured inventory to use
     * @return the singleton instance
     */
    public static CofeeMachineSystem initialize(Inventory inventory) {
        if (instance == null) {
            lock.lock();
            try {
                // Double-checked locking — second null check inside the lock
                if (instance == null) {
                    instance = new CofeeMachineSystem(inventory);
                }
            } finally {
                lock.unlock(); // always release, even if constructor throws
            }
        }
        return instance;
    }

    /**
     * Returns the singleton after it has been initialised.
     *
     * @throws IllegalStateException if initialize() was not called first
     */
    public static CofeeMachineSystem getInstance() {
        if (instance == null) {
            throw new IllegalStateException(
                "CofeeMachineSystem not initialised. Call initialize(inventory) first.");
        }
        return instance;
    }

    // ── Public facade methods ─────────────────────────────────────────────────
    // IMPLEMENTED: All methods were TODO comments in the original.

    /** Selects a beverage for preparation. */
    public void selectBeverage(Beverage beverage) {
        machine.selectBeverage(beverage);
    }

    /**
     * Validates ingredients and prepares the selected beverage.
     * Catches InsufficientIngredientException and prints user-friendly feedback.
     * Satisfies Requirement #10 — "provide appropriate feedback" on failure.
     */
    public void prepareBeverage() {
        try {
            machine.prepareBeverage();
        } catch (InsufficientIngredientException e) {
            // ADDED: Requirement #10 — catch at system boundary and show feedback
            System.out.println(e.getMessage());
            System.out.println("[System] Order cancelled. Please refill ingredients and try again.");
        }
    }

    /** Dispenses the prepared beverage to the user. */
    public void dispatchBeverage() {
        machine.dispatchBeverage();
    }

    /**
     * Refills an ingredient up to its maximum capacity.
     * Satisfies Requirement #11 — "support refilling ingredients".
     *
     * @param ingredient the ingredient name (e.g. "coffeeBeans", "water")
     * @param amount     units to add
     */
    public void refillIngredient(String ingredient, int amount) {
        machine.getInventory().addQuantity(ingredient, amount);
    }

    /** Puts the machine into Maintenance mode (blocks all user operations). */
    public void enterMaintenance() {
        machine.enterMaintenance();
    }

    /** Returns the machine to Idle from Maintenance mode. */
    public void exitMaintenance() {
        machine.exitMaintenance();
    }
}
