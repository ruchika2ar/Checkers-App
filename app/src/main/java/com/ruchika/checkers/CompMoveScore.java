package com.ruchika.checkers;

import android.util.Log;

import java.util.ArrayList;

import static com.ruchika.checkers.CompMove.isEndOfCapture;
import static com.ruchika.checkers.CompMove.isEndOfMove;
import static com.ruchika.checkers.CompMove.jumpR;
import static com.ruchika.checkers.CompMove.jumpC;
import static com.ruchika.checkers.CompMove.nrlR;
import static com.ruchika.checkers.CompMove.nrlC;
import static com.ruchika.checkers.Constants.Functions.inBounds;

public class CompMoveScore {
    int level;

    public CompMoveScore(int level) {
        this.level = level;
    }

    private ArrayList<int[]> captures(char[][] board) {
        ArrayList<int[]> captures = new ArrayList<>();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                char cur = board[r][c];
                if ((cur == 'w' || cur == 'W') && !isEndOfCapture(r, c, board, cur == 'W')) {
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
                if ((cur == 'w' || cur == 'W') && !isEndOfMove(r, c, board, cur == 'W')) {
                    moves.add(new int[]{r, c});
                }
            }
        }
        return moves;
    }

    private double bestMoveCapture(int r, int c, char[][] board, final boolean isKing,
                                   int level, double compCur, double humanCur) {
        if ((r == 7 && !isKing) || isEndOfCapture(r, c, board, isKing)) {
            return new HumanMove(level).nextMove(board, level + 1, compCur);
        }
        int en = isKing ? 4 : 2;
        for (int i = 0; i < en; i++) {
            final int R = r + jumpR[i], C = c + jumpC[i];
            if (inBounds(R, C) && board[R][C] == 'E') {
                final char toCapture = board[r + nrlR[i]][c + nrlC[i]];
                if (toCapture == 'b' || toCapture == 'B') {
                    char original = board[r][c];
                    board[R][C] = board[r][c];
                    if (R == 7) board[R][C] = 'W';
                    board[r + nrlR[i]][c + nrlC[i]] = 'E';
                    board[r][c] = 'E';
                    double cur = bestMoveCapture(R, C, board, isKing, level, compCur, humanCur);
                    if (cur < compCur) compCur = cur;
                    board[r][c] = original;
                    board[r + nrlR[i]][c + nrlC[i]] = toCapture;
                    board[R][C] = 'E';
                    if (compCur <= humanCur) {
                        return compCur;
                    }
                }
            }
        }
        return compCur;
    }

    private double bestMoveNonCapture(int r, int c, char[][] board, final boolean isKing,
                                      int level, double compCur, double humanCur) {
        int en = isKing ? 4 : 2;
        for (int i = 0; i < en; i++) {
            final int R = r + nrlR[i], C = c + nrlC[i];
            if (inBounds(R, C) && board[R][C] == 'E') {
                char original = board[r][c];
                board[R][C] = board[r][c];
                if (R == 7) board[R][C] = 'W';
                board[r][c] = 'E';
                double score = new HumanMove(level).nextMove(board, level + 1, compCur);
                if (score < compCur) compCur = score;
                board[r][c] = original;
                board[R][C] = 'E';
                if (compCur <= humanCur) {
                    return compCur;
                }
            }
        }
        return compCur;
    }


    public double nextMove(char[][] board, Integer level, double humanCur) {
        ArrayList<int[]> capture = captures(board);
        double curMin = 2000;
        boolean isCapAvail = capture.size() > 0;
        ArrayList<int[]> movesList = isCapAvail ? capture : nonCaptures(board);
        for (int i = 0; i < movesList.size(); i++) {
            if (curMin <= humanCur) return curMin;
            int r = movesList.get(i)[0], c = movesList.get(i)[1];
            double curMove;
            final boolean isKing = board[r][c] == 'W';
            if (isCapAvail) curMove = bestMoveCapture(r, c, board, isKing, level, curMin, humanCur);
            else curMove = bestMoveNonCapture(r, c, board, isKing, level, curMin, humanCur);
            if (curMove < curMin) curMin = curMove;
        }
        return curMin;
    }
}