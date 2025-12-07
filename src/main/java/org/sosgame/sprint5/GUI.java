package org.sosgame.sprint5;

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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.sosgame.sprint5.GameReplayer;

import java.io.File;
import java.util.List;

public class GUI extends Application {
    private GridPane grid;
    private Console console;
    private ToggleGroup blueTypeGroup;
    private ToggleGroup redTypeGroup;
    private ToggleGroup blueLetterGroup;
    private ToggleGroup redLetterGroup;
    private Label currentTurnLabel;
    private Label scoreLabel;
    private HBox root;
    private VBox centerPanel;
    private Console.GameMode gameMode = Console.GameMode.SIMPLE;
    private Stage primaryStage;
    public static GUI instance;
    private int boardSize = 8;
    private RadioButton blueComputerRadio;
    private RadioButton redComputerRadio;
    private ToggleGroup gameModeGroup;
    private RadioButton simpleRadio;
    private RadioButton generalRadio;
    private TextField boardSizeField;

    @Override
    public void start(Stage primaryStage) {
        instance = this;
        this.primaryStage = primaryStage;
        console = new Console();
        showLandingScreen();
    }

    private void showLandingScreen() {
        Label title = buildTitle();

        VBox layout = new VBox(30,
                title,
                createGameModePanel(),
                createPlayersSection(),
                createBoardSizePanel(),
                createStartButton(),
                createReplayButton()
        );
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle(backgroundStyle());

        Scene scene = new Scene(layout, 800, 650);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Label buildTitle() {
        Label title = new Label("Welcome to SOS!");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");
        return title;
    }

    private VBox createGameModePanel() {
        Label modeLabel = new Label("Game Mode");
        modeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        simpleRadio = new RadioButton("Simple");
        generalRadio = new RadioButton("General");
        gameModeGroup = new ToggleGroup();
        simpleRadio.setToggleGroup(gameModeGroup);
        generalRadio.setToggleGroup(gameModeGroup);
        simpleRadio.setSelected(true);

        VBox gameModeBox = new VBox(10, modeLabel, simpleRadio, generalRadio);
        gameModeBox.setStyle(cardStyle());
        gameModeBox.setAlignment(Pos.CENTER_LEFT);

        return gameModeBox;
    }

    private VBox createSinglePlayerSelector(String color, ToggleGroup group) {
        Label label = new Label(color + " Player");
        label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        RadioButton human = new RadioButton("Human");
        RadioButton computer = new RadioButton("Computer");

        human.setToggleGroup(group);
        computer.setToggleGroup(group);
        human.setSelected(true);

        VBox box = new VBox(6, label, human, computer);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    private VBox createPlayersSection() {
        Label title = new Label("Player Settings");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        blueTypeGroup = new ToggleGroup();
        redTypeGroup  = new ToggleGroup();

        RadioButton blueHumanRadio = new RadioButton("Human");
        RadioButton blueCompRadio   = new RadioButton("Computer");
        blueHumanRadio.setToggleGroup(blueTypeGroup);
        blueCompRadio.setToggleGroup(blueTypeGroup);
        blueHumanRadio.setSelected(true);
        blueComputerRadio = blueCompRadio;
        VBox blueBoxExplicit = new VBox(6, new Label("Blue Player"), blueHumanRadio, blueComputerRadio);
        blueBoxExplicit.setAlignment(Pos.CENTER_LEFT);

        RadioButton redHumanRadio = new RadioButton("Human");
        RadioButton redCompRadio  = new RadioButton("Computer");
        redHumanRadio.setToggleGroup(redTypeGroup);
        redCompRadio.setToggleGroup(redTypeGroup);
        redHumanRadio.setSelected(true);
        redComputerRadio = redCompRadio;

        VBox redBoxExplicit = new VBox(6, new Label("Red Player"), redHumanRadio, redComputerRadio);
        redBoxExplicit.setAlignment(Pos.CENTER_LEFT);

        HBox row = new HBox(40, blueBoxExplicit, redBoxExplicit);
        row.setAlignment(Pos.CENTER);

        VBox wrapper = new VBox(10, title, row);
        wrapper.setStyle(cardStyle());
        wrapper.setAlignment(Pos.CENTER);
        return wrapper;
    }

    private VBox createBoardSizePanel() {
        Label sizeLabel = new Label("Board Size (3–10):");
        sizeLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        boardSizeField = new TextField("8");
        boardSizeField.setMaxWidth(60);

        VBox sizeBox = new VBox(5, sizeLabel, boardSizeField);
        sizeBox.setStyle(cardStyle());
        sizeBox.setAlignment(Pos.CENTER_LEFT);
        return sizeBox;
    }

    private Button createStartButton() {
        Button startButton = new Button("Start Game");
        startButton.setStyle("-fx-font-size: 16px; -fx-padding: 8 20;");
        startButton.setOnAction(e -> handleStartClick());
        return startButton;
    }

    private Button createReplayButton() {
        Button replayButton = new Button("Replay Saved Game");
        replayButton.setStyle("-fx-font-size: 16px; -fx-padding: 8 20;");
        replayButton.setOnAction(e -> loadReplay());
        return replayButton;
    }

    private void handleStartClick() {
        try {
            int size = Integer.parseInt(boardSizeField.getText());
            Board tempBoard = new Board(size);
            if (tempBoard.getErrorMessage() != null) {
                showAlert("Invalid size", tempBoard.getErrorMessage());
                return;
            }

            this.boardSize = size;

            boolean blueIsComputerSelected = blueComputerRadio != null && blueComputerRadio.isSelected();
            boolean redIsComputerSelected = redComputerRadio != null && redComputerRadio.isSelected();

            Console.GameMode selectedMode = simpleRadio.isSelected() ? Console.GameMode.SIMPLE : Console.GameMode.GENERAL;

            startNewGame(size, selectedMode, redIsComputerSelected, blueIsComputerSelected);

        } catch (NumberFormatException ex) {
            showAlert("Invalid input", "Please enter a valid number between 3 and 10.");
        }
    }

    private void startNewGame(int size, Console.GameMode mode, boolean redIsComputer, boolean blueIsComputer) {
        this.gameMode = mode;
        console.enableRecording(size, mode);
        console.initiateGame(size, mode, redIsComputer, blueIsComputer);
        showGameScreen();
    }

    private String backgroundStyle() {
        return "-fx-background-color: linear-gradient(to bottom, #f7f9f9, #d0e6df);";
    }

    private String cardStyle() {
        return "-fx-background-color: #ffffff;"
                + "-fx-padding: 15;"
                + "-fx-border-radius: 10;"
                + "-fx-background-radius: 10;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 10, 0.2, 0, 2);";
    }

    private void attachGameListeners(SOSGame game) {
        game.setListener(new GameEventListener() {
            @Override
            public void onSOSFormed(List<SOSGame.SOSSequence> sequences) {
                javafx.application.Platform.runLater(() -> {
                    for (SOSGame.SOSSequence seq : sequences) {
                        GUI.highlightSOS(seq);
                    }
                    if (game instanceof GeneralSOSGame g) {
                        scoreLabel.setText("Score — Blue: " + g.getBlueScore() + " | Red: " + g.getRedScore());
                    }
                });
            }

            @Override
            public void onGameOver(String winner) {
                javafx.application.Platform.runLater(() -> {
                    if ("Draw".equals(winner)) {
                        GUI.showTieScreenStatic();
                    } else {
                        GUI.showWinScreenStatic(winner);
                    }
                });
            }

            @Override
            public void onMoveMade(int row, int col, char letter, String playerColor) {
                javafx.application.Platform.runLater(() -> {
                    Label cell = getCellLabel(row, col);
                    if (cell != null) {
                        cell.setText(String.valueOf(letter));
                    }
                    currentTurnLabel.setText("Current Turn: " + game.getCurrentPlayer());
                });
            }
        });
    }

    public void showGameScreen() {
        SOSGame game = console.getGame();

        attachGameListeners(game);

        Board board = game.getBoard();

        Label titleLabel = buildGameTitle(game);
        grid = buildGameGrid(board);

        currentTurnLabel = buildTurnLabel(game);
        scoreLabel = buildScoreLabel();

        VBox leftPanel = createBluePlayerPanel();
        VBox rightPanel = createRedPlayerPanel();
        centerPanel = createCenterPanel(titleLabel, scoreLabel, grid, currentTurnLabel);

        Button newGameButton = new Button("New Game");
        newGameButton.setOnAction(e -> showLandingScreen());

        Button saveButton = new Button("Save Game");
        saveButton.setOnAction(e -> saveGameToFile());

        root = new HBox(30, leftPanel, centerPanel, rightPanel);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f0f0f0;");

        Scene gameScene = new Scene(root, 800, 650);
        primaryStage.setScene(gameScene);
        primaryStage.show();

        if (game.currentPlayerIsComputer()) {
            runComputerTurnWithDelay();
        }
    }

    private Label buildGameTitle(SOSGame game) {
        String titleText = (gameMode == Console.GameMode.SIMPLE) ? "Simple SOS Game" : "General SOS Game";
        Label titleLabel = new Label(titleText);
        titleLabel.setStyle("-fx-font-weight: bold;");
        titleLabel.setPrefHeight(50);
        return titleLabel;
    }

    private Label buildScoreLabel() {
        Label score = new Label("Score — Blue: 0 | Red: 0");
        score.setStyle("-fx-font-size: 14px;");
        return score;
    }

    private Label buildTurnLabel(SOSGame game) {
        Label turn = new Label("Current Turn: " + game.getCurrentPlayer());
        turn.setStyle("-fx-font-size: 14px;");
        return turn;
    }

    private GridPane buildGameGrid(Board board) {
        // reuse existing helper getGridPane(board) if it sets up cell labels & click handlers
        // otherwise ensure this method wires up click handlers to call console.makeMove(row,col,letter)
        return getGridPane(board);
    }

    private VBox createBluePlayerPanel() {
        VBox panel = new VBox(15);
        panel.setAlignment(Pos.CENTER);
        panel.setPrefWidth(200);

        Label label = new Label("Blue Player");
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        RadioButton sRadio = new RadioButton("S");
        RadioButton oRadio = new RadioButton("O");
        blueLetterGroup = new ToggleGroup();
        sRadio.setToggleGroup(blueLetterGroup);
        oRadio.setToggleGroup(blueLetterGroup);
        sRadio.setSelected(true);

        panel.getChildren().addAll(label, sRadio, oRadio);
        return panel;
    }

    private VBox createCenterPanel(Label title, Label scoreLabel, GridPane grid, Label turn) {
        VBox panel;
        if (gameMode == Console.GameMode.GENERAL) {
            panel = new VBox(20, title, scoreLabel, grid, turn);
        } else {
            panel = new VBox(20, title, grid, turn);
        }
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
        redLetterGroup = new ToggleGroup();
        sRadio.setToggleGroup(redLetterGroup);
        oRadio.setToggleGroup(redLetterGroup);
        sRadio.setSelected(true);

        Button newGameButton = new Button("New Game");
        newGameButton.setOnAction(e -> {
            showLandingScreen();
        });

        panel.getChildren().addAll(label, sRadio, oRadio, newGameButton);
        return panel;
    }

    private void handleCellClick(Label cell, int row, int col) throws Exception {
        if (console.isReplaying()) return;

        Board board = console.getBoard();
        if (board == null || !board.isEmpty(row, col)) return;

        RadioButton selectedButton = (RadioButton) (
                console.getCurrentPlayer().equals("Blue")
                        ? blueLetterGroup.getSelectedToggle()
                        : redLetterGroup.getSelectedToggle()
        );

        if (selectedButton == null) {
            showAlert("Selection Error", "Please select S or O before making a move.");
            return;
        }

        char letter = selectedButton.getText().charAt(0);
        boolean success = console.handleCellClick(row, col, letter);

        if (success) {
            cell.setText(String.valueOf(letter));

            currentTurnLabel.setText("Current Turn: " + console.getCurrentPlayer());

            if (!console.getGame().isGameInProgress()) {
                String winner = console.getGame().getWinner();
                if (winner != null) {
                    GUI.showWinScreenStatic(winner);
                }
            }

            if (console.getGame() instanceof GeneralSOSGame) {
                GeneralSOSGame game = console.getGeneralGame();
                int redScore = game.getRedScore();
                int blueScore = game.getBlueScore();
                scoreLabel.setText("Score — Blue: " + blueScore + " | Red: " + redScore);
            }

            if (console.getGame().currentPlayerIsComputer() &&
                    console.getGame().isGameInProgress()) {

                runComputerTurnWithDelay();
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
                cell.setOnMouseClicked(e -> {
                    try {
                        handleCellClick(cell, r, c);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });
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
            if (r != null && c != null && r == row && c == col) {
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

        Scene winScene = new Scene(layout, 800, 650);
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

        Scene tieScene = new Scene(layout, 800, 650);
        primaryStage.setScene(tieScene);
    }

    private void runComputerTurnWithDelay() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.5), event -> {
                    Console.ComputerMove cm = console.makeComputerMove();
                    if (cm == null) return;

                    Label cell = getCellLabel(cm.row(), cm.col());
                    if (cell != null) cell.setText(String.valueOf(cm.letter()));

                    currentTurnLabel.setText("Current Turn: " + console.getCurrentPlayer());

                    // Continue autoplay if next player is also a computer
                    if (console.getGame().currentPlayerIsComputer() &&
                            console.getGame().isGameInProgress()) {

                        runComputerTurnWithDelay();
                    }
                })
        );
        timeline.play();
    }

    private void saveGameToFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Game");
        chooser.setInitialFileName("sos-game.txt");

        File file = chooser.showSaveDialog(primaryStage);
        if (file == null) return;

        try {
            console.saveRecording(file);
            showAlert("Saved", "Game saved successfully!");
        } catch (Exception ex) {
            showAlert("Error", "Could not save file.");
        }
    }

    private void loadReplay() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Saved Game");

        File file = chooser.showOpenDialog(primaryStage);
        if (file == null) return;

        try {
            GameReplayer replayer = new GameReplayer(this, console);
            replayer.replayFromFile(file);
        } catch (Exception ex) {
            showAlert("Error", "Could not replay file");
        }
    }

}
