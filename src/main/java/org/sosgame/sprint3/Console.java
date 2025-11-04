package org.sosgame.sprint3;

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
        if (sosGame == null) return false; // invalid move
        return sosGame.makeMove(row, col, letter);
    }

//    public GameMode getGameMode() {
//        return gameMode;
//    }
//
//    public void setGameMode(GameMode gameMode) {
//        this.gameMode = gameMode;
//    }

    public boolean isGameInProgress() {
        return sosGame != null && sosGame.isGameInProgress();
    }
}
