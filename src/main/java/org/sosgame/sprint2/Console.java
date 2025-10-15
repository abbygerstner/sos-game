package org.sosgame.sprint2;

import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class Console {
    private Board board;

    public Console(Board board) {
        this.board = board;
    }

    public void startNewGame(int size, GridPane grid) {
        if (size < 3 || size > 10) {
            throw new IllegalArgumentException("Board size must be between 3 and 10.");
        }

        // create new board
        board = new Board(size);

        // rebuild grid
        grid.getChildren().clear();
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                Label cell = new Label(" ");
                cell.setMinSize(50, 50);
                cell.setStyle("-fx-border-color: black; -fx-background-color: white;");
                cell.setAlignment(javafx.geometry.Pos.CENTER);

                final int r = row;
                final int c = col;
                cell.setOnMouseClicked(e -> {
                    board.placeLetter(r, c, 'S'); // S is a placeholder
                    cell.setText(String.valueOf(board.getCell(r, c)));
                });

                grid.add(cell, col, row);
            }
        }
    }
}
