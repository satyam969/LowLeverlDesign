package strategies.winning;

import entities.Board;
import entities.Move;

/**
 * Strategy interface for determining if a player has won.
 */
public interface WinningStrategy {
    boolean checkWinner(Board board, Move move);
}
