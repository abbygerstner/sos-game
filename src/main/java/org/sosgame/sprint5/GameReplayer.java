package org.sosgame.sprint5;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.File;
import java.util.List;

public class GameReplayer {

    private final GUI gui;
    private final Console console;

    public GameReplayer(GUI gui, Console console) {
        this.gui = gui;
        this.console = console;
    }

    public void replayFromFile(File file) throws Exception {
        console.setReplaying(true);
        System.out.println("REPLAY: started; console.isReplaying=" + console.isReplaying());
        List<String> lines = GameRecorder.loadFromFile(file);

        int size = 0;
        Console.GameMode mode = Console.GameMode.SIMPLE;

        for (String line : lines) {
            if (line.startsWith("SIZE")) size = Integer.parseInt(line.split(" ")[1]);
            if (line.startsWith("MODE")) mode = Console.GameMode.valueOf(line.split(" ")[1]);
        }

        console.initiateGame(size, mode, false, false);
        gui.showGameScreen();

        Timeline timeline = new Timeline();
        int delay = 0;

        for (String line : lines) {
            if (!line.startsWith("MOVE")) continue;

            String[] parts = line.split(" ");
            String player = parts[1];
            int row = Integer.parseInt(parts[2]);
            int col = Integer.parseInt(parts[3]);
            char letter = parts[4].charAt(0);

            delay += 500;
            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.millis(delay), e -> {
                        console.markReplayCall(() -> {
                            try {
                                console.handleCellClick(row, col, letter);
                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                        });
                    })
            );
        }

        timeline.play();

        timeline.setOnFinished(e -> {
            console.setReplaying(false);
//            System.out.println("REPLAY: finished");
        });
    }
}
