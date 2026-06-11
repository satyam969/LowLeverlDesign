package states;

import beverage.Beverage;
import context.CofeeMachine;

/**
 * SUPERSEDED — kept only for historical reference.
 *
 * This class has been REPLACED by DispensingState.
 *
 * Original issues:
 *  1. Name "PreparedState" does not appear in the specification.
 *     Requirement says "Dispensing" state.
 *  2. dispatchBeverage() transitioned to IdleState correctly — that part was right.
 *  3. selectBeverage() and prepareBeverage() only printed "Beverage is being prepared"
 *     which was inaccurate for a beverage that was already fully prepared.
 *
 * @deprecated Use {@link DispensingState} instead.
 */
@Deprecated
public class PreparedState implements CofeeMachineState {

    @Override
    public void selectBeverage(CofeeMachine context, Beverage toMake) {
        System.out.println("[PreparedState - DEPRECATED] Use DispensingState instead.");
    }

    @Override
    public void prepareBeverage(CofeeMachine context) {
        System.out.println("[PreparedState - DEPRECATED] Use DispensingState instead.");
    }

    @Override
    public void dispatchBeverage(CofeeMachine context) {
        System.out.println("[PreparedState - DEPRECATED] Use DispensingState.dispatchBeverage() instead.");
        context.setState(new IdleState()); // original transition was correct
    }
}
