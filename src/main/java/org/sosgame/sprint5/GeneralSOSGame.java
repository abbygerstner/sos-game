package org.sosgame.sprint5;

import java.util.ArrayList;
import java.util.List;

public class GeneralSOSGame extends SOSGame {
    private int redScore = 0;
    private int blueScore = 0;

    public GeneralSOSGame(int size, boolean blueIsCPU, boolean redIsCPU) {
        super(size, blueIsCPU, redIsCPU);
    }

    @Override
    protected boolean checkWinner(int row, int col) {
        List<SOSSequence> sequences = countSOS(row, col);

        if (!sequences.isEmpty()) {
            // Update scores
            if (currentPlayerObj.getColor().equals("Red")) redScore += sequences.size();
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
                    list.add(new SOSSequence(mR1, mC1, row, col, mR3, mC3, currentPlayerObj.getColor()));
                }
            }

            // Case 2: placed as first S
            int fR1 = row + dr, fC1 = col + dc;
            int fR2 = row + 2*dr, fC2 = col + 2*dc;
            if (inBounds(fR1,fC1,n) && inBounds(fR2,fC2,n)) {
                if (placed == 'S' &&
                        Character.toUpperCase(g[fR1][fC1]) == 'O' &&
                        Character.toUpperCase(g[fR2][fC2]) == 'S') {
                    list.add(new SOSSequence(row, col, fR1, fC1, fR2, fC2, currentPlayerObj.getColor()));
                }
            }

            // Case 3: placed as last S: two behind, one behind, placed
            int lR1 = row - 2*dr, lC1 = col - 2*dc;
            int lR2 = row - dr,  lC2 = col - dc;
            if (inBounds(lR1,lC1,n) && inBounds(lR2,lC2,n)) {
                if (Character.toUpperCase(g[lR1][lC1]) == 'S' &&
                        Character.toUpperCase(g[lR2][lC2]) == 'O' &&
                        placed == 'S') {
                    list.add(new SOSSequence(lR1, lC1, lR2, lC2, row, col, currentPlayerObj.getColor()));
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

    @Override
    protected List<SOSSequence> getSequencesFromLastMove(int row, int col) {
        return countSOS(row, col);
    }

    private boolean createsSOS(int row, int col, char letter) {
        char[][] grid = board.getGrid();

        // temporarily make move
        char old = grid[row][col];
        grid[row][col] = letter;

        // check sequences
        boolean found = !countSOS(row, col).isEmpty();

        // undo
        grid[row][col] = old;
        return found;
    }

    @Override
    public Move getComputerMove() {
        char[][] g = board.getGrid();
        int n = board.getSize();

        // Try all empty cells with both 'S' and 'O'
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                if (g[r][c] == '\0' || g[r][c] == ' ') {

                    // Try placing S
                    if (createsSOS(r, c, 'S'))
                        return new Move(r, c, 'S');

                    // Try placing O
                    if (createsSOS(r, c, 'O'))
                        return new Move(r, c, 'O');
                }
            }
        }

        // Otherwise fallback
        return getFirstAvailableMove();
    }

}
