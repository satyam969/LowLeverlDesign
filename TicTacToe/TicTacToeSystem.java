import java.util.Deque;

import entities.Board;
import entities.Move;
import entities.Player;
import strategies.winning.WinningStrategy;

public class TicTacToeSystem {
    public Board board;
    public Deque<Player> players;
    public WinningStrategy winningStrategy;

    public TicTacToeSystem(Board board, Deque<Player> players, WinningStrategy winningStrategy) {
        this.board = board;
        this.players = players;
        this.winningStrategy = winningStrategy;
    }

    public void playGame() {
        while (true) {
            System.out.println("Current board state:");
            board.displayBoard();
            
            Player currentPlayer = players.pollFirst();
            Move move = currentPlayer.makeMove(board);
            
            if (board.applyMove(move)) {
                // Check Win
                if (winningStrategy.checkWinner(board, move)) {
                    board.displayBoard();
                    System.out.println("Winner is " + currentPlayer.getName());
                    break;
                }
                // Check Draw
                if (board.isFull()) {
                    board.displayBoard();
                    System.out.println("Game Draw");
                    break;
                }
                players.offerLast(currentPlayer);
            } else {
                System.out.println("Invalid Move, try again");
                players.offerFirst(currentPlayer);
            }
        }
    }
}
