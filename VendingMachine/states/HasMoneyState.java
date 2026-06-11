package states;

import entities.Coin;
import entities.Product;
import entities.VendingMachine;

/**
 * In this state, the user can insert more money, select a product, or cancel.
 */
public class HasMoneyState implements State {

    @Override
    public void insertCoin(VendingMachine vm, Coin coin) {
        System.out.println("Additional coin inserted: " + coin);
        vm.addCoinToBalance(coin);
    }

    @Override
    public void selectProduct(VendingMachine vm, int productId, int quantity) {
        Product product = vm.getInventory().getProduct(productId);
        
        if (product == null) {
            System.out.println("Invalid Product ID.");
            return;
        }

        if (quantity <= 0) {
            System.out.println("Invalid quantity.");
            return;
        }

        if (!vm.getInventory().hasProduct(productId, quantity)) {
            System.out.println("Insufficient stock for " + product.getName() + ". Requested: " + quantity);
            return;
        }

        int totalPrice = product.getPrice() * quantity;
        if (vm.getBalanceTotal() >= totalPrice) {
            // ATOMIC RESERVATION
            if (vm.getInventory().reserveStock(productId, quantity)) {
                System.out.println("Reserved " + quantity + " x " + product.getName() + " (Total: " + totalPrice + ")");
                vm.setSelectedProductId(productId);
                vm.setSelectedQuantity(quantity);
                vm.setState(new DispensingState());
            } else {
                System.out.println("Sorry, stock was just taken by another user.");
            }
        } else {
            System.out.println("Insufficient balance for " + quantity + " x " + product.getName() + 
                ". Need: " + totalPrice + ", current: " + vm.getBalanceTotal());
        }
    }

    @Override
    public void dispenseProduct(VendingMachine vm) {
        System.out.println("Please select a product first.");
    }

    @Override
    public void refundMoney(VendingMachine vm) {
        int total = vm.getBalanceTotal();
        
        // If they had a product selected but not dispensed, we'd release here if we allowed multi-selection state
        // but currently we move to DispensingState immediately.
        
        vm.refundUser();
        System.out.println("Refunding money: " + total);
        vm.setState(new IdleState());
    }
}
