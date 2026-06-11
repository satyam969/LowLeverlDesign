package states;

import beverage.Beverage;
import context.CofeeMachine;

/**
 * Preparing state — the machine is actively brewing the beverage.
 *
 * ADDED: New state required by the specification (Idle → Preparing → Dispensing → Idle).
 * Original had SelectedState which didn't match any state in the requirements.
 *
 * This state acts as a guard: if the machine is currently brewing, all other operations
 * are blocked. In a real implementation this would run asynchronously and auto-transition
 * to DispensingState on completion.
 */
public class PreparingState implements CofeeMachineState {

    @Override
    public void selectBeverage(CofeeMachine context, Beverage toMake) {
        // Guard: cannot accept a new order while preparing
        System.out.println("[Preparing] Machine is busy preparing '"
                + context.getCurrentBeverage().getName()
                + "'. Please wait.");
    }

    @Override
    public void prepareBeverage(CofeeMachine context) {
        // Guard: already preparing
        System.out.println("[Preparing] Already preparing. Please wait for the current order to complete.");
    }

    @Override
    public void dispatchBeverage(CofeeMachine context) {
        // Guard: beverage not ready yet
        System.out.println("[Preparing] Beverage is not ready yet. Please wait.");
    }
}
