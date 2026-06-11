package strategies.bot;

import entities.Board;
import entities.Cell;
import entities.Move;
import entities.Player;

/**
 * Strategy that picks the first available empty cell.
 * A true "Random" strategy would collect all empty cells and pick one randomly.
 * For simplicity, we scan and pick the first one.
 */
public class RandomBotPlayingStrategy implements BotPlayingStrategy {

    @Override
    public Move decideMove(Player player, Board board) {
        for (int i = 0; i < board.row; i++) {
            for (int j = 0; j < board.col; j++) {
                if (board.cells[i][j].isEmpty()) {
                    return new Move(i, j, player);
                }
            }
        }
        return null; // Should not happen if game checks for draw
    }
}
