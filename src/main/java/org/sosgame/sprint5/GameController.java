package org.sosgame.sprint5;

public class GameController {

    private final Console console;
    private final GUI gui;

    public GameController(Console console, GUI gui) {
        this.console = console;
        this.gui = gui;
    }

    public void handleHumanMove(int row, int col, char letter) throws Exception {
        boolean success = console.handleCellClick(row, col, letter);

        if (!success) return;

        gui.updateMoveUI(row, col, letter);

        if (!console.isGameInProgress()) {
            gui.showEndGameScreen(console.getGame().getWinner());
            return;
        }

        runComputerTurnIfNeeded();
    }

    public void runComputerTurnIfNeeded() {
        if (!console.getGame().currentPlayerIsComputer()) return;

        gui.runComputerTurnWithDelay(() -> {
            Console.ComputerMove move = console.makeComputerMove();
            if (move == null) return;

            gui.updateMoveUI(move.row(), move.col(), move.letter());

            if (!console.isGameInProgress()) {
                gui.showEndGameScreen(console.getGame().getWinner());
                return;
            }

            runComputerTurnIfNeeded();
        });
    }
}

