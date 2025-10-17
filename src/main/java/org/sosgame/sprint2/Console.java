package org.sosgame.sprint2;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;

public class Console {
    private Board board;
    private String currentPlayer = "Red";

    public Console(Board board) {
        this.board = board;
    }

    public void startNewGame(int size, GridPane grid) {
        if (size < 3 || size > 10) {
            throw new IllegalArgumentException("Board size must be between 3 and 10.");
        }

        this.board = new Board(size);
        grid.getChildren().clear();

        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                Label cell = new Label(" ");
                cell.setMinSize(50, 50);
                cell.setAlignment(javafx.geometry.Pos.CENTER);
                cell.setStyle("-fx-border-color: black; -fx-background-color: white;");

                final int r = row;
                final int c = col;

                cell.setOnMouseClicked(e -> handleCellClick(cell, r, c));
                grid.add(cell, col, row);
            }
        }
    }

    private void handleCellClick(Label cell, int row, int col) {
        // TODO: GUI will set letter and style, check SOS patterns, update scores
    }

    public Board getBoard() {
        return board;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchTurn() {
        currentPlayer = currentPlayer.equals("Red") ? "Blue" : "Red";
    }
}
