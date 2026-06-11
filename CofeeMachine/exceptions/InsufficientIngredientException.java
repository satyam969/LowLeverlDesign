package exceptions;

/**
 * ADDED: Custom exception thrown when ingredient validation fails before beverage preparation.
 * Satisfies Requirement #10 — "If ingredient validation fails, the system should reject
 * the preparation request and provide appropriate feedback."
 *
 * Extends RuntimeException so callers are not forced to declare checked throws,
 * but the exception is still caught at the system boundary (CofeeMachineSystem).
 */
public class InsufficientIngredientException extends RuntimeException {

    private final String ingredient;
    private final int required;
    private final int available;

    public InsufficientIngredientException(String ingredient, int required, int available) {
        super(String.format(
            "[ERROR] Insufficient '%s': required %d units but only %d available. Please refill.",
            ingredient, required, available
        ));
        this.ingredient = ingredient;
        this.required   = required;
        this.available  = available;
    }

    public String getIngredient() { return ingredient; }
    public int    getRequired()   { return required;   }
    public int    getAvailable()  { return available;  }
}
