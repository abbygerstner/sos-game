package org.sosgame.sprint5;

import java.util.List;

public abstract class SOSGame {
    protected Board board;
    protected boolean gameInProgress;
    protected String winner;
    protected GameEventListener listener;
    protected Player redPlayer;
    protected Player bluePlayer;
    protected Player currentPlayerObj;
    protected abstract List<SOSSequence> getSequencesFromLastMove(int row, int col);

    public SOSGame(int size, boolean isBlueCPU, boolean isRedCPU) {
        this.board = new Board(size);
        bluePlayer = isBlueCPU ? new ComputerPlayer("Blue") : new HumanPlayer("Blue");
        redPlayer  = isRedCPU ? new ComputerPlayer("Red") : new HumanPlayer("Red");
        currentPlayerObj = bluePlayer;
        this.gameInProgress = true;
    }

    public Board getBoard() {
        return board;
    }

    public String getCurrentPlayer() {
        return currentPlayerObj.getColor();
    }

    public void switchTurn() {
        if (currentPlayerObj == redPlayer) {
            currentPlayerObj = bluePlayer;
        } else {
            currentPlayerObj = redPlayer;
        }
    }

    public boolean makeMove(int row, int col, char letter) {
        // Human move
        boolean placed = attemptMove(row, col, letter);
        if (!placed) return false;
        if (!gameInProgress) return true;
        boolean formedSOS = formedSOSLastMove(row, col);

        // For general game: if SOS formed, same player goes again
        if (formedSOS && (this instanceof GeneralSOSGame)) {
            return true;
        }
        // Else switch to the other player
        switchTurn();

        return true;
    }

    public Move getFirstAvailableMove() {
        char[][] grid = board.getGrid();
        int n = board.getSize();

        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                if (grid[r][c] == '\0' || grid[r][c] == ' ') {
                    return new Move(r, c, randomLetter());
                }
            }
        }
        return null;
    }

    private char randomLetter() {
        return Math.random() < 0.5 ? 'S' : 'O';
    }

    protected boolean attemptMove(int row, int col, char letter) {
        if (!gameInProgress) return false;

        if (!board.isEmpty(row, col)) return false;

        board.setCell(row, col, letter);

        if (listener != null) {
            listener.onMoveMade(row, col, letter, currentPlayerObj.getColor());
        }

        if (!gameInProgress && listener != null) {
            listener.onGameOver(winner);
        }

        return true;  // always true if move placed
    }

    protected boolean formedSOSLastMove(int row, int col) {
        return checkWinner(row, col);
    }

    public String getWinner() { return winner; }

    public void setListener(GameEventListener listener) {
        this.listener = listener;
    }

    public boolean isGameInProgress() {
        return gameInProgress;
    }

    // Each game mode defines its own winner logic
    protected abstract boolean checkWinner(int row, int col);

    public record SOSSequence(int row1, int col1, int row2, int col2, int row3, int col3, String player) {}

    public record Move(int row, int col, char letter) {}

    protected boolean inBounds(int r, int c, int n) {
        return r >= 0 && r < n && c >= 0 && c < n;
    }

    // default (simple) logic
    public Move getComputerMove() {
        return getFirstAvailableMove();
    }

    public boolean currentPlayerIsComputer() {
        return currentPlayerObj instanceof ComputerPlayer;
    }

}
