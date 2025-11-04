package org.sosgame.sprint3;

import java.util.ArrayList;
import java.util.List;

public class GeneralSOSGame extends SOSGame {
    private int redScore = 0;
    private int blueScore = 0;

    public GeneralSOSGame(int size) {
        super(size);
    }

    @Override
    protected boolean checkWinner(int row, int col) {
        List<SOSSequence> sequences = countSOS(row, col);

        if (!sequences.isEmpty()) {
            // Update scores
            if (currentPlayer.equals("Red")) redScore += sequences.size();
            else blueScore += sequences.size();

            // Notify listener to highlight SOS
            if (listener != null) {
                listener.onSOSFormed(sequences);
            }
        }

        // End game if full
        if (board.isBoardFull()) {
            gameInProgress = false;
            winner = declareWinner();

            if (listener != null) {
                listener.onGameOver(winner);
            }
        }

        // If player scored, they get another turn
        return !sequences.isEmpty();
    }

    private List<SOSSequence> countSOS(int row, int col) {
        List<SOSSequence> list = new ArrayList<>();
        char[][] g = board.getGrid();
        int n = board.getSize();
        // All directions that SOS can be placed
        int[][] dirs = {{0,1}, {1,0}, {1,1}, {1,-1}};

        char placed = Character.toUpperCase(g[row][col]);

        for (int[] d : dirs) {
            int dr = d[0], dc = d[1];

            // Case 1: Middle O case
            int mR1 = row - dr, mC1 = col - dc;
            int mR3 = row + dr, mC3 = col + dc;
            if (inBounds(mR1,mC1,n) && inBounds(mR3,mC3,n)) {
                if (Character.toUpperCase(g[mR1][mC1]) == 'S' &&
                        placed == 'O' &&
                        Character.toUpperCase(g[mR3][mC3]) == 'S') {
                    list.add(new SOSSequence(mR1, mC1, row, col, mR3, mC3, currentPlayer));
                }
            }

            // Case 2: placed as first S
            int fR1 = row + dr, fC1 = col + dc;
            int fR2 = row + 2*dr, fC2 = col + 2*dc;
            if (inBounds(fR1,fC1,n) && inBounds(fR2,fC2,n)) {
                if (placed == 'S' &&
                        Character.toUpperCase(g[fR1][fC1]) == 'O' &&
                        Character.toUpperCase(g[fR2][fC2]) == 'S') {
                    list.add(new SOSSequence(row, col, fR1, fC1, fR2, fC2, currentPlayer));
                }
            }

            // Case 3: placed as last S: two behind, one behind, placed
            int lR1 = row - 2*dr, lC1 = col - 2*dc;
            int lR2 = row - dr,  lC2 = col - dc;
            if (inBounds(lR1,lC1,n) && inBounds(lR2,lC2,n)) {
                if (Character.toUpperCase(g[lR1][lC1]) == 'S' &&
                        Character.toUpperCase(g[lR2][lC2]) == 'O' &&
                        placed == 'S') {
                    list.add(new SOSSequence(lR1, lC1, lR2, lC2, row, col, currentPlayer));
                }
            }
        }
        return list;
    }

    private String declareWinner() {
        if (redScore > blueScore) return "Red";
        else if (blueScore > redScore) return "Blue";
        else winner = "Draw";
        return winner;
    }

    public int getRedScore() { return redScore; }
    public int getBlueScore() { return blueScore; }
}
