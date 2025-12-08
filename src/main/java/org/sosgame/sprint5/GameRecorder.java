package org.sosgame.sprint5;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameRecorder {

    private final List<String> recordedLines = new ArrayList<>();

    public void recordHeader(int size, Console.GameMode mode) {
        recordedLines.add("SIZE " + size);
        recordedLines.add("MODE " + mode.name());
    }

    public void recordMove(String player, int row, int col, char letter) {
        recordedLines.add("MOVE " + player + " " + row + " " + col + " " + letter);
    }

    public void saveToFile(File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (String line : recordedLines) {
                writer.println(line);
            }
        }
    }

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

