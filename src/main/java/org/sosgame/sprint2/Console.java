package org.sosgame.sprint2;

public class Console {
    private Board board;
    private String currentPlayer;
    private boolean gameInProgress;
    private GameMode gameMode;

    public enum GameMode {
        SIMPLE,
        GENERAL
    }

    public Console(Board board) {
        this.board = board;
        this.currentPlayer = "Red";
        this.gameMode = GameMode.SIMPLE;
    }

    public void startNewGame(int size, GameMode gameMode) {
        if (size < 3 || size > 10) {
            throw new IllegalArgumentException("Board size must be between 3 and 10.");
        }

        if (gameMode == null) gameMode = GameMode.SIMPLE;

        this.board = new Board(size);
        this.gameMode = gameMode;
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

    public boolean handleCellClick(int row, int col, char selectedLetter) {
        if (!board.isEmpty(row, col)) {
            return false; // invalid move
        }
        board.placeLetter(row, col, selectedLetter);
        switchTurn();
        return true;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public boolean isGameInProgress() {
        return gameInProgress;
    }
}
