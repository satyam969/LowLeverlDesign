package states;

import entities.Coin;
import entities.VendingMachine;

/**
 * Vending machine is waiting for user interaction.
 */
public class IdleState implements State {

    @Override
    public void insertCoin(VendingMachine vm, Coin coin) {
        System.out.println("Coin inserted: " + coin);
        vm.addCoinToBalance(coin);
        vm.setState(new HasMoneyState());
    }

    @Override
    public void selectProduct(VendingMachine vm, int productId, int quantity) {
        System.out.println("No money inserted. Please insert coins first.");
    }

    @Override
    public void dispenseProduct(VendingMachine vm) {
        System.out.println("Payment required.");
    }

    @Override
    public void refundMoney(VendingMachine vm) {
        System.out.println("No balance to refund.");
    }
}
