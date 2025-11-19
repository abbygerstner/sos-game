package org.sosgame.sprint4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sosgame.sprint2.Board;
import org.sosgame.sprint2.Console;

import static org.junit.jupiter.api.Assertions.*;

/** Unit tests for Acceptance Criteria 1.1, 1.2, 2.1, 2.2 */

public class NewGameTest {
    private Board board;
    private Console console;
    @BeforeEach
    public void setUp() {
        board = new Board(5);
        console = new Console(board);
    }

    // AC 1.1: Player selects valid board size
    @Test
    void testValidBoardSizeSelection() {
        // Given: the player is on the home screen (board initialized)
        int validSize = 8;

        // When: player selects a valid board size between 3 and 10
        boolean result = board.setBoardSize(validSize);

        // Then: the board updates to the chosen valid size
        assertTrue(result, "Expected setBoardSize() to return true for a valid size.");
        assertEquals(validSize, board.getSize(),
                "Board size should be updated to the valid value selected by the player.");
        assertNull(board.getErrorMessage(),
                "No error message should be shown for a valid board size.");
    }

    // AC 1.2: Player selects invalid board size
    @Test
    void testInvalidBoardSizeTooSmall() {
        // Given
        int invalidSize = 2;

        // When
        boolean result = board.setBoardSize(invalidSize);

        // Then
        assertFalse(result, "Expected setBoardSize() to return false for an invalid small size.");
        assertEquals("Size must be between 3 and 10", board.getErrorMessage(),
                "Should display 'Size must be between 3 and 10' for an invalid small size.");
        assertNotEquals(invalidSize, board.getSize(),
                "Board size should not be updated to an invalid value.");
    }

    // AC 1.2: Player selects invalid board size
    @Test
    void testInvalidBoardSizeTooLarge() {
        int invalidSize = 11;
        boolean result = board.setBoardSize(invalidSize);
        assertFalse(result, "Expected setBoardSize() to return false for an invalid large size.");
        assertEquals("Size must be between 3 and 10", board.getErrorMessage(),
                "Should display 'Size must be between 3 and 10' for an invalid large size.");
        assertNotEquals(invalidSize, board.getSize(),
                "Board size should not be updated to an invalid value.");
    }

    // AC 1.2
    @Test
    void testEdgeCasesBoundaryValues() {
        assertTrue(board.setBoardSize(3), "Board size of 3 should be valid.");
        assertEquals(3, board.getSize());
        assertTrue(board.setBoardSize(10), "Board size of 10 should be valid.");
        assertEquals(10, board.getSize());
    }

    // AC 2.1: Player selects simple game
    @Test
    void testSelectSimpleGameMode() {
        console.setGameMode(Console.GameMode.SIMPLE);
        assertEquals(Console.GameMode.SIMPLE, console.getGameMode());
    }

    // AC 2.2: Player selects general game
    @Test
    void testSelectGeneralGameMode() {
        console.setGameMode(Console.GameMode.GENERAL);
        assertEquals(Console.GameMode.GENERAL, console.getGameMode());
    }

}
