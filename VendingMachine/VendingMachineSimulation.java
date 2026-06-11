import entities.Coin;
import entities.Product;
import entities.VendingMachine;

/**
 * Simulation entry point for the Vending Machine with Rupee Denominations.
 */
public class VendingMachineSimulation {
    public static void main(String[] args) {
        System.out.println("=== Starting Rupee Vending Machine Simulation ===");

        // 1. Initialize Machine
        VendingMachine vm = new VendingMachine();

        // 2. Setup Cash Inventory (Machine needs some coins for change)
        vm.getCashInventory().addCoin(Coin.INR_10);
        vm.getCashInventory().addCoin(Coin.INR_10);
        vm.getCashInventory().addCoin(Coin.INR_20);
        vm.getCashInventory().addCoin(Coin.INR_50);
        
        vm.getCashInventory().displayInventory();

        // 3. Setup Product Inventory
        vm.getInventory().addProduct(new Product(1, "Samosa", 20), 5); // 20 Rupees
        vm.getInventory().addProduct(new Product(2, "Coffee", 30), 10); // 30 Rupees
        vm.getInventory().addProduct(new Product(3, "Chips", 15), 2); // 15 Rupees

        // --- Scenario 1: Exact Change ---
        System.out.println("\nScenario 1: Buying Samosa (20) with 2 10s...");
        vm.insertCoin(Coin.INR_10);
        vm.insertCoin(Coin.INR_10);
        vm.selectProduct(1, 1);
        vm.dispenseProduct();

        // --- Scenario 2: Getting Change ---
        System.out.println("\nScenario 2: Buying Coffee (30) with 50 coin...");
        System.out.println("Total Cash in Machine BEFORE: " + vm.getCashInventory().getTotalValue());
        vm.insertCoin(Coin.INR_50);
        vm.selectProduct(2, 1);
        vm.dispenseProduct(); // Should return 20 (either 1x20 or 2x10)
        System.out.println("Total Cash in Machine AFTER: " + vm.getCashInventory().getTotalValue());

        // --- Scenario 3: Insufficient Machine Change ---
        System.out.println("\nScenario 3: Attempting to buy Chips (15) with 50 but no 10s/5s...");
        vm.insertCoin(Coin.INR_50);
        vm.selectProduct(3, 1); 
        vm.dispenseProduct();

        // --- Scenario 4: Bulk Purchase ---
        System.out.println("\nScenario 4: Bulk buying 3 Samosas (20 each = 60 total)...");
        vm.insertCoin(Coin.INR_100); // Insert 100
        vm.selectProduct(1, 3); // Buy 3
        vm.dispenseProduct(); // Should return 40 change (100 - 60)

        vm.getCashInventory().displayInventory();
        System.out.println("\n=== Simulation Complete ===");
    }
}
