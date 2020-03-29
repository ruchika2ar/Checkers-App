package com.ruchika.checkers;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import static com.ruchika.checkers.Constants.Functions.inBounds;
import static java.lang.Math.log;
import static java.lang.Math.min;

/**
 * Created by ruchi on 28-02-2018.
 */

public class CompMove {

    int level;

    CompMove(int level) {
        this.level = level;
    }

    public static final int[] nrlR = {1, 1, -1, -1};
    public static final int[] nrlC = {-1, 1, -1, 1};
    public static final int[] jumpR = {2, 2, -2, -2};
    public static final int[] jumpC = {-2, 2, -2, 2};
    boolean isCapture = false;

    private class Move {
        Stack<int[]> stack;
        double score;

        Move() {
            stack = new Stack<>();
            score = 0;
        }
    }

    /*
     * This function return true if no capturing move is available
     */

    public static boolean isEndOfCapture(int r, int c, char[][] board, final boolean isKing) {
        int en = isKing ? 4 : 2;
        for (int i = 0; i < en; i++) {
            if (inBounds(r + jumpR[i], c + jumpC[i])) {
                if (board[r + jumpR[i]][c + jumpC[i]] == 'E') {
                    char toCapture = board[r + nrlR[i]][c + nrlC[i]];
                    if (toCapture == 'b' || toCapture == 'B') return false;
                }
            }
        }
        return true;
    }

    public static boolean isEndOfMove(int r, int c, char[][] board, final boolean isKing) {
        int en = isKing ? 4 : 2;
        for (int i = 0; i < en; i++) {
            int R = r + nrlR[i], C = c + nrlC[i];
            if (inBounds(R, C) && board[R][C] == 'E') return false;
        }
        return true;
    }

    public static boolean hasLost(char[][] board) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (board[r][c] == 'W' || board[r][c] == 'w') {
                    final boolean isKing = board[r][c] == 'W';
                    if (!isEndOfCapture(r, c, board, isKing) || !isEndOfMove(r, c, board, isKing))
                        return false;
                }
            }
        }
        return true;
    }

    /*
     * This function returns a Move object containing a stack containing the best move where each element except the top of the stack is the next move
     * In the stack, the elements are integer arrays of size 2, array[0] = row and array[1] = column
     * The top is the value of r & c from where the pawn or king has to begin
     * If the stack if of size 1, where top contains the passed value of r & c, then no capture is possible
     * If the top has r == 7, then the pawn will become the king and the turn ends there
     * The returned object also contains the best score
     */

    private Move bestMoveCapture(int r, int c, char[][] board, final boolean isKing, Integer level, double compCur) {
        if ((r == 7 && !isKing) || isEndOfCapture(r, c, board, isKing)) {
            // r == 7 && !isKing is for recursive call
            Move curAns = new Move();
            curAns.score = new HumanMove(level).nextMove(board, level + 1, compCur);
            curAns.stack.push(new int[]{r, c});
            return curAns;
        }
        int en = isKing ? 4 : 2;
        Stack<int[]> bestStack = new Stack<>();
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
                    Move cur = bestMoveCapture(R, C, board, isKing, level, compCur);
                    if (cur.score < compCur) {
                        compCur = cur.score;
                        bestStack = cur.stack;
                        bestStack.push(new int[]{r, c});
                    }
                    board[r][c] = original;
                    board[r + nrlR[i]][c + nrlC[i]] = toCapture;
                    board[R][C] = 'E';
                }
            }
        }
        Move ans = new Move();
        ans.score = compCur;
        ans.stack = bestStack;
        return ans;
    }

    /*
     * This function returns a Move object containing a stack of best move
     * The top of the stack is the position form where we have to begin where this is the passed position
     * If the size of the stack is 1, then no move is possible
     * The returned object also contains the best possible score
     */

    private Move bestMoveNonCapture(int r, int c, char[][] board,
                                    final boolean isKing, int level, double compCur) {
        Move ans = new Move();
        int en = isKing ? 4 : 2;
        for (int i = 0; i < en; i++) {
            final int R = r + nrlR[i], C = c + nrlC[i];
            if (inBounds(R, C) && board[R][C] == 'E') {
                char original = board[r][c];
                if (R == 7) board[R][C] = 'W';
                else board[R][C] = board[r][c];
                board[r][c] = 'E';
                double score = new HumanMove(level).nextMove(board, level + 1, compCur);
                if (score < compCur) {
                    Stack<int[]> stack = new Stack<>();
                    stack.push(new int[]{R, C});
                    ans.stack = stack;
                    compCur = score;
                }
                board[r][c] = original;
                board[R][C] = 'E';
            }
        }
        ans.stack.push(new int[]{r, c});
        ans.score = compCur;
        return ans;
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

    /*
    This function returns a stack of the next move where top is the current position from
    where we have to start
     */

    public Stack<int[]> nextMove(char[][] board, Integer level) {
        ArrayList<int[]> capture = captures(board), movesList;
        double curMin = 2000;
        Stack <int[]> ans = new Stack<>();
        boolean captureAvailable = capture.size() > 0;
        isCapture = captureAvailable;
        movesList = captureAvailable ? capture : nonCaptures(board);
        ArrayList<Stack<int[]>> possibleMoves = new ArrayList<>();
        for (int i = 0; i < movesList.size(); i++) {
            int r = movesList.get(i)[0], c = movesList.get(i)[1];
            final boolean isKing = board[r][c] == 'W';
            Move curMove;
            if (captureAvailable) curMove = bestMoveCapture(r, c, board, isKing, level, curMin);
            else curMove = bestMoveNonCapture(r, c, board, isKing, level, curMin);
            if (curMove.stack.size() > 1 && curMove.score <= curMin) {
                if (curMove.score < curMin) {
                    ans = curMove.stack;
                    possibleMoves.clear();
                }
                possibleMoves.add(curMove.stack);
                curMin = curMove.score;
            }
        }
        if (possibleMoves.size() > 0) {
            Integer moveIndex = new Random().nextInt(possibleMoves.size());
            return possibleMoves.get(moveIndex);
        } else return ans;
    }
}