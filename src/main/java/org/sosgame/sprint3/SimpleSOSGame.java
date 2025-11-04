package org.sosgame.sprint3;

public class SimpleSOSGame extends SOSGame {

    public SimpleSOSGame(int size) {
        super(size);
    }

    @Override
    protected boolean checkWinner(int row, int col) {
        SOSSequence seq = formsSOS(row, col);
        // If current move creates any "SOS", game ends
        if (seq != null) {
            gameInProgress = false;
            winner = currentPlayer;
            if (listener != null) {
                listener.onGameOver(winner);
            }
            return true;
        }
        // Otherwise, check if the board is full -> tie
        if (board.isBoardFull()) {
            gameInProgress = false;
            winner = "Draw"; // No winner, tie
            if (listener != null) {
                listener.onGameOver(winner);
            }
            return true;
        }
        return false;
    }

    private SOSSequence formsSOS(int row, int col) {
        char[][] g = board.getGrid();
        int n = board.getSize();
//        System.out.println("formsSOS checking at (" + row + "," + col + "), placed=" + g[row][col]);

        char placed = Character.toUpperCase(g[row][col]);

        // All directions that SOS can be placed
        int[][] dirs = {{0,1}, {1,0}, {1,1}, {1,-1}};

        for (int[] d : dirs) {
            int dr = d[0], dc = d[1];

            // Case 1: middle O is placed
            int aR1 = row - dr, aC1 = col - dc;
            int aR3 = row + dr, aC3 = col + dc;
            if (inBounds(aR1, aC1, n) && inBounds(aR3, aC3, n)) {
                if (Character.toUpperCase(g[aR1][aC1]) == 'S' &&
                        placed == 'O' &&
                        Character.toUpperCase(g[aR3][aC3]) == 'S') {
                    return new SOSSequence(aR1, aC1, row, col, aR3, aC3, currentPlayer);
                }
            }

            // Case 2: placed is the first S
            int bR1 = row + dr, bC1 = col + dc;
            int bR2 = row + 2*dr, bC2 = col + 2*dc;
            if (inBounds(bR1, bC1, n) && inBounds(bR2, bC2, n)) {
                if (placed == 'S' &&
                        Character.toUpperCase(g[bR1][bC1]) == 'O' &&
                        Character.toUpperCase(g[bR2][bC2]) == 'S') {
                    return new SOSSequence(row, col, bR1, bC1, bR2, bC2, currentPlayer);
                }
            }

            // Case 3: placed is the last S
            int cR1 = row - 2*dr, cC1 = col - 2*dc;
            int cR2 = row - dr,  cC2 = col - dc;
            if (inBounds(cR1, cC1, n) && inBounds(cR2, cC2, n)) {
                if (Character.toUpperCase(g[cR1][cC1]) == 'S' &&
                        Character.toUpperCase(g[cR2][cC2]) == 'O' &&
                        placed == 'S') {
                    return new SOSSequence(cR1, cC1, cR2, cC2, row, col, currentPlayer);
                }
            }
        }
        return null;
    }

}
