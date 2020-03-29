package com.ruchika.checkers;

/**
 * Created by ruchi on 10-03-2018.
 */
 public class MainMoves {

    private final int[] rowW = {1, 1, -1, -1};
    private final int[] colW = {-1, 1, -1, 1};
    private final int[] cRW = {2, 2, -2, -2};
    private final int[] cCW = {-2, 2, -2, 2};
    private final int[] rowB = {-1, -1, 1, 1};
    private final int[] colB = {-1, 1, -1, 1};
    private final int[] cRB = {-2, -2, 2, 2};
    private final int[] cCB = {-2, 2, -2, 2};

    public int getCapt(int first, int last) {
        if (last == first - 2) return first - 1;
        if (last == first + 2) return first + 1;
        return -1;
    }

    public boolean inBounds(int r, int c) {
        return (r >= 0 && r < 8 && c >= 0 && c < 8);
    }

    public int[] getnR(char turn) {
        if (turn == 'B') return rowB;
        else return rowW;
    }

    public int[] getnC(char turn) {
        if (turn == 'B') return colB;
        else return colW;
    }

    public int[] getcR(char turn) {
        if (turn == 'B') return cRB;
        else return cRW;
    }

    public int[] getcC(char turn) {
        if (turn == 'B') return cCB;
        else return cCW;
    }

    public char getLowercase(char upper) {
        return (char) (upper - 'A' + 'a');
    }

    public boolean isCaptureAvailable (char[][] board, char turn) {
        final char king = turn;
        char pawn = getLowercase(king);
        for (int r = 0; r < 8; r ++) {
            for (int c = 0; c < 8; c ++) {
                if (board[r][c] == king || board[r][c] == pawn){
                    if (nextCap(board, r, c, turn)) return true;
                }
            }
        }
        return false;
    }

    public boolean isNonCaptureAvailable (char[][] board, char turn) {
        final char king = turn;
        char pawn = getLowercase(king);
        for (int r = 0; r < 8; r ++) {
            for (int c = 0; c < 8; c ++) {
                if (board[r][c] == king || board[r][c] == pawn){
                    if (nextNonCap(board, r, c, turn)) return true;
                }
            }
        }
        return false;
    }

    public boolean nextCap(char[][] board, int r, int c, final char turn) {
        final char king = turn;
        char pawn = getLowercase(king);
        char op = turn == 'B' ? 'W' : 'B';
        if (board[r][c] == king || board[r][c] == pawn) {
            int en = board[r][c] == king ? 4 : 2;
            for (int i = 0; i < en; i++) {
                int R = r + getcR(turn)[i], C = c + getcC(turn)[i];
                if (inBounds(R, C) && board[R][C] == 'E') {
                    char captured = board[r + getnR(turn)[i]][c + getnC(turn)[i]];
                    if (captured == op || captured == getLowercase(op)) return true;
                }
            }
        }
        return false;
    }

    public boolean nextNonCap(char[][] board, int r, int c, char turn) {
        final char king = turn;
        char pawn = getLowercase(king);
        if (board[r][c] == king || board[r][c] == pawn) {
            int en = board[r][c] == king ? 4 : 2;
            for (int i = 0; i < en; i++) {
                int R = r + getnR(turn)[i], C = c + getnC(turn)[i];
                if (inBounds(R, C) && board[R][C] == 'E') return true;
            }
        }
        return false;
    }
}