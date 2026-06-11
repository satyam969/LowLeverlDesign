import entities.Coin;
import entities.Product;
import entities.VendingMachine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Verifies that the Vending Machine handles concurrent coin insertion 
 * and state transitions correctly.
 */
public class ConcurrentVendingSimulation {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Starting Concurrent Vending Machine Simulation ===");

        VendingMachine vm = new VendingMachine();
        vm.getInventory().addProduct(new Product(1, "Premium Soda", 200), 10);

        ExecutorService executor = Executors.newFixedThreadPool(5);

        // Scenario: 5 people try to insert a Dollar at the same time
        System.out.println("Dispatching 5 threads to insert DOLLAR coins simultaneously...");
        for (int i = 0; i < 5; i++) {
            executor.execute(() -> {
                vm.insertCoin(Coin.DOLLAR);
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("Final Balance: " + vm.getBalanceTotal() + "c (Expected 500c)");

        if (vm.getBalanceTotal() == 500) {
            System.out.println("SUCCESS: Concurrency handled. All coins accounted for.");
        } else {
            System.out.println("FAILURE: Race condition detected.");
        }

        // Scenario: Concurrent Select while Dispensing
        System.out.println("\nTesting Selection while Dispensing...");
        vm.selectProduct(1, 1); 
        
        // Wait, Dispense is a separate action.
        // If we dispense, it should lock.
        new Thread(() -> {
            vm.dispenseProduct();
        }).start();

        // Immediately try to insert another coin from another thread
        Thread.sleep(50);
        System.out.println("Attempting to insert coin during dispense...");
        vm.insertCoin(Coin.QUARTER); 

        System.out.println("=== Concurrent Simulation Complete ===");
    }
}
