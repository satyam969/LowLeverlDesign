package states;

import beverage.Beverage;
import context.CofeeMachine;
import exceptions.InsufficientIngredientException;

import java.util.Map;

/**
 * Idle state — the machine is waiting for a beverage selection.
 *
 * CHANGES FROM ORIGINAL:
 *  ★ CRITICAL FIX: Removed infinite recursion.
 *    Original selectBeverage() called context.selectBeverage(toMake) which
 *    called state.selectBeverage() again → infinite recursion → StackOverflowError.
 *    Fix: CofeeMachine.selectBeverage() assigns currentBeverage BEFORE delegating
 *    to state, so the state only needs to handle logging and remain in Idle.
 *
 *  ★ IMPLEMENTED prepareBeverage():
 *    - Full ingredient validation against the beverage's recipe (Requirement #5)
 *    - Throws InsufficientIngredientException with details if any ingredient is short
 *    - Consumes ingredients after successful validation
 *    - Calls beverage.prepare() and transitions to DispensingState
 *    - Original had a TODO comment here — the most critical requirement unimplemented
 *
 *  - ADDED proper state transitions following the required flow:
 *    Idle → (select) → Idle → (prepare) → Preparing → Dispensing
 */
public class IdleState implements CofeeMachineState {

    @Override
    public void selectBeverage(CofeeMachine context, Beverage toMake) {
        // ★ FIXED: Original called context.selectBeverage(toMake) here, which
        //   called state.SelectBeverage(this, toMake) again → infinite recursion.
        //   CofeeMachine.selectBeverage() already sets context.currentBeverage
        //   BEFORE calling this method, so we must NOT call it again from here.
        System.out.println("[Idle] Beverage selected: " + toMake.getName()
                + ". Call prepareBeverage() when ready.");
        // State remains Idle — transition happens when prepareBeverage() is called
    }

    @Override
    public void prepareBeverage(CofeeMachine context) {
        Beverage beverage = context.getCurrentBeverage();

        // Guard: no beverage selected yet
        if (beverage == null) {
            System.out.println("[Idle] Please select a beverage first.");
            return;
        }

        // ★ IMPLEMENTED: Requirement #5 — validate ALL ingredients before consuming any.
        //   Original had only a TODO comment here. The entire validation flow was missing.
        Map<String, Integer> recipe = beverage.getRecipe().getIngredients();

        // Step 1: Validate (fail-fast, no partial consumption)
        String missing = context.getInventory().findMissingIngredient(recipe);
        if (missing != null) {
            int required  = recipe.get(missing);
            int available = context.getInventory().getQuantity(missing);
            // ADDED: Throws exception with full context for Requirement #10
            throw new InsufficientIngredientException(missing, required, available);
        }

        // Step 2: Transition to Preparing (machine is now busy)
        System.out.println("[Idle] Validation passed. Starting preparation of: " + beverage.getName());
        context.setState(new PreparingState());

        // Step 3: Consume ingredients from inventory
        for (Map.Entry<String, Integer> entry : recipe.entrySet()) {
            context.getInventory().reduceQuantity(entry.getKey(), entry.getValue());
        }

        // Step 4: Brew/prepare the beverage
        beverage.prepare();

        // Step 5: Transition to Dispensing — beverage is ready to collect
        context.setState(new DispensingState());
        System.out.println("[System] " + beverage.getName()
                + " is ready! Call dispatchBeverage() to collect.");
    }

    @Override
    public void dispatchBeverage(CofeeMachine context) {
        System.out.println("[Idle] No beverage is ready to dispatch. Please select and prepare first.");
    }
}
