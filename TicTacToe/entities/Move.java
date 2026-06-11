package entities;

/**
 * Represents a move made by a player.
 */
public class Move {
    public int row;
    public int col;
    public Player player;

    public Move(int row, int col, Player player) {
        this.row = row;
        this.col = col;
        this.player = player;
    }
}
