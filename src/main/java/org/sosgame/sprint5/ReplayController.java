package org.sosgame.sprint5;

import javafx.stage.Stage;
import java.io.File;

public class ReplayController {

    private final GameReplayer replayer;
    private final FileController fileController;

    public ReplayController(GUI gui, Console console, Stage stage) {
        this.replayer = new GameReplayer(gui, console);
        this.fileController = new FileController(console, stage);
    }

    public void startReplay() {
        try {
            File file = fileController.loadReplayFile();
            if (file == null) return;
            replayer.replayFromFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

