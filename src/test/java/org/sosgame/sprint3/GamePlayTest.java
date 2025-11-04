package org.sosgame.sprint3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sosgame.sprint2.Board;
import org.sosgame.sprint2.Console;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/** Extra unit tests for gameplay */
public class GamePlayTest {
    private org.sosgame.sprint2.Board board;
    private org.sosgame.sprint2.Console console;

    @BeforeEach
    void setUp() {
        board = new Board(3);
        console = new org.sosgame.sprint2.Console(board);
    }

    /** After a valid move turn switches **/
    @Test
    void testTurnSwitchesAfterMove() {
        String initialPlayer = console.getCurrentPlayer();
        console.handleCellClick(0, 0, 'S');
        String nextPlayer = console.getCurrentPlayer();
        assertNotEquals(initialPlayer, nextPlayer, "Player turn should switch after valid move");
    }

    /** Game mode selection updates correctly **/
    @Test
    void testGameModeSelection() {
        console.setGameMode(org.sosgame.sprint2.Console.GameMode.SIMPLE);
        assertEquals(org.sosgame.sprint2.Console.GameMode.SIMPLE, console.getGameMode(), "Game mode should be SIMPLE when set");

        console.setGameMode(org.sosgame.sprint2.Console.GameMode.GENERAL);
        assertEquals(Console.GameMode.GENERAL, console.getGameMode(), "Game mode should be GENERAL when set");
    }
}
