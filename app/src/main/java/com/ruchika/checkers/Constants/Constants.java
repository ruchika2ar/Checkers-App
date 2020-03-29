package com.ruchika.checkers.Constants;

import com.ruchika.checkers.R;

public class Constants {
    public static final String TWO_PLAYERS = "TWO_PLAYERS";
    public static final String COMPUTER = "COMPUTER";
    public static final String STATE = "STATE";
    public static final String THEME = "THEME";
    public static final String[] light_piece = new String[]{"#ffffff", "#ffffff", "#ffffff"};
    public static final String[] dark_piece = new String[]{"#000000", "#7289da", "#00bfa5"};
    public static final String[] border = new String[]{"#000000", "#000000", "#000000"};
    public static final String[] dark_bg = new String[]{"#5c4033", "#2c2f33", "#212121"};
    public static final String[] light_bg = new String[]{"#d2b48c", "#99aab5", "#424242"};
    public static final String[] bg = new String[]{"#bb772e", "#23272a", "#23272a"};
    public static final String[] marker = new String[]{"#806641", "#707070", "#707070"};
    public static final int[] btnMoreDrawable = new int[]{
            R.drawable.ic_more_vert_black_36dp,
            R.drawable.ic_more_vert_white_36dp,
            R.drawable.ic_more_vert_white_36dp};
    public static final int[] radioButton = new int[]{R.id.rbClassic, R.id.rbDefault, R.id.rbDark};
    public static final String[] rules = new String[]{
            "The game starts with 12 men on each side.\n\n" +
                    "A man can be crowned to become a king.",
            "A man can only move forward diagonally towards the opposite opponent whereas " +
                    "a king can move diagonally in all four directions.\n\n" +
                    "The darker side always makes the first move.",
            "There are two types of moves available -\n\nA capturing and a non capturing move.",
            "If the space on forward diagonal next to man is empty and no capturing move is available, then it can move to that space.",
            "If one of the opponent’s piece is on a forward diagonal next to a man, and the next space beyond it is empty, then it must jump the opponent’s" +
                    " checker and land in the space beyond.\n\nThe opponent’s checker is captured and removed from the board.",
            "If a capture is available, then the piece must make it.\n\nHowever, in case of multiple captures available, one can choose any.\n" +
                    "Also, once a capture is made, the capturing piece has to keep making captures till no more captures are available.",
            "When a man reaches the opposite end of the board, it is crowned and becomes a king ♛.\nThe turn ends there.\n\n" +
                    "The rules regarding capturing and non capturing moves remain the same for both type of pieces.",
            "When the opposite side cannot make any move or all its pieces are captured, it loses."
    };

    public static final String[] heading_rules = new String[]{
            "Pieces", "Movement", "Moves", "Non Capturing Move", "Capturing Move", "Mandatory Capture", "Crowning", "Winning"
    };

}
