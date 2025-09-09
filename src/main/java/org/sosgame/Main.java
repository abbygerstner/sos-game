package org.sosgame;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        //TODO: create new overall theme with custom font
        //TODO: create landing page for when app is opened, click play button to get to game screen
        //TODO: create "get help" with popup explaining how to play when app is opened
        Label titleLabel = new Label("SOS Game");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        titleLabel.setPrefHeight(50);

        // sos game grid
        GridPane grid = getGridPane();

        Label currentTurnLabel = new Label("Current Turn: blue (or red)");

        RadioButton radioButton = new RadioButton("Simple Game");
        RadioButton radioButton2 = new RadioButton("General Game");

        // three main vertical panels
        VBox leftPanel = createBluePlayerPanel();
        VBox centerPanel = createCenterPanel(titleLabel, grid, currentTurnLabel, radioButton, radioButton2);
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

        // TODO: Only allow one radio button to be selected at a time
        RadioButton humanRadio = new RadioButton("Human");
        humanRadio.setSelected(true);
        RadioButton computerRadio = new RadioButton("Computer");
        // TODO: create GUI feature to input S or O

        bluePanel.getChildren().addAll(bluePlayerLabel, humanRadio, computerRadio);
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
        // TODO: Only allow one radio button to be selected at a time
        RadioButton humanRadio = new RadioButton("Human");
        RadioButton computerRadio = new RadioButton("Computer");
        // TODO: create GUI feature to input S or O
        computerRadio.setSelected(true);
        middleSection.getChildren().addAll(redPlayerLabel, humanRadio, computerRadio);

        // lower third section
        VBox bottomSection = new VBox();
        bottomSection.setAlignment(Pos.CENTER);
        Button replayButton = new Button("Replay");
        Button newGameButton = new Button("New Game");
        bottomSection.getChildren().addAll(replayButton, newGameButton);

        VBox.setVgrow(topSection, Priority.ALWAYS);
        VBox.setVgrow(middleSection, Priority.ALWAYS);
        VBox.setVgrow(bottomSection, Priority.ALWAYS);

        redPanel.getChildren().addAll(topSection, middleSection, bottomSection);
        return redPanel;
    }

    private static GridPane getGridPane() {
        GridPane grid = new GridPane();
        grid.setGridLinesVisible(true);
        grid.setHgap(2);
        grid.setVgap(2);
        grid.setAlignment(Pos.CENTER);

        // TODO: Edit for loop to take input of boardSizeField textfield and create board
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Label cell = new Label(" ");
                cell.setMinSize(50, 50);
                cell.setStyle("-fx-border-color: black; -fx-background-color: white;");
                grid.add(cell, col, row);
            }
        }
        return grid;
    }

    public static void main(String[] args) {
        launch(args);
    }
}