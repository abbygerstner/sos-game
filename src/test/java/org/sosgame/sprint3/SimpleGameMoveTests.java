package org.sosgame.sprint3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sosgame.sprint2.Board;
import org.sosgame.sprint2.Console;

import static org.junit.jupiter.api.Assertions.*;

/** AC 4.1-4.2 */
public class SimpleGameMoveTests {
    private org.sosgame.sprint2.Console console;
    private org.sosgame.sprint2.Board board;

    @BeforeEach
    void setUp() {
        board = new Board(3);
        console = new org.sosgame.sprint2.Console(board);
        console.setGameMode(Console.GameMode.SIMPLE);
    }

    // AC 4.1: Player makes legal move in simple game
    @Test
    void testLegalMove_SymbolAppearsOnBoard() {
        boolean success = console.handleCellClick(1, 1, 'S');
        assertTrue(success, "Valid move should return true");

        char cellValue = board.getCell(1, 1);
        assertEquals('S', cellValue, "Cell should contain the symbol placed by the player");
    }

    // AC 4.2: Player makes illegal move on occupied cell in simple game
    @Test
    void testIllegalMove_OnOccupiedCell_ShouldRejectMove() {
        console.handleCellClick(0, 0, 'S'); // first move, valid
        boolean secondMove = console.handleCellClick(0, 0, 'O'); // same spot

        assertFalse(secondMove, "Move on occupied cell should be rejected");
        assertEquals('S', board.getCell(0, 0), "Cell should remain unchanged after illegal move");
    }

}
