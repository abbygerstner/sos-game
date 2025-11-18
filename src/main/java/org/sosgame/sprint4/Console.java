package org.sosgame.sprint4;

public class Console {
    private SOSGame sosGame;
    private boolean vsComputer = false;   // NEW: store opponent type

    public enum GameMode {
        SIMPLE,
        GENERAL
    }

    public void setVsComputer(boolean vsComputer) {
        this.vsComputer = vsComputer;
    }

    public boolean isVsComputer() {
        return vsComputer;
    }

    public void initiateGame(int size, GameMode gameMode) {
        if (gameMode == null) gameMode = GameMode.SIMPLE;

        if (size < 3 || size > 10)
            throw new IllegalArgumentException("Board size must be between 3 and 10");

        switch (gameMode) {
            case SIMPLE:
                sosGame = new SimpleSOSGame(size, vsComputer);
                break;

            case GENERAL:
                sosGame = new GeneralSOSGame(size, vsComputer);
                break;
        }
    }

    public void setGame(SOSGame game) {
        this.sosGame = game;
    }

    public SOSGame getGame() {
        return sosGame;
    }

    public Board getBoard() {
        return sosGame != null ? sosGame.getBoard() : null;
    }

    public String getCurrentPlayer() {
        return sosGame != null ? sosGame.getCurrentPlayer() : "Red";
    }

    public boolean handleCellClick(int row, int col, char letter) {
        if (sosGame == null) return false;
        return sosGame.makeMove(row, col, letter);
    }

    public boolean isGameInProgress() {
        return sosGame != null && sosGame.isGameInProgress();
    }

    public boolean isSimpleGame() {
        return sosGame instanceof SimpleSOSGame;
    }

    public boolean isGeneralGame() {
        return sosGame instanceof GeneralSOSGame;
    }

    public GeneralSOSGame getGeneralGame() {
        return (sosGame instanceof GeneralSOSGame) ? (GeneralSOSGame) sosGame : null;
    }

    public SimpleSOSGame getSimpleGame() {
        return (sosGame instanceof SimpleSOSGame) ? (SimpleSOSGame) sosGame : null;
    }

    // Requests the next computer move from the game and returns it
    public static record ComputerMove(int row, int col, char letter) {}

    public ComputerMove makeComputerMove() {
        if (sosGame == null || !vsComputer) return null;

        // Ask the SOSGame for its computer-selected move
        SOSGame.Move move = sosGame.getComputerMove();
        if (move == null) return null;

        // Perform the move inside the game board
        boolean success = sosGame.makeMove(move.row(), move.col(), move.letter());
        if (!success) return null;

        // Return a Console-level version for the GUI
        return new ComputerMove(move.row(), move.col(), move.letter());
    }
}
