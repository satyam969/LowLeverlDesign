package recipe;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Holds the ingredient requirements for a beverage.
 *
 * CHANGES FROM ORIGINAL:
 *  - Made 'ingredients' field final for immutability (recipe shouldn't change after creation)
 *  - ADDED getIngredients() accessor — was missing, making validation impossible
 *  - ADDED static factory methods for each supported beverage, so recipes are
 *    centrally defined and not scattered across factories
 */
public class Recipe {

    // CHANGED: final — a recipe's composition is fixed after construction
    private final Map<String, Integer> ingredients;

    public Recipe(Map<String, Integer> ingredients) {
        // Defensive copy + unmodifiable wrapper to prevent external mutation
        this.ingredients = Collections.unmodifiableMap(new HashMap<>(ingredients));
    }

    /**
     * ADDED: Getter was completely missing in the original.
     * Without this, ingredient validation was impossible (the field was private with no accessor).
     */
    public Map<String, Integer> getIngredients() {
        return ingredients; // already unmodifiable
    }

    // ── Static factory methods for predefined beverage recipes ──────────────

    /** Coffee: 20g beans + 150ml water */
    public static Recipe forCoffee() {
        Map<String, Integer> ing = new HashMap<>();
        ing.put("coffeeBeans", 20);
        ing.put("water", 150);
        return new Recipe(ing);
    }

    /** Tea: 10g leaves + 200ml water + 50ml milk */
    public static Recipe forTea() {
        Map<String, Integer> ing = new HashMap<>();
        ing.put("teaLeaves", 10);
        ing.put("water", 200);
        ing.put("milk", 50);
        return new Recipe(ing);
    }

    /** Latte: 15g beans + 100ml water + 200ml milk */
    public static Recipe forLatte() {
        Map<String, Integer> ing = new HashMap<>();
        ing.put("coffeeBeans", 15);
        ing.put("water", 100);
        ing.put("milk", 200);
        return new Recipe(ing);
    }

    /** Cappuccino: 18g beans + 120ml water + 100ml frothed milk */
    public static Recipe forCappuccino() {
        Map<String, Integer> ing = new HashMap<>();
        ing.put("coffeeBeans", 18);
        ing.put("water", 120);
        ing.put("milk", 100);
        return new Recipe(ing);
    }
}
