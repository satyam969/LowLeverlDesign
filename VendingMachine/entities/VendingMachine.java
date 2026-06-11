package entities;

import states.IdleState;
import states.State;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The Context class that coordinates transitions and holds the data.
 * Enhanced with CashInventory and thread-safety.
 */
public class VendingMachine {
    private State currentState;
    private final Inventory inventory;
    private final CashInventory cashInventory;
    private final List<Coin> currentBalance;
    private int selectedProductId;
    private int selectedQuantity;
    private final ReentrantLock lock = new ReentrantLock();

    public VendingMachine() {
        this(new Inventory());
    }

    public VendingMachine(Inventory inventory) {
        this.inventory = inventory;
        this.cashInventory = new CashInventory();
        this.currentBalance = new ArrayList<>();
        this.currentState = new IdleState(); 
        this.selectedProductId = -1;
        this.selectedQuantity = 0;
    }

    public void insertCoin(Coin coin) {
        lock.lock();
        try {
            currentState.insertCoin(this, coin);
        } finally {
            lock.unlock();
        }
    }

    public void selectProduct(int productId, int quantity) {
        lock.lock();
        try {
            currentState.selectProduct(this, productId, quantity);
        } finally {
            lock.unlock();
        }
    }

    public void dispenseProduct() {
        lock.lock();
        try {
            currentState.dispenseProduct(this);
        } finally {
            lock.unlock();
        }
    }

    public void refundMoney() {
        lock.lock();
        try {
            currentState.refundMoney(this);
        } finally {
            lock.unlock();
        }
    }

    // State & Balance management
    public void setState(State state) {
        this.currentState = state;
    }

    public Inventory getInventory() { return inventory; }
    public CashInventory getCashInventory() { return cashInventory; }
    
    public void addCoinToBalance(Coin coin) {
        currentBalance.add(coin);
    }

    public int getBalanceTotal() {
        return currentBalance.stream().mapToInt(Coin::getValue).sum();
    }

    /**
     * Clears user balance and physically adds inserted coins to machine's cash vault.
     */
    public void fulfillPayment() {
        cashInventory.addCoins(currentBalance);
        currentBalance.clear();
    }

    public List<Coin> refundUser() {
        List<Coin> refund = new ArrayList<>(currentBalance);
        currentBalance.clear();
        return refund;
    }

    public int getSelectedProductId() {
        return selectedProductId;
    }

    public void setSelectedProductId(int id) {
        this.selectedProductId = id;
    }

    public int getSelectedQuantity() {
        return selectedQuantity;
    }

    public void setSelectedQuantity(int quantity) {
        this.selectedQuantity = quantity;
    }
}
