package org.sosgame.sprint2;

public class Board {
    private final int size;
    private final char[][] grid;

    public Board(int size) {
        if (size < 3 || size > 10) {
            throw new IllegalArgumentException("Board size must be between 3 and 10.");
        }
        this.size = size;
        this.grid= new char[size][size];
    }

    /** Returns board size n **/
    public int getSize() {
        return size;
    }

    /** Returns the letter at row, col or 0 if empty*/
    public char getCell(int row, int col) {
        return grid[row][col];
    }

    public boolean isEmpty(int row, int col) {
        return grid[row][col] == '\0';
    }

    public boolean placeLetter(int row, int col, char letter) {
        if (!isEmpty(row, col)) return false;
        grid[row][col] = Character.toUpperCase(letter);
        return true;
    }

}
