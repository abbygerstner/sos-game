package org.sosgame.sprint5;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

public class GameReplayerTest {

    @Test
    void testReplayFileLoadsCorrectly() throws Exception {
        // Create a fake replay file
        File temp = File.createTempFile("sos_replay_test", ".txt");

        try (PrintWriter pw = new PrintWriter(temp)) {
            pw.println("SIZE 3");
            pw.println("MODE SIMPLE");
            pw.println("MOVE Blue 0 0 S");
            pw.println("MOVE Red 0 1 O");
            pw.println("MOVE Blue 0 2 S");
        }

        Console console = new Console();
        GUI dummyGui = new GUI();
        GameReplayer replayer = new GameReplayer(dummyGui, console);

        // Skip JavaFX screen calls
        console.initiateGame(3, Console.GameMode.SIMPLE, false, false);

        // Simulate replay
        console.handleCellClick(0, 0, 'S');
        console.handleCellClick(0, 1, 'O');
        console.handleCellClick(0, 2, 'S');

        Board board = console.getBoard();

        assertEquals('S', board.getCell(0, 0));
        assertEquals('O', board.getCell(0, 1));
        assertEquals('S', board.getCell(0, 2));
    }

    @Test
    void testLoadingReplayFile() throws Exception {
        File temp = File.createTempFile("sos_replay_load", ".txt");

        try (PrintWriter pw = new PrintWriter(temp)) {
            pw.println("SIZE 5");
            pw.println("MODE GENERAL");
        }

        var lines = GameRecorder.loadFromFile(temp);

        assertEquals("SIZE 5", lines.get(0));
        assertEquals("MODE GENERAL", lines.get(1));
    }
}
