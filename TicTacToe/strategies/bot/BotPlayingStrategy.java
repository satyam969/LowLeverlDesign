package strategies.bot;

import entities.Board;
import entities.Move;
import entities.Player;

/**
 * Strategy interface for Bot decision making.
 */
public interface BotPlayingStrategy {
    Move decideMove(Player player, Board board);
}
