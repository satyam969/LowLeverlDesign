package states;

import entities.Coin;
import entities.VendingMachine;

/**
 * State interface defining possible actions in any vending machine phase.
 */
public interface State {
    void insertCoin(VendingMachine vm, Coin coin);
    void selectProduct(VendingMachine vm, int productId, int quantity);
    void dispenseProduct(VendingMachine vm);
    void refundMoney(VendingMachine vm);
}
