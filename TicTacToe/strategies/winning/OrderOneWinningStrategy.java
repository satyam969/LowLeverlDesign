package strategies.winning;

import entities.Board;
import entities.Move;
import entities.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Optimised Winning Strategy.
 * Checks for a winner in O(1) time complexity by maintaining move counts.
 */
public class OrderOneWinningStrategy implements WinningStrategy {

    // Maps integer (Row/Col Index) -> (Symbol -> Count)
    private final List<Map<String, Integer>> rowMaps;
    private final List<Map<String, Integer>> colMaps;
    private final Map<String, Integer> leftDiagMap;
    private final Map<String, Integer> rightDiagMap;
    private final int size;

    public OrderOneWinningStrategy(int size) {
        this.size = size;
        this.rowMaps = new ArrayList<>();
        this.colMaps = new ArrayList<>();
        this.leftDiagMap = new HashMap<>();
        this.rightDiagMap = new HashMap<>();

        for (int i = 0; i < size; i++) {
            rowMaps.add(new HashMap<>());
            colMaps.add(new HashMap<>());
        }
    }

    @Override
    public boolean checkWinner(Board board, Move move) {
        int row = move.row;
        int col = move.col;
        String symbol = move.player.getSymbol();

        // Update Row Count
        Map<String, Integer> rowMap = rowMaps.get(row);
        rowMap.put(symbol, rowMap.getOrDefault(symbol, 0) + 1);

        // Update Col Count
        Map<String, Integer> colMap = colMaps.get(col);
        colMap.put(symbol, colMap.getOrDefault(symbol, 0) + 1);

        // Update Left Diagonal (r==c)
        if (row == col) {
            leftDiagMap.put(symbol, leftDiagMap.getOrDefault(symbol, 0) + 1);
        }

        // Update Right Diagonal (r+c == N-1)
        if (row + col == size - 1) {
            rightDiagMap.put(symbol, rightDiagMap.getOrDefault(symbol, 0) + 1);
        }

        // Check if any count reached size
        if (rowMap.get(symbol) == size) return true;
        if (colMap.get(symbol) == size) return true;
        if (row == col && leftDiagMap.get(symbol) == size) return true;
        if (row + col == size - 1 && rightDiagMap.get(symbol) == size) return true;

        return false;
    }
}
