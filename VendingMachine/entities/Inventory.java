package entities;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages products and their stock levels.
 * Uses ConcurrentHashMap for thread-safe access.
 */
public class Inventory {
    private final Map<Integer, Product> products = new HashMap<>();
    private final Map<Integer, Integer> stock = new ConcurrentHashMap<>();
    private final Map<Integer, Integer> reservations = new ConcurrentHashMap<>();

    public void addProduct(Product product, int quantity) {
        products.put(product.getId(), product);
        stock.put(product.getId(), quantity);
        reservations.put(product.getId(), 0);
    }

    public boolean hasProduct(int productId, int quantity) {
        int available = stock.getOrDefault(productId, 0) - reservations.getOrDefault(productId, 0);
        return available >= quantity;
    }

    public Product getProduct(int productId) {
        return products.get(productId);
    }

    /**
     * Attempts to reserve stock for a transaction.
     * Returns true if successful.
     */
    public synchronized boolean reserveStock(int productId, int quantity) {
        int available = stock.getOrDefault(productId, 0) - reservations.getOrDefault(productId, 0);
        if (available >= quantity) {
            reservations.put(productId, reservations.getOrDefault(productId, 0) + quantity);
            return true;
        }
        return false;
    }

    /**
     * Finalizes the deduction after a successful purchase.
     */
    public synchronized void confirmStock(int productId, int quantity) {
        int currentStock = stock.getOrDefault(productId, 0);
        int currentRes = reservations.getOrDefault(productId, 0);
        
        stock.put(productId, currentStock - quantity);
        reservations.put(productId, Math.max(0, currentRes - quantity));
    }

    /**
     * Releases the reservation if the transaction fails or is cancelled.
     */
    public synchronized void releaseStock(int productId, int quantity) {
        int currentRes = reservations.getOrDefault(productId, 0);
        reservations.put(productId, Math.max(0, currentRes - quantity));
    }

    // Legacy method for single-machine flow (now internal helper or deprecated)
    @Deprecated
    public synchronized boolean deductStock(int productId, int quantity) {
        return reserveStock(productId, quantity); // For backward compatibility if needed
    }
}
