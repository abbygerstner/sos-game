package org.sosgame.sprint5;

import java.io.File;

public class Console {
    private SOSGame sosGame;
    private boolean blueIsComputer = false;
    private boolean redIsComputer = false;
    private GameRecorder recorder;
    private boolean isReplaying = false;
    private boolean replaying = false;
    private static final ThreadLocal<Boolean> isReplayCall = ThreadLocal.withInitial(() -> false);

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

        if (size < 3 || size > 10)
            throw new IllegalArgumentException("Board size must be between 3 and 10");

        switch (gameMode) {
            case SIMPLE:
                sosGame = new SimpleSOSGame(size, blueIsComputer, redIsComputer);
                break;

            case GENERAL:
                sosGame = new GeneralSOSGame(size, blueIsComputer, redIsComputer);
                break;
        }
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
        if (sosGame == null) return false;
        if (isReplaying) return false;

        if (isReplaying() && !isReplayCall.get()) {
            return false;
        }

        String player = sosGame.getCurrentPlayer();
        boolean success = sosGame.makeMove(row, col, letter);

        if (success && recorder != null) {
            recorder.recordMove(player, row, col, letter);
        }

//      Auto-save when game ends
        if (success && !sosGame.isGameInProgress() && recorder != null) {
            try {
//                File file = new File("sos_replay.txt");
//                recorder.saveToFile(file);
//                System.out.println("Replay saved to: " + file.getAbsolutePath());
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

    // Requests the next computer move from the game and returns it
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

    public void markReplayCall(Runnable action) {
        isReplayCall.set(true);
        action.run();
        isReplayCall.set(false);
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
}
