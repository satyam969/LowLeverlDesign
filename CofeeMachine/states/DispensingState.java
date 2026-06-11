package states;

import beverage.Beverage;
import context.CofeeMachine;

/**
 * Dispensing state — the beverage is brewed and waiting to be collected.
 *
 * ADDED: Properly named state matching the requirement (Dispensing, not PreparedState).
 * Original PreparedState had: selectBeverage → no-op, prepareBeverage → no-op,
 * dispatchBeverage → print + go to Idle. The state naming and transitions are now correct.
 *
 * Transition: DispensingState → dispatchBeverage() → IdleState
 */
public class DispensingState implements CofeeMachineState {

    @Override
    public void selectBeverage(CofeeMachine context, Beverage toMake) {
        // Guard: must collect current beverage before ordering another
        System.out.println("[Dispensing] Please collect your '"
                + context.getCurrentBeverage().getName()
                + "' before selecting a new beverage.");
    }

    @Override
    public void prepareBeverage(CofeeMachine context) {
        // Guard: beverage already prepared
        System.out.println("[Dispensing] Beverage already prepared. Please collect it first.");
    }

    @Override
    public void dispatchBeverage(CofeeMachine context) {
        // ★ Core action: dispense the beverage and return to Idle
        Beverage beverage = context.getCurrentBeverage();
        beverage.dispense(); // calls the beverage's dispense() method (added to interface)

        // Clear the current beverage and return to Idle
        context.setCurrentBeverage(null); // ADDED: reset after dispatch
        context.setState(new IdleState());
        System.out.println("[System] Machine returned to Idle. Ready for next order.\n");
    }
}
