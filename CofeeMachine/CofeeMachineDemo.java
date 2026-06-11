import beverage.Beverage;
import factory.*;
import inventory.Inventory;
import inventory.InventoryObserver;
import inventory.Observer;
import strategy.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * End-to-end demonstration of the Coffee Machine LLD.
 *
 * ADDED: Was completely empty in the original (just an empty class body).
 * This demo exercises all requirements:
 *   #1  — Multiple beverage types via Factory Pattern
 *   #2  — Ingredient inventory with real-time tracking
 *   #3  — Predefined recipes per beverage
 *   #4  — Ingredient validation before preparation
 *   #5  — Observer alerts at 20% threshold
 *   #6  — Customisation via Strategy Pattern (sugar + milk)
 *   #7  — State machine (Idle → Preparing → Dispensing → Idle)
 *   #8  — Full beverage preparation flow
 *   #9  — Rejection with feedback when ingredients are insufficient
 *   #10 — Refill support
 *   #11 — Maintenance state
 */
public class CofeeMachineDemo {

    public static void main(String[] args) {
        printBanner("COFFEE MACHINE LLD DEMO");

        // ── 1. Set up Inventory ───────────────────────────────────────────────
        Map<String, Integer> ingredients = new HashMap<>();
        ingredients.put("coffeeBeans", 100);  // grams
        ingredients.put("water",       500);  // ml
        ingredients.put("milk",        200);  // ml
        ingredients.put("teaLeaves",   50);   // grams

        Map<String, Integer> capacity = new HashMap<>();
        capacity.put("coffeeBeans", 200);
        capacity.put("water",       1000);
        capacity.put("milk",        500);
        capacity.put("teaLeaves",   100);

        List<Observer> observers = new ArrayList<>();
        observers.add(new InventoryObserver()); // Observer pattern — alerts on low stock

        Inventory inventory = new Inventory(ingredients, capacity, observers);

        // ── 2. Boot the system (Singleton + volatile DCL) ─────────────────────
        CofeeMachineSystem system = CofeeMachineSystem.initialize(inventory);

        // ──────────────────────────────────────────────────────────────────────
        // Scenario 1: Coffee — Low Sugar, Regular Milk (happy path)
        // ──────────────────────────────────────────────────────────────────────
        printScenario(1, "Coffee — Low Sugar, Regular Milk (happy path)");

        Beverage coffee = new CofeeFactory()
                .createBeverage(new LowSugarStrategy(), new RegularMilkStrategy());

        system.selectBeverage(coffee);   // Idle: log + stay Idle
        system.prepareBeverage();        // Idle → Preparing → Dispensing
        system.dispatchBeverage();       // Dispensing → Idle

        // ──────────────────────────────────────────────────────────────────────
        // Scenario 2: Tea — No Sugar, Skim Milk
        // ──────────────────────────────────────────────────────────────────────
        printScenario(2, "Tea — No Sugar, Skim Milk");

        Beverage tea = new TeaFactory()
                .createBeverage(new NoSugarStrategy(), new SkimMilkStrategy());

        system.selectBeverage(tea);
        system.prepareBeverage();
        system.dispatchBeverage();

        // ──────────────────────────────────────────────────────────────────────
        // Scenario 3: Latte — Medium Sugar, Almond Milk
        // ──────────────────────────────────────────────────────────────────────
        printScenario(3, "Latte — Medium Sugar, Almond Milk");

        Beverage latte = new LatteFactory()
                .createBeverage(new MediumSugarStrategy(), new AlmondMilkStrategy());

        system.selectBeverage(latte);
        system.prepareBeverage();
        system.dispatchBeverage();

        // ──────────────────────────────────────────────────────────────────────
        // Scenario 4: Cappuccino — High Sugar, Regular Milk
        // ──────────────────────────────────────────────────────────────────────
        printScenario(4, "Cappuccino — High Sugar, Regular Milk");

        Beverage cappuccino = new CappuccinoFactory()
                .createBeverage(new HighSugarStrategy(), new RegularMilkStrategy());

        system.selectBeverage(cappuccino);
        system.prepareBeverage();
        system.dispatchBeverage();

        // ──────────────────────────────────────────────────────────────────────
        // Scenario 5: Insufficient ingredient — should be rejected with feedback
        // (Requirement #9 and #10)
        // ──────────────────────────────────────────────────────────────────────
        printScenario(5, "Insufficient Ingredients → order rejected (Req #9, #10)");

        // coffeeBeans started at 100g, each coffee uses 20g.
        // We've already made one coffee (20g) + one latte (15g) + one cappuccino (18g) = 53g used.
        // Remaining ~47g — let's drain it by ordering many coffees.
        // For simplicity, directly reduce coffee beans to just 5g (below 20g needed).
        inventory.addQuantity("coffeeBeans", 0); // triggers no alert, just confirm state
        // Manually deplete by reducing 42 more grams
        inventory.reduceQuantity("coffeeBeans", 42); // now coffeeBeans ≈ 5g left

        Beverage anotherCoffee = new CofeeFactory()
                .createBeverage(new LowSugarStrategy(), new RegularMilkStrategy());
        system.selectBeverage(anotherCoffee);
        system.prepareBeverage(); // ← should fail and print InsufficientIngredientException message

        // ──────────────────────────────────────────────────────────────────────
        // Scenario 6: Refill ingredient and retry (Requirement #11)
        // ──────────────────────────────────────────────────────────────────────
        printScenario(6, "Refill coffeeBeans + water and retry (Req #11)");

        // FIXED demo: water also ran out during previous scenarios, so refill it too
        system.refillIngredient("coffeeBeans", 150); // refill beans to near capacity
        system.refillIngredient("water", 800);        // refill water to near capacity
        system.prepareBeverage();                     // now succeeds — same Coffee still selected
        system.dispatchBeverage();

        // ──────────────────────────────────────────────────────────────────────
        // Scenario 7: Maintenance mode (Requirement #8 — state: Maintenance)
        // ──────────────────────────────────────────────────────────────────────
        printScenario(7, "Maintenance Mode (Req #8)");

        system.enterMaintenance();

        // All operations should be blocked
        Beverage blockedOrder = new CofeeFactory()
                .createBeverage(new LowSugarStrategy(), new RegularMilkStrategy());
        system.selectBeverage(blockedOrder);  // blocked
        system.prepareBeverage();             // blocked
        system.dispatchBeverage();            // blocked

        system.exitMaintenance();             // back to Idle

        // Machine is operational again
        printScenario(8, "Back from Maintenance — normal order");
        Beverage finalCoffee = new CofeeFactory()
                .createBeverage(new MediumSugarStrategy(), new SkimMilkStrategy());
        system.selectBeverage(finalCoffee);
        system.prepareBeverage();
        system.dispatchBeverage();

        printBanner("DEMO COMPLETE");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static void printBanner(String title) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.printf( "║  %-40s║%n", title);
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println();
    }

    private static void printScenario(int num, String description) {
        System.out.println();
        System.out.println("── Scenario " + num + ": " + description + " ──");
    }
}
