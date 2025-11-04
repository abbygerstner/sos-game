package org.sosgame.sprint3;

public class SimpleSOSGame extends SOSGame {

    public SimpleSOSGame(int size) {
        super(size);
    }

    @Override
    protected boolean checkWinner(int row, int col) {
        // If current move creates any "SOS", game ends
        if (formsSOS(row, col)) {
            System.out.println(currentPlayer + " wins!");
            gameInProgress = false;
            return true;
        }
        return false;
    }

    private boolean formsSOS(int row, int col) {
        // Check horizontally, vertically, and diagonally
        char[][] g = board.getGrid();
        int n = board.getSize();

        // Check all directions (horizontal, vertical, both diagonals)
        int[][] directions = {
                {0, 1}, {1, 0}, {1, 1}, {1, -1}
        };

        for (int[] d : directions) {
            int r = row - d[0], c = col - d[1];
            int r2 = row + d[0], c2 = col + d[1];

            if (r >= 0 && c >= 0 && r2 < n && c2 < n) {
                if (g[r][c] == 'S' && g[row][col] == 'O' && g[r2][c2] == 'S') {
                    return true;
                }
            }
        }
        return false;
    }

}
