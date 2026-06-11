package states;

import beverage.Beverage;
import context.CofeeMachine;

/**
 * State interface for the Coffee Machine State Pattern.
 *
 * CHANGES FROM ORIGINAL:
 *  - CHANGED: Method names corrected to camelCase per Java conventions.
 *    Original used PascalCase (SelectBeverage, PrepareBeverage, DispatchBeverage).
 *  - prepareBeverage() may throw InsufficientIngredientException (unchecked),
 *    so no throws declaration is needed, but states may propagate it.
 *
 * Required states per spec: Idle, Preparing, Dispensing, Maintenance.
 */
public interface CofeeMachineState {

    // CHANGED: camelCase (was SelectBeverage)
    void selectBeverage(CofeeMachine context, Beverage toMake);

    // CHANGED: camelCase (was PrepareBeverage)
    void prepareBeverage(CofeeMachine context);

    // CHANGED: camelCase (was DispatchBeverage)
    void dispatchBeverage(CofeeMachine context);
}
