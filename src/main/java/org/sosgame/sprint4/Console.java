package org.sosgame.sprint4;

public class Console {
    private SOSGame sosGame;

    public enum GameMode {
        SIMPLE,
        GENERAL
    }

    public void initiateGame(int size, GameMode gameMode) {
        if (gameMode == null) gameMode = GameMode.SIMPLE;
        if (size < 3 || size > 10)
            throw new IllegalArgumentException("Board size must be between 3 and 10");
        if (gameMode == GameMode.SIMPLE)
            sosGame = new SimpleSOSGame(size);
        else
            sosGame = new GeneralSOSGame(size);
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
}
