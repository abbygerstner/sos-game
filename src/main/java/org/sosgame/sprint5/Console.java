package org.sosgame.sprint5;

import java.io.File;

public class Console {
    private SOSGame sosGame;
    private boolean blueIsComputer = false;
    private boolean redIsComputer = false;
    private GameRecorder recorder;
    private boolean replaying = false;
    private static final int MIN_BOARD_SIZE = 3;
    private static final int MAX_BOARD_SIZE = 10;

    public enum GameMode {
        SIMPLE,
        GENERAL
    }

    public void setBlueIsComputer(boolean value) {
        this.blueIsComputer = value;
    }

    public void setRedIsComputer(boolean value) {
        this.redIsComputer = value;
    }

    public void initiateGame(int size, GameMode gameMode,
                             boolean redIsComputer, boolean blueIsComputer) {
        if (gameMode == null) gameMode = GameMode.SIMPLE;

        if (size < MIN_BOARD_SIZE || size > MAX_BOARD_SIZE)
            throw new IllegalArgumentException("Board size must be between 3 and 10");

        this.redIsComputer = redIsComputer;
        this.blueIsComputer = blueIsComputer;

        sosGame = GameFactory.createGame(gameMode, size, blueIsComputer, redIsComputer);
        enableRecording(size, gameMode);
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

    public boolean handleCellClick(int row, int col, char letter) throws Exception {
        if (sosGame == null) {
            throw new IllegalStateException("Game not initialized");
        }

        if (letter != 'S' && letter != 'O') {
            throw new IllegalArgumentException("Invalid letter: " + letter);
        }

        if (replaying) return false;

        String player = sosGame.getCurrentPlayer();
        boolean success = sosGame.makeMove(row, col, letter);

        if (success && recorder != null) {
            recorder.recordMove(player, row, col, letter);
        }

        if (success && !sosGame.isGameInProgress() && recorder != null) {
            try {
                saveRecordingAuto();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return success;
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

    public static record ComputerMove(int row, int col, char letter) {}

    public ComputerMove makeComputerMove() {
        if (sosGame == null) return null;

        if (!sosGame.currentPlayerIsComputer()) return null;

        SOSGame.Move move = sosGame.getComputerMove();
        if (move == null) return null;

        boolean success = sosGame.makeMove(move.row(), move.col(), move.letter());
        if (!success) return null;

        if (recorder != null) {
            String player = sosGame.getCurrentPlayer();
            recorder.recordMove(player, move.row(), move.col(), move.letter());
        }

        return new ComputerMove(move.row(), move.col(), move.letter());
    }

    public void enableRecording(int size, GameMode mode) {
        recorder = new GameRecorder();
        recorder.recordHeader(size, mode);
        recorder.recordPlayerTypes(redIsComputer, blueIsComputer);
    }

    public void saveRecording(File file) throws Exception {
        if (recorder == null) return;
        recorder.saveToFile(file);
    }

    public void setReplaying(boolean value) {
        replaying = value;
    }

    public boolean isReplaying() {
        return replaying;
    }

    public void saveRecordingAuto() throws Exception {
        if (recorder == null) return;

        File dir = new File("recordings");

        if (!dir.exists()) {
            dir.mkdirs();
        }

        int count = 1;
        File file;

        do {
            file = new File(dir, "sos_game_" + count + ".txt");
            count++;
        } while (file.exists());

        recorder.saveToFile(file);

        System.out.println("Saved replay file to: " + file.getAbsolutePath());
    }

    public void applyReplayMove(int row, int col, char letter) throws Exception {
        if (sosGame == null) return;

        String player = sosGame.getCurrentPlayer();
        boolean success = sosGame.makeMove(row, col, letter);
    }
}
