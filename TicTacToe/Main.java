import java.util.ArrayDeque;
import java.util.Deque;

import entities.Board;
import entities.BotPlayer;
import entities.HumanPlayer;
import entities.Player;
import strategies.bot.RandomBotPlayingStrategy;
import strategies.winning.OrderOneWinningStrategy;
import strategies.winning.WinningStrategy;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Tic-Tac-Toe Game...");
        
        // 1. Initialize Board
        int size = 3;
        Board board = new Board(size, size);

        // 2. Initialize Players (Human vs Bot)
        Player human = new HumanPlayer("Satya", "X");
        Player bot = new BotPlayer("Bot", "O", new RandomBotPlayingStrategy());

        Deque<Player> players = new ArrayDeque<>();
        players.add(human);
        players.add(bot);

        // 3. Initialize Winning Strategy
        WinningStrategy winningStrategy = new OrderOneWinningStrategy(size);

        // 4. Start Game
        TicTacToeSystem game = new TicTacToeSystem(board, players, winningStrategy);
        game.playGame();
    }
}
