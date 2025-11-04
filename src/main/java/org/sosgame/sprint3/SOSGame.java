package org.sosgame.sprint3;

public abstract class SOSGame {
    protected Board board;
    protected String currentPlayer;
    protected boolean gameInProgress;

    public SOSGame(int size) {
        this.board = new Board(size);
        this.currentPlayer = "Red";
        this.gameInProgress = true;
    }

    public Board getBoard() {
        return board;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchTurn() {
        currentPlayer = currentPlayer.equals("Red") ? "Blue" : "Red";
    }

    public boolean makeMove(int row, int col, char letter) {
        if (!board.isEmpty(row, col)) {
            return false; // invalid move
        }

        board.placeLetter(row, col, letter);
        boolean scored = checkWinner(row, col);

        if (!scored) {
            switchTurn();
        }

        return true;
    }

    public boolean isGameInProgress() {
        return gameInProgress;
    }

    /** Each game mode defines its own winner logic */
    protected abstract boolean checkWinner(int row, int col);
}
