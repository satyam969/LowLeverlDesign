package context;

import beverage.Beverage;
import exceptions.InsufficientIngredientException;
import inventory.Inventory;
import states.CofeeMachineState;
import states.IdleState;
import states.MaintenanceState;

/**
 * Context class for the State Pattern — the Coffee Machine itself.
 *
 * CHANGES FROM ORIGINAL:
 *  1. RENAMED: 'beverage' field → 'currentBeverage' for clarity.
 *  2. ADDED: 'inventory' field exposed via getInventory() so states can validate
 *     and consume ingredients directly (was inaccessible before).
 *  3. RENAMED: getSelectedBeverage() → getCurrentBeverage() for clarity.
 *  4. ADDED: setCurrentBeverage() so DispensingState can reset it after dispatch.
 *  5. FIXED: selectBeverage() now assigns currentBeverage FIRST, then delegates
 *     to state. Original assigned and then called state, but state also called
 *     context.selectBeverage() again → infinite recursion.
 *  6. IMPLEMENTED: prepareBeverage() — was just a TODO comment. Now delegates to
 *     the state which handles validation + ingredient consumption + brewing.
 *  7. RENAMED: DispatchBeverage() → dispatchBeverage() (camelCase convention).
 *  8. ADDED: enterMaintenance() and exitMaintenance() for Maintenance state support.
 *  9. ADDED: Startup log message showing initial state.
 */
public class CofeeMachine {

    private final Inventory      inventory;        // CHANGED: final — inventory is set once
    private CofeeMachineState    state;
    private Beverage             currentBeverage;  // RENAMED from 'beverage'

    public CofeeMachine(Inventory inventory) {
        this.inventory = inventory;
        this.state     = new IdleState();
        System.out.println("[System] Coffee Machine initialized. State: Idle.");
    }

    // ── Beverage selection ────────────────────────────────────────────────────

    /**
     * Selects a beverage to prepare.
     *
     * FIXED: currentBeverage is assigned BEFORE delegating to state.
     * Original assigned it AND called state.SelectBeverage which called
     * context.selectBeverage() again → StackOverflowError.
     */
    public void selectBeverage(Beverage toMake) {
        this.currentBeverage = toMake; // assign first — state must NOT call this again
        state.selectBeverage(this, toMake);
    }

    // ── Preparation ───────────────────────────────────────────────────────────

    /**
     * Validates ingredients and prepares the selected beverage.
     *
     * IMPLEMENTED: Was only a TODO comment in the original.
     * All logic delegated to the current state (IdleState handles the full flow).
     * Throws InsufficientIngredientException (unchecked) if any ingredient is short.
     */
    public void prepareBeverage() {
        state.prepareBeverage(this); // may throw InsufficientIngredientException
    }

    // ── Dispatch ──────────────────────────────────────────────────────────────

    /**
     * RENAMED: DispatchBeverage → dispatchBeverage (camelCase).
     */
    public void dispatchBeverage() {
        state.dispatchBeverage(this);
    }

    // ── Maintenance ───────────────────────────────────────────────────────────

    /**
     * ADDED: Transitions machine into Maintenance state, blocking all user operations.
     */
    public void enterMaintenance() {
        this.state = new MaintenanceState();
        System.out.println("[System] Machine entered MAINTENANCE mode.");
    }

    /**
     * ADDED: Exits Maintenance mode and returns to Idle.
     */
    public void exitMaintenance() {
        if (state instanceof MaintenanceState) {
            this.state = new IdleState();
            System.out.println("[System] Maintenance complete. Machine returned to Idle.");
        } else {
            System.out.println("[System] Machine is not in Maintenance mode.");
        }
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    /** RENAMED from getSelectedBeverage() for clarity. */
    public Beverage getCurrentBeverage() {
        return currentBeverage;
    }

    /** ADDED: Reset after dispatch so machine is clean for next order. */
    public void setCurrentBeverage(Beverage beverage) {
        this.currentBeverage = beverage;
    }

    /** ADDED: Exposed so states can validate and consume ingredients. */
    public Inventory getInventory() {
        return inventory;
    }

    /** Package-private setter used by state implementations for transitions. */
    public void setState(CofeeMachineState state) {
        this.state = state;
    }
}
