package org.sosgame.sprint5;

public class Board {
    private int size;
    private char[][] grid;
    private String errorMessage;

    public Board(int size) {
        setBoardSize(size);
    }


    /** Sets board size between 3 and 10, returns true if successful, false if unsuccessful **/
    public boolean setBoardSize(int size) {
        if (size < 3 || size > 10) {
            errorMessage = "Size must be between 3 and 10";
            return false;
        }
        this.size = size;
        this.grid = new char[size][size];
                this.errorMessage = null;
        return true;
    }

    /** Returns board size n **/
    public int getSize() {
        return size;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    /** Returns the letter at row, col or 0 if empty*/
    public char getCell(int row, int col) {
        return grid[row][col];
    }

    public boolean isEmpty(int row, int col) {
        return grid[row][col] == '\0';
    }

    public void placeLetter(int row, int col, char letter) {
        if (!isEmpty(row, col)) return;
        grid[row][col] = Character.toUpperCase(letter);
    }

    public char[][] getGrid() {
        return grid;
    }

    public boolean isBoardFull() {
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                if (isEmpty(i, j)) return false;
            }
        }
        return true;
    }

    public boolean isCellEmpty(int r, int c) {
        return grid[r][c] == '\0' || grid[r][c] == ' ';
    }

    public void setCell(int r, int c, char letter) {
        grid[r][c] = letter;
    }

}
