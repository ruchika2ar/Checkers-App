package com.ruchika.checkers;

/**
 * Created by ruchi on 24-03-2018.
 */

public class LastPos {
    int exR;
    int exC;
    boolean isKing;
    boolean isCapturing;
    boolean moveInProcess;

    void setLastPos(int exR, int exC, boolean isKing, boolean isCapturing, boolean moveInProcess) {
        this.exR = exR;
        this.exC = exC;
        this.isKing = isKing;
        this.isCapturing = isCapturing;
        this.moveInProcess = moveInProcess;
    }
}
