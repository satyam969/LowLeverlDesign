import entities.Board;
import entities.Move;
import entities.Player;
import strategies.winning.OrderOneWinningStrategy;
import strategies.winning.WinningStrategy;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class TicTacToeSimulation {

    // Helper class for scripted moves
    static class ScriptedPlayer extends Player {
        private Queue<Integer> moves;

        public ScriptedPlayer(String name, String symbol, Integer... coords) {
            super(name, symbol);
            this.moves = new LinkedList<>();
            for (Integer i : coords) {
                moves.add(i);
            }
        }

        @Override
        public Move makeMove(Board board) {
            if (moves.isEmpty()) return null;
            int r = moves.poll();
            int c = moves.poll();
            System.out.println(name + " plays (" + r + ", " + c + ")");
            return new Move(r, c, this);
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting Deterministic Simulation...");

        // 1. Setup Board
        int size = 3;
        Board board = new Board(size, size);

        // 2. Setup Players with Pre-defined moves for a Win scenario
        // X wins via Row 0: (0,0), (0,1), (0,2)
        // O plays dummy moves: (1,0), (1,1)
        Player p1 = new ScriptedPlayer("PlayerX", "X", 
            0, 0, // Move 1
            0, 1, // Move 2
            0, 2  // Move 3 (WIN)
        );
        
        Player p2 = new ScriptedPlayer("PlayerO", "O",
            1, 0, // Move 1
            1, 1  // Move 2
        );

        Deque<Player> players = new ArrayDeque<>();
        players.add(p1);
        players.add(p2);

        // 3. Strategy
        WinningStrategy winningStrategy = new OrderOneWinningStrategy(size);

        // 4. Run Game Loop manually to avoid infinite loop if logic fails
        int maxTurns = 10;
        int turn = 0;
        boolean gameWon = false;

        while (turn < maxTurns && !gameWon) {
            Player current = players.pollFirst();
            Move move = current.makeMove(board);
            
            if (move != null && board.applyMove(move)) {
                if (winningStrategy.checkWinner(board, move)) {
                    System.out.println("WINNER DETECTED: " + current.getName());
                    gameWon = true;
                }
                players.addLast(current);
            } else {
                 System.out.println("Invalid or No Move from " + current.getName());
                 break;
            }
            turn++;
        }

        if (gameWon) {
            System.out.println("Simulation SUCCESS: Winner found correctly.");
        } else {
            System.out.println("Simulation FAILED: No winner detected.");
        }
    }
}
