package org.sosgame.sprint5;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class FileController {

    private final Console console;
    private final Stage stage;

    public FileController(Console console, Stage stage) {
        this.console = console;
        this.stage = stage;
    }

    public void saveGame() throws Exception {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Game");
        chooser.setInitialFileName("sos-game.txt");

        File file = chooser.showSaveDialog(stage);
        if (file == null) return;

        console.saveRecording(file);
    }

    public File loadReplayFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Saved Game");
        return chooser.showOpenDialog(stage);
    }
}
