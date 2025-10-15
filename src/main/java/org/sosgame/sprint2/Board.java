package org.sosgame.sprint2;

public class Board {
    private final int size;
    private final char[][] grid;
//    private char turn = 'Red';

    public Board(int size) {
        if (size < 3) {
            throw new IllegalArgumentException("Board size must be at least 3x3.");
        }
        if (size > 10) {
            throw new IllegalArgumentException("Board size cannot be larger than 10x10.");
        }
        this.size = size;
        this.grid= new char[size][size];
    }

    /** Returns board size n **/
    public int getSize() {
        return size;
    }

    /** Returns grid **/
    public char[][] getGrid() {
        return grid;
    }

    /** Returns the letter at row, col or 0 if empty*/
    public char getCell(int row, int col) {
        return grid[row][col];
    }

//    public char getTurn() {
//        return this.turn;
//    }

    public boolean placeLetter(int row, int col, char letter) {
        // TODO: add isValidMove here
        grid[row][col] = Character.toUpperCase(letter);
        //this.turn = (char)(this.turn == 'X' ? 79 : 88); TODO: create turn iteration
        return true;
    }

}
