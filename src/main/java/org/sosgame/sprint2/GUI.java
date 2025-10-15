package org.sosgame.sprint2;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class GUI extends Application {
    // game model
    private static Board board;
    private static int boardSize = 8;
    private static TextField boardSizeField;
    private static GridPane grid;
    private Console console;

    public void start(Stage primaryStage) {
        //TODO: create new overall theme with custom font
        //TODO: create landing page for when app is opened, click play button to get to game screen
        //TODO: create "get help" with popup explaining how to play when app is opened
        Label titleLabel = new Label("SOS Game");
            titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            titleLabel.setPrefHeight(50);

        // sos game grid
        board = new Board(boardSize);
        console = new Console(board);
        grid = getGridPane();

        Label currentTurnLabel = new Label("Current Turn: blue (or red)");

        RadioButton simpleRadio = new RadioButton("Simple Game");
        RadioButton generalRadio = new RadioButton("General Game");

        ToggleGroup group = new ToggleGroup();
        simpleRadio.setToggleGroup(group);
        generalRadio.setToggleGroup(group);

        // three main vertical panels
        VBox leftPanel = createBluePlayerPanel();
        VBox centerPanel = createCenterPanel(titleLabel, grid, currentTurnLabel, simpleRadio, generalRadio);
        VBox rightPanel = createRedPlayerPanel();

        // main horizontal layout
        HBox root = new HBox(30);
            root.setAlignment(Pos.CENTER);
            root.setPadding(new Insets(20));
            root.getChildren().addAll(leftPanel, centerPanel, rightPanel);

            root.setStyle("-fx-background-color: #f0f0f0;");

        // scene and stage
        Scene scene = new Scene(root);
            primaryStage.setTitle("SOS Game");
            primaryStage.setScene(scene);
            primaryStage.sizeToScene();
            primaryStage.setResizable(true);
            primaryStage.show();
    }

    private VBox createBluePlayerPanel() {
        VBox bluePanel = new VBox(15);
        bluePanel.setAlignment(Pos.CENTER);
        bluePanel.setPrefWidth(200);

        Label bluePlayerLabel = new Label("Blue player");
        bluePlayerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        RadioButton sRadio = new RadioButton("S");
        sRadio.setSelected(true);
        RadioButton oRadio = new RadioButton("O");

        ToggleGroup group = new ToggleGroup();
        sRadio.setToggleGroup(group);
        oRadio.setToggleGroup(group);

        bluePanel.getChildren().addAll(bluePlayerLabel, sRadio, oRadio);
        return bluePanel;
    }

    private VBox createCenterPanel(Label titleLabel, GridPane grid, Label currentTurnLabel, RadioButton radiobutton, RadioButton radiobutton2) {
        VBox centerPanel = new VBox(20);
        centerPanel.setAlignment(Pos.CENTER);

        centerPanel.getChildren().addAll(titleLabel, grid, currentTurnLabel, radiobutton, radiobutton2);
        return centerPanel;
    }

    private VBox createRedPlayerPanel() {
        VBox redPanel = new VBox(5);
        redPanel.setAlignment(Pos.CENTER);
        redPanel.setPrefWidth(200);

        // top third section
        VBox topSection = new VBox(10);
        topSection.setAlignment(Pos.CENTER);
        HBox boardSizeBox = new HBox(5);
        boardSizeBox.setAlignment(Pos.CENTER);
        Label boardSizeLabel = new Label("Board size");
        //TODO: default 8x8, only allow digits 3-9 with arrows on right to adjust board size
        TextField boardSizeField = new TextField();
        boardSizeField.setPrefWidth(30);
        boardSizeField.setPrefHeight(25);
        boardSizeField.setMaxWidth(30);
        boardSizeBox.getChildren().addAll(boardSizeLabel, boardSizeField);

        topSection.getChildren().add(boardSizeBox);

        // middle third section
        VBox middleSection = new VBox(10);
        middleSection.setAlignment(Pos.CENTER);
        Label redPlayerLabel = new Label("Red player");
        redPlayerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        RadioButton sRadio = new RadioButton("S");
        RadioButton oRadio = new RadioButton("O");
        sRadio.setSelected(true);

        ToggleGroup group = new ToggleGroup();
        sRadio.setToggleGroup(group);
        oRadio.setToggleGroup(group);
        middleSection.getChildren().addAll(redPlayerLabel, sRadio, oRadio);

        // lower third section
        VBox bottomSection = new VBox();
        bottomSection.setAlignment(Pos.CENTER);
        Button replayButton = new Button("Replay");
        Button newGameButton = new Button("New Game");

        newGameButton.setOnAction(e -> {
            try {
                int newSize = Integer.parseInt(boardSizeField.getText());
                console.startNewGame(newSize, grid);
            } catch (NumberFormatException ex) {
                showAlert("Invalid input", "Please enter a valid number between 3 and 10.");
            } catch (IllegalArgumentException ex) {
                showAlert("Invalid size", ex.getMessage());
            }
        });
        
        bottomSection.getChildren().addAll(replayButton, newGameButton);

        VBox.setVgrow(topSection, Priority.ALWAYS);
        VBox.setVgrow(middleSection, Priority.ALWAYS);
        VBox.setVgrow(bottomSection, Priority.ALWAYS);

        redPanel.getChildren().addAll(topSection, middleSection, bottomSection);
        return redPanel;
    }

    private static void handleCellClick(Label cell, int r, int c) {
        // alternate between S and O for now TODO: handle S and O selection later
        char letter = Math.random() <0.5 ? 'S' : 'O'; // TODO: replace with player selection

        //TODO: exception handling to make sure cell is empty
        board.placeLetter(r,c,letter);
        cell.setText(String.valueOf(letter));
        System.out.println(letter + " placed at (" + r + ", " + c + ")");
    }

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private static GridPane getGridPane() {
        GridPane grid = new GridPane();
        grid.setGridLinesVisible(true);
        grid.setHgap(2);
        grid.setVgap(2);
        grid.setAlignment(Pos.CENTER);

        // TODO: Edit for loop to take input of boardSizeField textfield and create board
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                Label cell = new Label(" ");
                cell.setMinSize(50, 50);
                cell.setAlignment(Pos.CENTER);
                cell.setStyle("-fx-border-color: black; -fx-background-color: white;");

                // store coordinates as user data
                final int r = row;
                final int c = col;

                // click handler
                cell.setOnMouseClicked(event -> {
                    handleCellClick(cell,r,c);
                });

                grid.add(cell, col, row);
            }
        }
        return grid;
    }

}
