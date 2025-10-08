package org.sosgame.sprint2;

import javafx.application.Application;

public class Console {
    private Board board;

    public Console(Board board) {
        this.board = board;
    }

    public static void main(String[] args) {
        Application.launch(GUI.class, args);
    }
}
