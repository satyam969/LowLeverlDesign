package inventory;

/**
 * Observer interface for inventory level alerts.
 *
 * CHANGED FROM ORIGINAL:
 *  - warn() now accepts context: ingredient name, current quantity, and total capacity.
 *  - Original warn() had no parameters — observers couldn't tell WHICH ingredient was low,
 *    making the alert completely useless ("Low Resource" with no details).
 */
public interface Observer {

    /**
     * Called when an ingredient falls below the low-stock threshold.
     *
     * @param ingredient  the name of the low ingredient
     * @param currentQty  current quantity remaining
     * @param capacity    total capacity of this ingredient slot
     */
    void warn(String ingredient, int currentQty, int capacity);
}
