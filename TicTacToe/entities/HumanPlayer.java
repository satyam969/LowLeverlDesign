package entities;

import java.util.Scanner;

public class HumanPlayer extends Player {
    
    private static final Scanner scanner = new Scanner(System.in);

    public HumanPlayer(String name, String symbol) {
        super(name, symbol);
    }

    @Override
    public Move makeMove(Board board) {
        System.out.println(name + "'s turn (" + symbol + ").");
        System.out.println("Enter row:");
        int row = scanner.nextInt();
        System.out.println("Enter col:");
        int col = scanner.nextInt();
        return new Move(row, col, this);
    }
}
