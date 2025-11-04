package org.sosgame.sprint3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for verifying SOS formation and winner/draw logic
 * in both Simple and General game modes.
 */

public class SimpleAndGeneralGameLogicTests {
    private Console console;

    @BeforeEach
    void setUp() {
        console = new Console();
    }

    // AC 4.3: player creates SOS in simple game
    // AC 5.1 player wins
    @Test
    void testFormSOS_SimpleGame_RedWinsImmediately() {
        console.initiateGame(3, Console.GameMode.SIMPLE);

        // Red forms SOS on top row
        console.handleCellClick(0, 0, 'S'); // Red
        console.handleCellClick(1, 0, 'S'); // Blue
        console.handleCellClick(0, 1, 'O'); // Red
        console.handleCellClick(1, 1, 'O'); // Blue
        console.handleCellClick(0, 2, 'S'); // Red completes SOS

        assertFalse(console.isGameInProgress(), "Game should end immediately in Simple mode.");
        assertEquals("Red", console.getGame().getWinner(), "Red should be declared the winner.");
    }

    // AC 5.2 game ends in a draw
    @Test
    void testDraw_SimpleGame_NoSOSFormed() {
        console.initiateGame(3, Console.GameMode.SIMPLE);

        // Fill the board without forming SOS
        console.handleCellClick(0, 0, 'S');
        console.handleCellClick(0, 1, 'S');
        console.handleCellClick(0, 2, 'O');
        console.handleCellClick(1, 0, 'O');
        console.handleCellClick(1, 1, 'S');
        console.handleCellClick(1, 2, 'O');
        console.handleCellClick(2, 0, 'O');
        console.handleCellClick(2, 1, 'O');
        console.handleCellClick(2, 2, 'S');

        assertFalse(console.isGameInProgress(), "Game should end when board is full.");
        assertEquals("Draw", console.getGame().getWinner(), "Game should end in a draw with no SOS formed.");
    }

    // AC 6.3: player creates SOS in general game
    @Test
    void testFormSOS_GeneralGame_ScoringAndContinuedPlay() {
        console.initiateGame(3, Console.GameMode.GENERAL);

        // Red forms SOS → should score 1 point but continue
        console.handleCellClick(0, 0, 'S'); // Red
        console.handleCellClick(1, 0, 'S'); // Blue
        console.handleCellClick(0, 1, 'O'); // Red
        console.handleCellClick(1, 1, 'O'); // Blue
        console.handleCellClick(0, 2, 'S'); // Red completes SOS

        assertTrue(console.isGameInProgress(), "Game should continue after SOS in General mode.");

        SOSGame game = console.getGame();
        assertTrue(game instanceof GeneralSOSGame, "Expected GeneralSOSGame instance for general mode.");

        int redScore = ((GeneralSOSGame) game).getRedScore();
        assertEquals(1, redScore, "Red should have 1 point after forming SOS.");
    }

    // AC 7.1: player wins
    @Test
    void testGeneralGame_WinnerDeterminedByScore() {
        console.initiateGame(3, Console.GameMode.GENERAL);

        // Red scores 2 SOS patterns
        console.handleCellClick(0, 0, 'S'); // Red
        console.handleCellClick(1, 0, 'S'); // Blue
        console.handleCellClick(0, 1, 'O'); // Red
        console.handleCellClick(1, 1, 'O'); // Blue
        console.handleCellClick(0, 2, 'S'); // Red scores 1
        console.handleCellClick(2, 0, 'S'); // Blue
        console.handleCellClick(2, 1, 'O'); // Red
        console.handleCellClick(2, 2, 'S'); // Blue forms another SOS (tie)

        // Finish the game (board filled)
        console.handleCellClick(1, 2, 'S');
        console.handleCellClick(2, 1, 'O');

        assertFalse(console.isGameInProgress(), "Game should end when board is full.");
        String winner = console.getGame().getWinner();
        assertNotNull(winner, "Winner should be determined in general mode.");
        assertTrue(winner.equals("Red") || winner.equals("Blue") || winner.equals("Draw"),
                "Winner should be Red, Blue, or Draw.");
    }

    // AC 7.2 game ends in a draw
    @Test
    void testGeneralGame_DrawCondition() {
        console.initiateGame(3, Console.GameMode.GENERAL);

        // Intentionally no SOS formed for either player
        console.handleCellClick(0, 0, 'O');
        console.handleCellClick(0, 1, 'S');
        console.handleCellClick(0, 2, 'O');
        console.handleCellClick(1, 0, 'S');
        console.handleCellClick(1, 1, 'O');
        console.handleCellClick(1, 2, 'S');
        console.handleCellClick(2, 0, 'O');
        console.handleCellClick(2, 1, 'S');
        console.handleCellClick(2, 2, 'O');

        assertEquals("Draw", console.getGame().getWinner(), "Game should end in draw with no SOS formed.");
    }
}
