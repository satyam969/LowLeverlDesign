package states;

import entities.Coin;
import entities.Product;
import entities.VendingMachine;
import strategies.ChangeStrategy;
import java.util.List;

/**
 * Finalizing the transaction and dispensing the product with correct change.
 */
public class DispensingState implements State {
    private final ChangeStrategy changeStrategy = new ChangeStrategy();

    @Override
    public void insertCoin(VendingMachine vm, Coin coin) {
        System.out.println("Cannot insert coin while dispensing.");
    }

    @Override
    public void selectProduct(VendingMachine vm, int productId, int quantity) {
        System.out.println("Already dispensing another item.");
    }

    @Override
    public void dispenseProduct(VendingMachine vm) {
        int productId = vm.getSelectedProductId();
        int quantity = vm.getSelectedQuantity();
        Product product = vm.getInventory().getProduct(productId);
        
        int totalPrice = product.getPrice() * quantity;
        int changeRequired = vm.getBalanceTotal() - totalPrice;
        
        // 1. Check if we can give change
        List<Coin> changeCoins = changeStrategy.calculateChange(changeRequired, vm.getCashInventory());
        
        if (changeRequired > 0 && changeCoins == null) {
            System.out.println("ERROR: Insufficient specific coins for change. Transaction aborted.");
            vm.getInventory().releaseStock(productId, quantity); // RELEASE RESERVATION
            vm.refundUser();
            vm.setState(new IdleState());
            return;
        }

        // 2. Clear user balance & Add coins to machine vault
        vm.fulfillPayment();
        
        // 3. Confirm product stock deduction (Stock was reserved in HasMoneyState)
        vm.getInventory().confirmStock(productId, quantity);
        
        // 4. Deduct change coins from cash vault
        if (changeCoins != null) {
            for (Coin c : changeCoins) {
                vm.getCashInventory().deductCoin(c);
            }
        }
        
        System.out.println("Dispensed: " + quantity + " x " + product.getName());
        if (changeRequired > 0) {
            System.out.println("Returning change (" + changeRequired + "): " + changeCoins);
        }
        
        vm.setSelectedProductId(-1);
        vm.setSelectedQuantity(0);
        vm.setState(new IdleState());
    }

    @Override
    public void refundMoney(VendingMachine vm) {
        int productId = vm.getSelectedProductId();
        int quantity = vm.getSelectedQuantity();
        
        System.out.println("Cancelling transaction. Releasing stock and refunding...");
        vm.getInventory().releaseStock(productId, quantity);
        vm.refundUser();
        
        vm.setSelectedProductId(-1);
        vm.setSelectedQuantity(0);
        vm.setState(new IdleState());
    }
}
