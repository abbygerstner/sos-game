package org.sosgame.sprint2;

public class Board {
    private int size;
    private char[][] grid;
    private GameMode gameMode;
    private String errorMessage;

    public enum GameMode {
        SIMPLE,
        GENERAL
    }

    public Board(int size) {
        setBoardSize(size);
        this.gameMode = GameMode.SIMPLE; // default
    }

    public GameMode getGameMode() {
        return gameMode;
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

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
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

}
