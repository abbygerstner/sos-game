package org.sosgame.sprint4;

import java.util.List;

public abstract class SOSGame {
    protected Board board;
    protected boolean gameInProgress;
    protected String winner;
    protected GameEventListener listener;
    protected Player redPlayer;
    protected Player bluePlayer;
    protected Player currentPlayerObj;
    protected boolean vsComputer;
    protected abstract List<SOSSequence> getSequencesFromLastMove(int row, int col);

    public SOSGame(int size, boolean vsComputer) {
        this.board = new Board(size);
        this.vsComputer = vsComputer;

        // Blue is always human and goes first
        bluePlayer = new HumanPlayer("Blue");

        // Red is either human or computer
        redPlayer = vsComputer ? new ComputerPlayer("Red")
                : new HumanPlayer("Red");
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
        boolean valid = attemptMove(row, col, letter);
        if (!valid) return false;

        // If game ended, stop here.
        if (!gameInProgress) return true;

        switchTurn();

        // If new current player is computer, autoplay
        if (currentPlayerObj.isComputer()) {
            Move m = currentPlayerObj.getMove(this);
            attemptMove(m.row, m.col, m.letter);
            System.out.println("Computer played " + m.letter + " at " + m.row + "," + m.col);
            if (gameInProgress)
                switchTurn();
        }
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

        // Can't play if the game is already over
        if (!gameInProgress) return false;

        // Check bounds and that cell is empty
        if (!board.isCellEmpty(row, col)) return false;

        // Place the letter
        board.setCell(row, col, letter);

        if (listener != null) {
            listener.onMoveMade(row, col, letter, currentPlayerObj.getColor());
        }

        // Check for winner (each mode implements its own check)
        boolean formedSOS = checkWinner(row, col);

        if (formedSOS && listener != null) {
            listener.onSOSFormed(getSequencesFromLastMove(row, col));
        }

        // If winner declared inside checkWinner(), stop the game
        if (!gameInProgress) {
            if (listener != null) {
                listener.onGameOver(winner);
            }
        }

        return true;
    }

    public String getWinner() { return winner; }
    
    public void setListener(GameEventListener listener) {
        this.listener = listener;
    }

    public boolean isGameInProgress() {
        return gameInProgress;
    }

    /** Each game mode defines its own winner logic */
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

}
