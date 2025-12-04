package org.sosgame.sprint5;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sosgame.sprint2.Board;
import org.sosgame.sprint2.Console;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StartGameTest {

    private Console console;

    @BeforeEach
    void setUp() {
        console = new Console(new Board(8)); // start with default 8x8
    }

    /** AC 3.1: Valid new game (no game in progress) **/
    @Test
    void testValidNewGameWithoutGameInProgress() {
        console.startNewGame(5, Console.GameMode.SIMPLE);

        assertEquals(5, console.getBoard().getSize(), "Board should reset to the new chosen size");
        assertEquals(Console.GameMode.SIMPLE, console.getGameMode(), "Game mode should be set to SIMPLE");
        assertTrue(console.isGameInProgress(), "Game should be marked as in progress");
        assertEquals("Red", console.getCurrentPlayer(), "New game should start with Red player");
    }

    /** AC 3.2: Valid new game while a game is already in progress **/
    @Test
    void testValidNewGameWithGameInProgress() {
        // Start an initial game
        console.startNewGame(6, Console.GameMode.SIMPLE);
        assertTrue(console.isGameInProgress());

        // Start another game with new settings
        console.startNewGame(9, Console.GameMode.GENERAL);

        assertEquals(9, console.getBoard().getSize(), "Board should reset to new size even if a game was in progress");
        assertEquals(Console.GameMode.GENERAL, console.getGameMode(), "Game mode should update to GENERAL");
        assertTrue(console.isGameInProgress(), "Game should remain in progress after reset");
    }

    /** AC 3.3: Default game mode is SIMPLE when none selected **/
    @Test
    void testDefaultGameModeWhenNoneSelected() {
        console.startNewGame(7, null);

        assertEquals(Console.GameMode.SIMPLE, console.getGameMode(),
                "Default game mode should be SIMPLE when none is selected");
    }

    /** AC 3.4: Default board size is 8x8 when none selected **/
    @Test
    void testDefaultBoardSizeWhenAppOpens() {
        console.startNewGame(8, Console.GameMode.GENERAL);

        assertEquals(8, console.getBoard().getSize(),
                "Default board size should be 8x8 when none is selected");
        assertEquals(Console.GameMode.GENERAL, console.getGameMode(),
                "Game mode should still use selected mode if provided");
    }
}
