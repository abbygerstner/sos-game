package org.sosgame.sprint3;

public class GeneralSOSGame extends SOSGame {
    private int redScore = 0;
    private int blueScore = 0;

    public GeneralSOSGame(int size) {
        super(size);
    }

    @Override
    protected boolean checkWinner(int row, int col) {
        int points = countSOS(row, col);
        if (points > 0) {
            if (currentPlayer.equals("Red")) redScore += points;
            else blueScore += points;
        }

        // End game if board is full
        if (isBoardFull()) {
            gameInProgress = false;
            declareWinner();
        }

        // If player scored, they get another turn
        return points > 0;
    }

    private int countSOS(int row, int col) {
        int count = 0;
        char[][] g = board.getGrid();
        int n = board.getSize();
        int[][] directions = {
                {0, 1}, {1, 0}, {1, 1}, {1, -1}
        };

        for (int[] d : directions) {
            int r = row - d[0], c = col - d[1];
            int r2 = row + d[0], c2 = col + d[1];
            if (r >= 0 && c >= 0 && r2 < n && c2 < n) {
                if (g[r][c] == 'S' && g[row][col] == 'O' && g[r2][c2] == 'S') {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (board.isEmpty(i, j)) return false;
            }
        }
        return true;
    }

    private void declareWinner() {
        if (redScore > blueScore)
            System.out.println("Red wins " + redScore + " to " + blueScore);
        else if (blueScore > redScore)
            System.out.println("Blue wins " + blueScore + " to " + redScore);
        else
            System.out.println("It's a tie!");
    }

    public int getRedScore() { return redScore; }
    public int getBlueScore() { return blueScore; }
}
