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
    private static Board board;
    private static GridPane grid;
    private static Console console;
    private static String currentPlayer = "Red";
    private static ToggleGroup blueGroup;
    private static ToggleGroup redGroup;
    private static Label currentTurnLabel;
    private static HBox root;
    private static VBox centerPanel;
    private static Console.GameMode gameMode;

@Override
    public void start(Stage primaryStage) {
        //TODO: create new overall theme with custom font
        //TODO: create landing page for when app is opened, click play button to get to game screen
        //TODO: create "get help" with popup explaining how to play when app is opened
        Label titleLabel = new Label("SOS Game");
            titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            titleLabel.setPrefHeight(50);

        // board setup
        board = new Board(8);
        console = new Console(board);
        grid = getGridPane();

        currentTurnLabel = new Label("Current Turn: Red");

        RadioButton simpleRadio = new RadioButton("Simple Game");
        RadioButton generalRadio = new RadioButton("General Game");
        ToggleGroup gameModeGroup = new ToggleGroup();
        simpleRadio.setToggleGroup(gameModeGroup);
        generalRadio.setToggleGroup(gameModeGroup);

        simpleRadio.setSelected(true);
        console.setGameMode(Console.GameMode.SIMPLE);
        gameMode = Console.GameMode.SIMPLE;

        // Listen for selection changes
        gameModeGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == simpleRadio) {
                gameMode = Console.GameMode.SIMPLE;
                console.setGameMode(Console.GameMode.SIMPLE);
            } else if (newToggle == generalRadio) {
                gameMode = Console.GameMode.GENERAL;
                console.setGameMode(Console.GameMode.GENERAL);
            }
        });

        // three main vertical panels
        VBox leftPanel = createBluePlayerPanel();
        centerPanel = createCenterPanel(titleLabel, grid, currentTurnLabel, simpleRadio, generalRadio);
        VBox rightPanel = createRedPlayerPanel(currentTurnLabel);

        // main horizontal layout
        root = new HBox(30, leftPanel, centerPanel, rightPanel);
            root.setAlignment(Pos.CENTER);
            root.setPadding(new Insets(20));
            root.setStyle("-fx-background-color: #f0f0f0;"); // TODO: add background color for initial current player

        // scene and stage
        Scene scene = new Scene(root);
        primaryStage.setTitle("SOS Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    /** Blue player panel **/
    private VBox createBluePlayerPanel() {
        VBox bluePanel = new VBox(15);
        bluePanel.setAlignment(Pos.CENTER);
        bluePanel.setPrefWidth(200);

        Label bluePlayerLabel = new Label("Blue player");
        bluePlayerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        RadioButton sRadio = new RadioButton("S");
        sRadio.setSelected(true);
        RadioButton oRadio = new RadioButton("O");

        blueGroup = new ToggleGroup();
        sRadio.setToggleGroup(blueGroup);
        oRadio.setToggleGroup(blueGroup);

        bluePanel.getChildren().addAll(bluePlayerLabel, sRadio, oRadio);
        return bluePanel;
    }

    /** center section with title, board, mode **/
    private VBox createCenterPanel(Label titleLabel, GridPane grid, Label currentTurnLabel, RadioButton radiobutton, RadioButton radiobutton2) {
        VBox centerPanel = new VBox(20, titleLabel, grid, currentTurnLabel, radiobutton, radiobutton2);
        centerPanel.setAlignment(Pos.CENTER);
        return centerPanel;
    }

    /** Red player panel with board size and control buttons **/
    private VBox createRedPlayerPanel(Label currentTurnLabel) {
        VBox redPanel = new VBox(15);
        redPanel.setAlignment(Pos.CENTER);
        redPanel.setPrefWidth(200);

        // top third section
        VBox topSection = new VBox(10);
        topSection.setAlignment(Pos.CENTER);
        HBox boardSizeBox = new HBox(5);
        boardSizeBox.setAlignment(Pos.CENTER);
        Label boardSizeLabel = new Label("Board size");
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

        redGroup = new ToggleGroup();
        sRadio.setToggleGroup(redGroup);
        oRadio.setToggleGroup(redGroup);
        middleSection.getChildren().addAll(redPlayerLabel, sRadio, oRadio);

        // lower third section
        VBox bottomSection = new VBox();
        bottomSection.setAlignment(Pos.CENTER);
        Button replayButton = new Button("Replay");
        Button newGameButton = getButton(boardSizeField);

        bottomSection.getChildren().addAll(replayButton, newGameButton);

        VBox.setVgrow(topSection, Priority.ALWAYS);
        VBox.setVgrow(middleSection, Priority.ALWAYS);
        VBox.setVgrow(bottomSection, Priority.ALWAYS);

        redPanel.getChildren().addAll(topSection, middleSection, bottomSection);
        return redPanel;
    }

    private static Button getButton(TextField boardSizeField) {
        Button newGameButton = new Button("New Game");

        newGameButton.setOnAction(e -> {
            try {
                int newSize;

                if (boardSizeField.getText().isEmpty()) {
                    newSize = 8;
                } else {
                    newSize = Integer.parseInt(boardSizeField.getText());
                }
                console.startNewGame(newSize, gameMode);

                board = new Board(newSize);
                grid = getGridPane();

                centerPanel.getChildren().set(1,grid);

            } catch (NumberFormatException ex) {
                showAlert("Invalid input", "Please enter a valid number between 3 and 10.");
            }
        });
        return newGameButton;
    }

    private static void handleCellClick(Label cell, int row, int col) {
        if (!board.isEmpty(row, col)) return; // if already filled, ignore

        // Get selected S or O from current player's radio buttons
        RadioButton selectedButton = (RadioButton) (
                currentPlayer.equals("Red") ? redGroup.getSelectedToggle() : blueGroup.getSelectedToggle()
                );

        if (selectedButton == null) {
            showAlert("Selection Error", "Please select S or O before making a move");
            return;
        }

        char letter = selectedButton.getText().charAt(0);
        boolean success = console.handleCellClick(row, col, letter);

        if (success) {
            cell.setText(String.valueOf(letter));

            // change background color based on player
            String color = currentPlayer.equals("Red") ? "#cce0ff": "#ffcccc";
            root.setStyle("-fx-background-color: " + color + ";");

            currentPlayer = console.getCurrentPlayer();
            currentTurnLabel.setText("Current Turn: " + currentPlayer);
        } else {
            showAlert("Invalid input", "Please enter a valid number between 3 and 10.");
        }
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

        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                Label cell = new Label(" ");
                cell.setMinSize(50, 50);
                cell.setAlignment(Pos.CENTER);
                cell.setStyle("-fx-border-color: black; -fx-background-color: white;");

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
