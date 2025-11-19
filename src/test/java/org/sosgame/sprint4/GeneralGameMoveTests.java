package org.sosgame.sprint4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sosgame.sprint2.Board;
import org.sosgame.sprint2.Console;

import static org.junit.jupiter.api.Assertions.*;

/** AC 6.1-6.2 */
public class GeneralGameMoveTests {
    private Console console;
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(3);
        console = new Console(board);
        console.setGameMode(Console.GameMode.GENERAL);
    }

    // AC 6.1: Player makes legal move in general game
    @Test
    void testLegalMove_SymbolAppearsOnBoard() {
        boolean success = console.handleCellClick(1, 1, 'S');
        assertTrue(success, "Valid move should return true");

        char cellValue = board.getCell(1, 1);
        assertEquals('S', cellValue, "Cell should contain the symbol placed by the player");
    }

    // AC 6.2: Player makes illegal move on occupied cell in general game
    @Test
    void testIllegalMove_OnOccupiedCell_ShouldRejectMove() {
        console.handleCellClick(0, 0, 'S'); // first move, valid
        boolean secondMove = console.handleCellClick(0, 0, 'O'); // same spot

        assertFalse(secondMove, "Move on occupied cell should be rejected");
        assertEquals('S', board.getCell(0, 0), "Cell should remain unchanged after illegal move");
    }

}
