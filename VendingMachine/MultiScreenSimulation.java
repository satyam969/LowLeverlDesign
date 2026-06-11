import entities.Coin;
import entities.Product;
import entities.VendingMachine;
import entities.Inventory;

public class MultiScreenSimulation {
    public static void main(String[] args) {
        System.out.println("=== Starting Multi-Screen Stock Reservation Simulation ===");

        // 1. Shared Inventory with 2 Samosas
        Inventory sharedInventory = new Inventory();
        sharedInventory.addProduct(new Product(1, "Samosa", 20), 2);

        // 2. Two separate machine sessions sharing inventory
        VendingMachine screen1 = new VendingMachine(sharedInventory);
        VendingMachine screen2 = new VendingMachine(sharedInventory);
        
        System.out.println("\n--- Step 1: Screen 1 selects ALL 2 Samosas ---");
        screen1.insertCoin(Coin.INR_50);
        screen1.selectProduct(1, 2); // Reserved 2. Inventory stock=2, reserved=2.
        
        System.out.println("\n--- Step 2: Screen 2 tries to select 1 Samosa (Should Fail) ---");
        screen2.insertCoin(Coin.INR_20);
        screen2.selectProduct(1, 1); 
        
        System.out.println("\n--- Step 3: Screen 1 refunds (Releasing Stock) ---");
        screen1.refundMoney(); // Releases 2
        
        System.out.println("\n--- Step 4: Screen 2 tries to select 1 Samosa again (Should Succeed) ---");
        screen2.selectProduct(1, 1); 
        screen2.dispenseProduct();

        System.out.println("\n=== Multi-Screen Simulation Complete ===");
    }
}
