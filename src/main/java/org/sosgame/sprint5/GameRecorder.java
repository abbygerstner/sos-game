package org.sosgame.sprint5;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameRecorder {

    private final List<String> recordedLines = new ArrayList<>();

    // Call when a game starts
    public void recordHeader(int size, Console.GameMode mode) {
        recordedLines.add("SIZE " + size);
        recordedLines.add("MODE " + mode.name());
    }

    // Call every time a move happens
    public void recordMove(String player, int row, int col, char letter) {
        recordedLines.add("MOVE " + player + " " + row + " " + col + " " + letter);
    }

    // Save everything to a file
    public void saveToFile(File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (String line : recordedLines) {
                writer.println(line);
            }
        }
    }

    // Static loader for replay
    public static List<String> loadFromFile(File file) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line.trim());
            }
        }
        return lines;
    }
}

