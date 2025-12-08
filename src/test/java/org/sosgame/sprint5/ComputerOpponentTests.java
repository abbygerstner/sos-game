package org.sosgame.sprint5;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ComputerOpponentTests {
    // AC 8.1: Selecting opponent type creates correct player objects
    @Test
    void testBluePlayerIsComputerWhenSelected() {
        org.sosgame.sprint5.SOSGame game = new org.sosgame.sprint5.SimpleSOSGame(8, true, false); // blue=CPU, red=human
        assertInstanceOf(org.sosgame.sprint5.ComputerPlayer.class, game.bluePlayer, "Blue player should be a computer");
        assertInstanceOf(org.sosgame.sprint5.HumanPlayer.class, game.redPlayer, "Red player should be a human");
    }

    @Test
    void testRedPlayerIsComputerWhenSelected() {
        org.sosgame.sprint5.SOSGame game = new org.sosgame.sprint5.SimpleSOSGame(8, false, true); // blue=human, red=CPU
        assertInstanceOf(HumanPlayer.class, game.bluePlayer);
        assertInstanceOf(org.sosgame.sprint5.ComputerPlayer.class, game.redPlayer);
    }

    @Test
    void testBothPlayersCanBeComputer() {
        org.sosgame.sprint5.SOSGame game = new org.sosgame.sprint5.SimpleSOSGame(8, true, true);
        assertInstanceOf(org.sosgame.sprint5.ComputerPlayer.class, game.bluePlayer);
        assertInstanceOf(ComputerPlayer.class, game.redPlayer);
    }

    // AC 8.2: Computer takes automatic and valid turns on its turn
    @Test
    void testComputerMakesValidMove() {
        org.sosgame.sprint5.SOSGame game = new org.sosgame.sprint5.SimpleSOSGame(5, true, false); // blue = computer
        // Computer or blue goes first automatically
        org.sosgame.sprint5.SOSGame.Move move = game.getComputerMove();
        assertNotNull(move, "Computer should produce a move");

        boolean result = game.makeMove(move.row(), move.col(), move.letter());
        assertTrue(result, "Computer move should be accepted");

        boolean foundLetter = false;
        char[][] grid = game.getBoard().getGrid();
        for (int r = 0; r < game.getBoard().getSize(); r++) {
            for (int c = 0; c < game.getBoard().getSize(); c++) {
                if (grid[r][c] == 'S' || grid[r][c] == 'O') {
                    foundLetter = true;
                    break;
                }
            }
        }
        assertTrue(foundLetter, "Computer should have placed at least one letter");
    }

    @Test
    void testComputerTurnTriggersAutomaticMove() {
        org.sosgame.sprint5.SOSGame game = new org.sosgame.sprint5.SimpleSOSGame(5, false, true); // blue = human, red = computer
        boolean placed = game.makeMove(0, 0, 'S');
        assertTrue(placed, "Human should be able to place the first move");
        assertTrue(game.currentPlayerIsComputer(), "It should be the computer's turn");

        org.sosgame.sprint5.SOSGame.Move cpuMove = game.getComputerMove();
        assertNotNull(cpuMove, "Computer should choose a move");

        boolean cpuPlaced = game.makeMove(cpuMove.row(), cpuMove.col(), cpuMove.letter());
        assertTrue(cpuPlaced, "Computer move should succeed");

        // Check at least two filled cells (one human, one computer)
        int filledCount = 0;
        for (int r = 0; r < game.getBoard().getSize(); r++) {
            for (int c = 0; c < game.getBoard().getSize(); c++) {
                if (game.getBoard().getCell(r, c) == 'S' ||
                        game.getBoard().getCell(r, c) == 'O') {
                    filledCount++;
                }
            }
        }

        assertTrue(filledCount >= 2, "After human then CPU turns, at least two cells must be filled");
    }

    @Test
    void testComputerMoveIsOnEmptyCell() {
        org.sosgame.sprint5.SOSGame game = new SimpleSOSGame(5, true, false);

        SOSGame.Move move = game.getComputerMove();
        assertNotNull(move);

        assertTrue(game.getBoard().isEmpty(move.row(), move.col()),
                "Computer should only choose empty cells");
    }
}
