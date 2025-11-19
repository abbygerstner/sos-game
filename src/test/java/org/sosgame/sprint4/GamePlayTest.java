package org.sosgame.sprint4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sosgame.sprint2.Board;
import org.sosgame.sprint2.Console;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/** Extra unit tests for gameplay */
public class GamePlayTest {
    private Board board;
    private Console console;

    @BeforeEach
    void setUp() {
        board = new Board(3);
        console = new Console(board);
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
        console.setGameMode(Console.GameMode.SIMPLE);
        assertEquals(Console.GameMode.SIMPLE, console.getGameMode(), "Game mode should be SIMPLE when set");

        console.setGameMode(Console.GameMode.GENERAL);
        assertEquals(Console.GameMode.GENERAL, console.getGameMode(), "Game mode should be GENERAL when set");
    }
}
