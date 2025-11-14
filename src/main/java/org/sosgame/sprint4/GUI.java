package org.sosgame.sprint4;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

public class GUI extends Application {
    private GridPane grid;
    private Console console;
    private ToggleGroup blueGroup;
    private ToggleGroup redGroup;
    private Label currentTurnLabel;
    private Label scoreLabel;
    private HBox root;
    private VBox centerPanel;
    private Console.GameMode gameMode = Console.GameMode.SIMPLE;
    private Stage primaryStage;
    public static GUI instance;

    private int boardSize = 8; // default

    @Override
    public void start(Stage primaryStage) {
        instance = this;
        this.primaryStage = primaryStage;
        console = new Console();
        showLandingScreen();
    }

    private void showLandingScreen() {
        Label title = new Label("Welcome to SOS!");
        title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

        // Game mode selection
        RadioButton simpleRadio = new RadioButton("Simple Game");
        RadioButton generalRadio = new RadioButton("General Game");
        ToggleGroup gameModeGroup = new ToggleGroup();
        simpleRadio.setToggleGroup(gameModeGroup);
        generalRadio.setToggleGroup(gameModeGroup);
        simpleRadio.setSelected(true);

        gameModeGroup.selectedToggleProperty().addListener((obs, old, selected) -> {
            gameMode = (selected == generalRadio) ? Console.GameMode.GENERAL : Console.GameMode.SIMPLE;
        });

        // Board size input
        Label sizeLabel = new Label("Board Size (3–10):");
        TextField sizeField = new TextField("8");
        sizeField.setMaxWidth(50);

        // Start button
        Button startButton = new Button("Start Game");
        startButton.setOnAction(e -> {
            try {
                int size = Integer.parseInt(sizeField.getText());
                Board tempBoard = new Board(size);
                if (tempBoard.getErrorMessage() != null) {
                    showAlert("Invalid size", tempBoard.getErrorMessage());
                    return;
                }

                this.boardSize = size;
                console.initiateGame(boardSize, gameMode);
                showGameScreen();

            } catch (NumberFormatException ex) {
                showAlert("Invalid input", "Please enter a valid number between 3 and 10.");
            }
        });

        VBox layout = new VBox(15, title, simpleRadio, generalRadio, sizeLabel, sizeField, startButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #f7f9f9, #d0e6df);");
        layout.setPadding(new Insets(40));

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("SOS Game");
        primaryStage.show();
    }

    private void showGameScreen() {
        if (console.getGame() == null) {
            if (gameMode == Console.GameMode.SIMPLE) {
                console.setGame(new SimpleSOSGame(boardSize));
            } else {
                console.setGame(new GeneralSOSGame(boardSize));
            }
        }

        SOSGame game = console.getGame();
        game.setListener(new GameEventListener() {
            @Override
            public void onSOSFormed(List<SOSGame.SOSSequence> sequences) {
                // Highlight on the board
                javafx.application.Platform.runLater(() -> {
                    for (SOSGame.SOSSequence seq : sequences) {
                        GUI.highlightSOS(seq);
                    }

                    // Update score if General game
                    if (game instanceof GeneralSOSGame g) {
                        scoreLabel.setText("Score — Red: " + g.getRedScore() + " | Blue: " + g.getBlueScore());
                    }
                });
            }

            @Override
            public void onGameOver(String winner) {
                javafx.application.Platform.runLater(() -> {
                    if ("Draw".equals(winner)) {
                        GUI.showTieScreenStatic(); // separate tie screen
                    } else {
                        GUI.showWinScreenStatic(winner);
                    }
                });
            }
        });

        Board board = game.getBoard();

        // Dynamic title based on mode
        String titleText = (gameMode == Console.GameMode.SIMPLE)
                ? "Simple SOS Game"
                : "General SOS Game";

        Label titleLabel = new Label(titleText);
        titleLabel.setStyle("-fx-font-weight: bold;");
        titleLabel.setPrefHeight(50);

        grid = getGridPane(board);

        currentTurnLabel = new Label("Current Turn: " + game.getCurrentPlayer());

        scoreLabel = new Label();
        scoreLabel.setText("Score — Red: 0 | Blue: 0");

        // Radio buttons to switch mode (if desired mid-game)
//        RadioButton simpleRadio = new RadioButton("Simple Game");
//        RadioButton generalRadio = new RadioButton("General Game");
//        ToggleGroup modeGroup = new ToggleGroup();
//        simpleRadio.setToggleGroup(modeGroup);
//        generalRadio.setToggleGroup(modeGroup);
//        simpleRadio.setSelected(gameMode == Console.GameMode.SIMPLE);
//        generalRadio.setSelected(gameMode == Console.GameMode.GENERAL);

        VBox leftPanel = createBluePlayerPanel();
        centerPanel = createCenterPanel(titleLabel, scoreLabel, grid, currentTurnLabel);
        VBox rightPanel = createRedPlayerPanel();

        Button newGameButton = new Button("New Game");
        newGameButton.setOnAction(e -> showLandingScreen());

        root = new HBox(30, leftPanel, centerPanel, rightPanel);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f0f0f0;");

        Scene gameScene = new Scene(root);
        primaryStage.setScene(gameScene);
        primaryStage.show();
    }

    private VBox createBluePlayerPanel() {
        VBox panel = new VBox(15);
        panel.setAlignment(Pos.CENTER);
        panel.setPrefWidth(200);

        Label label = new Label("Blue Player");
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        RadioButton sRadio = new RadioButton("S");
        RadioButton oRadio = new RadioButton("O");
        blueGroup = new ToggleGroup();
        sRadio.setToggleGroup(blueGroup);
        oRadio.setToggleGroup(blueGroup);
        sRadio.setSelected(true);

        panel.getChildren().addAll(label, sRadio, oRadio);
        return panel;
    }

    private VBox createCenterPanel(Label title, Label scoreLabel, GridPane grid, Label turn) {
        VBox panel = new VBox(20, title, grid, turn);
        panel.setAlignment(Pos.CENTER);
        return panel;
    }

    private VBox createRedPlayerPanel() {
        VBox panel = new VBox(15);
        panel.setAlignment(Pos.CENTER);
        panel.setPrefWidth(200);

        Label label = new Label("Red Player");
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        RadioButton sRadio = new RadioButton("S");
        RadioButton oRadio = new RadioButton("O");
        redGroup = new ToggleGroup();
        sRadio.setToggleGroup(redGroup);
        oRadio.setToggleGroup(redGroup);
        sRadio.setSelected(true);

        Button newGameButton = new Button("New Game");
        newGameButton.setOnAction(e -> {
            showLandingScreen();
        });

        panel.getChildren().addAll(label, sRadio, oRadio, newGameButton);
        return panel;
    }

    private void handleCellClick(Label cell, int row, int col) {
        Board board = console.getBoard();
        if (board == null || !board.isEmpty(row, col)) return;

        RadioButton selectedButton = (RadioButton) (
                console.getCurrentPlayer().equals("Red") ? redGroup.getSelectedToggle() : blueGroup.getSelectedToggle()
        );

        if (selectedButton == null) {
            showAlert("Selection Error", "Please select S or O before making a move.");
            return;
        }

        char letter = selectedButton.getText().charAt(0);
        boolean success = console.handleCellClick(row, col, letter);

        if (success) {
            cell.setText(String.valueOf(letter));

            // Update board background color
            String color = console.getCurrentPlayer().equals("Red") ? "#ffcccc" : "#cce0ff";
            root.setStyle("-fx-background-color: " + color + ";");

            // Update turn label
            currentTurnLabel.setText("Current Turn: " + console.getCurrentPlayer());

            // If the game has ended in simple mode
            if (!console.getGame().isGameInProgress()) {
                String winner = console.getGame().getWinner();
                if (winner != null) {
                    GUI.showWinScreenStatic(winner);
                }
            }

            // If general mode, also update score
            if (console.getGame() instanceof GeneralSOSGame g) {
//                System.out.println("Red: " + g.getRedScore() + " | Blue: " + g.getBlueScore());
                GeneralSOSGame game = console.getGeneralGame();
                int redScore = game.getRedScore();
                int blueScore = game.getBlueScore();
                scoreLabel.setText("Score — Red: " + redScore + " | Blue: " + blueScore);
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private GridPane getGridPane(Board board) {
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

                int r = row;
                int c = col;
                cell.setOnMouseClicked(e -> handleCellClick(cell, r, c));
                grid.add(cell, col, row);
            }
        }
        return grid;
    }

    public static void highlightSOS(SOSGame.SOSSequence seq) {
        Label cell1 = instance.getCellLabel(seq.row1(), seq.col1());
        Label cell2 = instance.getCellLabel(seq.row2(), seq.col2());
        Label cell3 = instance.getCellLabel(seq.row3(), seq.col3());

        String color = seq.player().equals("Red") ? "#ff4c4c" : "#4c6eff";
        String style = "-fx-border-color: " + color + "; -fx-border-width: 3px; -fx-font-weight: bold;";
        cell1.setStyle(style);
        cell2.setStyle(style);
        cell3.setStyle(style);
    }

    private Label getCellLabel(int row, int col) {
        for (var node : grid.getChildren()) {
            Integer r = GridPane.getRowIndex(node);
            Integer c = GridPane.getColumnIndex(node);
            if ((r == null ? 0 : r) == row && (c == null ? 0 : c) == col) {
                return (Label) node;
            }
        }
        return null;
    }

    public static void showWinScreenStatic(String winner) {
        if (instance != null) instance.showWinScreen(winner);
    }

    private void showWinScreen(String winner) {
        Label winLabel = new Label(winner + " Wins!");
        winLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold;");

        Button playAgain = new Button("Play Again");
        playAgain.setOnAction(e -> showLandingScreen());

        VBox layout = new VBox(30, winLabel, playAgain);
        layout.setAlignment(Pos.CENTER);

        Timeline disco = new Timeline(
                new KeyFrame(Duration.seconds(0.3), ev -> {
                    String color = String.format("#%06x", (int)(Math.random() * 0xffffff));
                    layout.setStyle("-fx-background-color: " + color + ";");
                })
        );
        disco.setCycleCount(Animation.INDEFINITE);
        disco.play();

        Scene winScene = new Scene(layout, 800, 600);
        primaryStage.setScene(winScene);
    }

    public static void showTieScreenStatic() {
        if (instance != null) instance.showTieScreen();
    }

    private void showTieScreen() {
        Label tieLabel = new Label("It's a Tie!");
        tieLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold;");

        Button playAgain = new Button("Play Again");
        playAgain.setOnAction(e -> showLandingScreen());

        VBox layout = new VBox(30, tieLabel, playAgain);
        layout.setAlignment(Pos.CENTER);

        Scene tieScene = new Scene(layout, 800, 600);
        primaryStage.setScene(tieScene);
    }

}
