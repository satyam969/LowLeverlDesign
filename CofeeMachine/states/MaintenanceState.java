package states;

import beverage.Beverage;
import context.CofeeMachine;

/**
 * Maintenance state — all user-facing operations are blocked.
 *
 * ADDED: Required by the specification (states: Idle, Preparing, Dispensing, Maintenance).
 * Was only mentioned in a comment in SelectedState.java but never implemented.
 *
 * The machine can enter Maintenance at any time via CofeeMachine.enterMaintenance()
 * and exit via CofeeMachine.exitMaintenance() (returns to Idle).
 */
public class MaintenanceState implements CofeeMachineState {

    @Override
    public void selectBeverage(CofeeMachine context, Beverage toMake) {
        // ADDED: Block all user operations during maintenance
        System.out.println("[Maintenance] Machine is under maintenance. Cannot accept orders. Please try later.");
    }

    @Override
    public void prepareBeverage(CofeeMachine context) {
        System.out.println("[Maintenance] Machine is under maintenance. Cannot prepare beverages.");
    }

    @Override
    public void dispatchBeverage(CofeeMachine context) {
        System.out.println("[Maintenance] Machine is under maintenance. Cannot dispatch beverages.");
    }
}
