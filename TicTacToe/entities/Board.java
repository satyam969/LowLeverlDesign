package entities;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public int row;
    public int col;
    public Cell[][] cells;
    public int filledCells;

    public Board(int row, int col) {
        this.row = row;
        this.col = col;
        this.filledCells = 0;
        cells = new Cell[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                cells[i][j] = new Cell();
            }
        }
    }

    public boolean applyMove(Move move) {
        int r = move.row;
        int c = move.col;
        if (r < 0 || r >= this.row || c < 0 || c >= this.col) {
            return false;
        }        
        if(!cells[r][c].isEmpty()) {
            return false;
        }
        cells[r][c].setSymbol(move.player.getSymbol());
        filledCells++;
        return true;
    }
    
    // Undo logic for potential minimax or rollback
    public void undoMove(Move move) {
        int r = move.row;
        int c = move.col;
        cells[r][c].setSymbol("");
        filledCells--;
    }

    public boolean isFull() {
        return filledCells == row * col;
    }

    public void displayBoard() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                System.out.print(cells[i][j].symbol.isEmpty() ? "-" : cells[i][j].symbol);
                if (j < col - 1) System.out.print(" | ");
            }
            System.out.println();
            if (i < row - 1) {
                for (int k = 0; k < col; k++) {
                    System.out.print("---");
                    if (k < col - 1) System.out.print("+");
                }
                System.out.println();
            }
        }
    }
}
