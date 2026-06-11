package states;

import beverage.Beverage;
import context.CofeeMachine;

/**
 * SUPERSEDED — kept only for historical reference.
 *
 * This class has been REPLACED by PreparingState and DispensingState to match
 * the requirement's state names (Idle → Preparing → Dispensing → Idle).
 *
 * Original issues with this class:
 *  1. prepareBeverage() called beverage.prepare() but NEVER transitioned state
 *     to PreparedState/DispensingState — the machine was permanently stuck here.
 *  2. Name "SelectedState" does not appear in the specification.
 *  3. A comment at the bottom mentioned "maintenance state" but it was never implemented.
 *
 * @deprecated Use {@link PreparingState} and {@link DispensingState} instead.
 */
@Deprecated
public class SelectedState implements CofeeMachineState {

    @Override
    public void selectBeverage(CofeeMachine context, Beverage toMake) {
        // Original comment: "already selected" — no action, no logging
        System.out.println("[SelectedState - DEPRECATED] Use PreparingState / DispensingState.");
    }

    @Override
    public void prepareBeverage(CofeeMachine context) {
        // ORIGINAL BUG: called beverage.prepare() but forgot to setState(new PreparedState())
        // → machine was permanently stuck in SelectedState after preparation
        System.out.println("[SelectedState - DEPRECATED] Use IdleState.prepareBeverage() flow instead.");
    }

    @Override
    public void dispatchBeverage(CofeeMachine context) {
        System.out.println("[SelectedState - DEPRECATED] Use DispensingState.dispatchBeverage() instead.");
    }
}