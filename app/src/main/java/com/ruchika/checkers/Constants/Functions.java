package com.ruchika.checkers.Constants;

public class Functions {

    private static final int[][] w = new int[][]{
            {0, 4, 0, 4, 0, 4, 0, 4},
            {4, 0, 3, 0, 3, 0, 3, 0},
            {0, 3, 0, 2, 0, 2, 0, 4},
            {4, 0, 2, 0, 1, 0, 3, 0},
            {0, 3, 0, 1, 0, 2, 0, 4},
            {4, 0, 2, 0, 2, 0, 3, 0},
            {0, 3, 0, 3, 0, 3, 0, 4},
            {4, 0, 4, 0, 4, 0, 4, 0}
    };

   public static int[][] getMarkerBoard() {
        int[][] markerBoard = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}
        };
        return markerBoard;
    }

    public static void clearMarkerBoard(int[][] markerBoard) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                markerBoard[r][c] = 0;
            }
        }
    }


    public static double evalFunc(char[][] board) {
        double pieceScore = 0, kingScore = 0, cellScore = 0;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                char cur = board[r][c];
                if (cur == 'w') {
                    pieceScore--;
                    cellScore = cellScore - w[r][c];
                } else if (cur == 'b') {
                    pieceScore++;
                    cellScore = cellScore + w[r][c];
                } else if (cur == 'W') {
                    kingScore--;
                    cellScore = cellScore - w[r][c];
                } else if (cur == 'B') {
                    kingScore++;
                    cellScore = cellScore + w[r][c];
                }
            }
        }
        return pieceScore + (1.5 * kingScore) + (0.25 * cellScore);

    }

    public static boolean inBounds(int r, int c) {
        return (r >= 0 && r < 8 && c >= 0 && c < 8);
    }

}
