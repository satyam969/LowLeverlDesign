package entities;

import java.util.List;

public abstract class Player {
    protected String name;
    protected String symbol;

    public Player(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    // Abstract method to get a move. 
    // Returns List<Integer> to match existing signature, or we could switch to Move object.
    // Let's stick to Move object internally, but interface might return coords.
    // Actually, makeMove should return a Move object.
    public abstract Move makeMove(Board board);
}
