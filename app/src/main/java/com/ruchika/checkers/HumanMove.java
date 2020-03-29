package com.ruchika.checkers;

import android.util.Log;

import com.ruchika.checkers.Constants.Functions;

import java.util.ArrayList;

import static com.ruchika.checkers.Constants.Functions.evalFunc;
import static com.ruchika.checkers.Constants.Functions.inBounds;

public class HumanMove {
    private final int[] nrlR = {-1, -1, 1, 1};
    private final int[] nrlC = {-1, 1, -1, 1};
    private final int[] jumpR = {-2, -2, 2, 2};
    private final int[] jumpC = {-2, 2, -2, 2};
    private int endLevel = 8;

    public HumanMove(int endLevel) {
    }

    private boolean isEndOfCapture(int r, int c, char[][] board, final boolean isKing) {
        int en = isKing ? 4 : 2;
        for (int i = 0; i < en; i++) {
            int R = r + jumpR[i], C = c + jumpC[i];
            if (inBounds(R, C) && board[R][C] == 'E') {
                char toCapture = board[r + nrlR[i]][c + nrlC[i]];
                if (toCapture == 'w' || toCapture == 'W') return false;
            }
        }
        return true;
    }

    private boolean isEndOfMove(int r, int c, char[][] board, final boolean isKing) {
        int en = isKing ? 4 : 2;
        for (int i = 0; i < en; i++) {
            int R = r + nrlR[i], C = c + nrlC[i];
            if (inBounds(R, C) && board[R][C] == 'E') return false;
        }
        return true;
    }

    public boolean hasLost(char[][] board) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (board[r][c] == 'B' || board[r][c] == 'b') {
                    final boolean isKing = board[r][c] == 'B';
                    if (!isEndOfCapture(r, c, board, isKing) || !isEndOfMove(r, c, board, isKing))
                        return false;
                }
            }
        }
        return true;
    }

    private double bestMoveCapture(int r, int c, char[][] board, final boolean isKing, Integer level,
                                double humanCur, double compCur) {
        if ((r == 0 && !isKing) || isEndOfCapture(r, c, board, isKing)) {
            if (level < endLevel) {
                return new CompMoveScore(level).nextMove(board, level + 1, humanCur);
            } else {
                return evalFunc(board);
            }
        }
        int en = isKing ? 4 : 2;
        for (int i = 0; i < en; i++) {
            int R = r + jumpR[i], C = c + jumpC[i];
            if (inBounds(R, C) && board[R][C] == 'E') {
                final char toCapture = board[r + nrlR[i]][c + nrlC[i]];
                if (toCapture == 'w' || toCapture == 'W') {
                    char original = board[r][c];
                    board[R][C] = board[r][c];
                    if (R == 0) board[R][C] = 'B';
                    board[r + nrlR[i]][c + nrlC[i]] = 'E';
                    board[r][c] = 'E';
                    double cur = bestMoveCapture(R, C, board, isKing, level, humanCur, compCur);
                    if (cur > humanCur) {
                        humanCur = cur;
                    }
                    board[r][c] = original;
                    board[r + nrlR[i]][c + nrlC[i]] = toCapture;
                    board[R][C] = 'E';
                    if (compCur <= humanCur) {
                        return humanCur;
                    }
                }
            }
        }
        return humanCur;
    }

    private double bestMoveNonCapture(int r, int c, char[][] board, final boolean isKing, Integer
            level, double humanCur, double compCur) {
        int en = isKing ? 4 : 2;
        for (int i = 0; i < en; i++) {
            final int R = r + nrlR[i], C = c + nrlC[i];
            if (inBounds(R, C) && board[R][C] == 'E') {
                char original = board[r][c];
                board[R][C] = board[r][c];
                if (R == 0) board[R][C] = 'B';
                board[r][c] = 'E';
                double score;
                if (level < endLevel) {
                    score = new CompMoveScore(level).nextMove(board, level + 1, humanCur);
                } else score = evalFunc(board);
                if (score > humanCur) humanCur = score;
                board[r][c] = original;
                board[R][C] = 'E';
                if (compCur <= humanCur) {
                    return humanCur;
                }
            }
        }
        return humanCur;
    }

    private ArrayList<int[]> captures(char[][] board) {
        ArrayList<int[]> captures = new ArrayList<>();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                char cur = board[r][c];
                if ((cur == 'b' || cur == 'B') && !isEndOfCapture(r, c, board, cur == 'B')) {
                    captures.add(new int[]{r, c});
                }
            }
        }
        return captures;
    }


    private ArrayList<int[]> nonCaptures(char[][] board) {
        ArrayList<int[]> moves = new ArrayList<>();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                char cur = board[r][c];
                if ((cur == 'b' || cur == 'B') && !isEndOfMove(r, c, board, cur == 'B')) {
                    moves.add(new int[]{r, c});
                }
            }
        }
        return moves;
    }

    public double nextMove(char[][] board, Integer level, double compCur) {
        ArrayList<int[]> capture = captures(board);
        final boolean isCapAvail = capture.size() > 0;
        double curMax = -2000;
        ArrayList<int[]> moveList = isCapAvail ? capture : nonCaptures(board);
        for (int i = 0; i < moveList.size(); i++) {
            if (compCur <= curMax) return curMax;
            int r = moveList.get(i)[0], c = moveList.get(i)[1];
            double curMove;
            final boolean isKing = board[r][c] == 'B';
            if (isCapAvail) curMove = bestMoveCapture(r, c, board, isKing, level, curMax, compCur);
            else curMove = bestMoveNonCapture(r, c, board, isKing, level, curMax, compCur);
            if (curMove > curMax) {
                curMax = curMove;
            }
        }
        return curMax;
    }
}
