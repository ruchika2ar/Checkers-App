package com.ruchika.checkers;

import android.util.Log;

/**
 * Created by ruchi on 22-02-2018.
 */

public class CreateBoard {

    public static char[][] createBoard(final String state) {
        char[][] board = new char[8][8];
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Character a = state.charAt((r * 8) + c);
                board[r][c] = a;
            }
        }
        return board;

    }

    public static String createState(char[][] board) {
        StringBuilder state = new StringBuilder();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                state = state.append(board[r][c]);
            }
        }
        return state.toString();
    }
}
