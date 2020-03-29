package com.ruchika.checkers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.ruchika.checkers.Constants.Constants;
import com.ruchika.checkers.Constants.Functions;

import java.util.Stack;

import static com.ruchika.checkers.CompMove.hasLost;
import static com.ruchika.checkers.MainActivity.MAIN_STATE;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    char[][] board;
    int[][] markerBoard;
    ConstraintLayout mainBack;
    int numMoves = 0;
    int drawLimit = 70;
    boolean activityVisible = true, dialogOnDisplay = false, isMuted = false;
    Integer curVolume = 0;
    char nextTurn = 'B';
    MainMoves m = new MainMoves();
    ImageButton btnMore;
    TextSwitcher A2, A4, A6, A8, B1, B3, B5, B7;
    TextSwitcher C2, C4, C6, C8, D1, D3, D5, D7;
    TextSwitcher E2, E4, E6, E8, F1, F3, F5, F7;
    TextSwitcher G2, G4, G6, G8, H1, H3, H5, H7;
    Button btnAnchor;
    TextSwitcher[][] btnArray;
    LastPos lp = new LastPos();
    TextView tvTimer;
    long timeRem;
    Timer timer, resTimer = null;
    CoordinatorLayout clWrapper;
    MediaPlayer mp = new MediaPlayer();
    boolean twoP, toResume = false;
    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8;
    TextView tv9, tv10, tv11, tv12, tv13, tv14, tv15, tv16;
    TextView tv17, tv18, tv19, tv20, tv21, tv22, tv23, tv24;
    TextView tv25, tv26, tv27, tv28, tv29, tv30, tv31, tv32;
    TextView[][] tv;
    String white_col = "#ffffff", black_col = "#000000";
    String border_col = "#000000", dark_back_col = "#5c4033";
    String light_back_col = "#d2b48c", back_col = "#bb772e";
    String col_mark = "#b2ebf2";
    Integer themeOption = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout2);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        clWrapper = findViewById(R.id.clWrapper);
        btnMore = findViewById(R.id.btnMore);
        timer = new Timer(60000, 1000);
        twoP = getIntent().getBooleanExtra("twoP", true);
        String prefType = twoP ? Constants.TWO_PLAYERS : Constants.COMPUTER;
        String state = getSharedPreferences(prefType, MODE_PRIVATE).getString(Constants.STATE, MAIN_STATE);
        board = CreateBoard.createBoard(state);
        markerBoard = Functions.getMarkerBoard();
        tvTimer = findViewById(R.id.tvTimer);
        mainBack = findViewById(R.id.mainBackground);
        btnAnchor = findViewById(R.id.btnAnchor);

        btnArray = new TextSwitcher[][]{
                {null, B1, null, D1, null, F1, null, H1},
                {A2, null, C2, null, E2, null, G2, null},
                {null, B3, null, D3, null, F3, null, H3},
                {A4, null, C4, null, E4, null, G4, null},
                {null, B5, null, D5, null, F5, null, H5},
                {A6, null, C6, null, E6, null, G6, null},
                {null, B7, null, D7, null, F7, null, H7},
                {A8, null, C8, null, E8, null, G8, null},
        };


        tv = new TextView[][]{
                {tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8},
                {tv9, tv10, tv11, tv12, tv13, tv14, tv15, tv16},
                {tv17, tv18, tv19, tv20, tv21, tv22, tv23, tv24},
                {tv25, tv26, tv27, tv28, tv29, tv30, tv31, tv32}
        };

        Integer[][] arrayId = new Integer[][]{
                {null, R.id.B1, null, R.id.D1, null, R.id.F1, null, R.id.H1},
                {R.id.A2, null, R.id.C2, null, R.id.E2, null, R.id.G2, null},
                {null, R.id.B3, null, R.id.D3, null, R.id.F3, null, R.id.H3},
                {R.id.A4, null, R.id.C4, null, R.id.E4, null, R.id.G4, null},
                {null, R.id.B5, null, R.id.D5, null, R.id.F5, null, R.id.H5},
                {R.id.A6, null, R.id.C6, null, R.id.E6, null, R.id.G6, null},
                {null, R.id.B7, null, R.id.D7, null, R.id.F7, null, R.id.H7},
                {R.id.A8, null, R.id.C8, null, R.id.E8, null, R.id.G8, null},
        };

        Integer[][] tvId = new Integer[][]{
                {R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4, R.id.tv5, R.id.tv6, R.id.tv7, R.id.tv8},
                {R.id.tv9, R.id.tv10, R.id.tv11, R.id.tv12, R.id.tv13, R.id.tv14, R.id.tv15, R.id.tv16},
                {R.id.tv17, R.id.tv18, R.id.tv19, R.id.tv20, R.id.tv21, R.id.tv22, R.id.tv23, R.id.tv24},
                {R.id.tv25, R.id.tv26, R.id.tv27, R.id.tv28, R.id.tv29, R.id.tv30, R.id.tv31, R.id.tv32}
        };

        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 8; c++) {
                tv[r][c] = findViewById(tvId[r][c]);
            }
        }

        themeOption = getMyTheme();
        setColours(themeOption);

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (arrayId[r][c] != null) {
                    btnArray[r][c] = findViewById(arrayId[r][c]);
                    btnArray[r][c].setOnClickListener(this);
                    String clr = (board[r][c] == 'W' || board[r][c] == 'w') ? white_col : black_col;
                    MyViewFactory myViewFactory = new MyViewFactory(clr);
                    btnArray[r][c].setFactory(myViewFactory);
                    if (board[r][c] != 'E') {
                        btnArray[r][c].setInAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
                        btnArray[r][c].setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));
                    }
                }
            }
        }

        int[] layoutID = new int[]{R.id.a, R.id.b, R.id.c, R.id.c, R.id.d, R.id.e, R.id.f, R.id.g, R.id.h};
        int w = getScreenWidth();

        for (int i = 0; i < layoutID.length; i++) {
            LinearLayout a = findViewById(layoutID[i]);
            ViewGroup.LayoutParams layoutParams = a.getLayoutParams();
            layoutParams.height = w;
            a.setLayoutParams(layoutParams);
        }
        TextView left = findViewById(R.id.left);
        TextView right = findViewById(R.id.right);
        ViewGroup.LayoutParams layoutParams = left.getLayoutParams();
        layoutParams.height = w;
        left.setLayoutParams(layoutParams);

        layoutParams = right.getLayoutParams();
        layoutParams.height = w;
        right.setLayoutParams(layoutParams);

        setMyLayout(true);
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });
        timer.start();
    }

    private int getMyTheme() {
        SharedPreferences sp = getSharedPreferences(Constants.THEME, MODE_PRIVATE);
        return sp.getInt(Constants.THEME, 1);
    }

    class MyViewFactory implements ViewSwitcher.ViewFactory {
        String clr;

        MyViewFactory(String clr) {
            this.clr = clr;
        }

        @Override
        public View makeView() {
            TextView t = new TextView(Main2Activity.this);
            int w = getScreenWidth();
            w = w / 8;  // size of one block
            t.setHeight(w);
            t.setTextColor(Color.parseColor(clr));
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
                t.setTextSize(31);
            } else {
                t.setTextSize(36);
            }
            t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            return t;
        }
    }

    int getScreenWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return (size.x - 40);
    }

    private void addDotsNonCapture(int r, int c, char turn) {
        Functions.clearMarkerBoard(markerBoard);
        final char king = turn;
        char pawn = m.getLowercase(king);
        if (board[r][c] == king || board[r][c] == pawn) {
            int en = board[r][c] == king ? 4 : 2;
            for (int i = 0; i < en; i++) {
                int R = r + m.getnR(turn)[i], C = c + m.getnC(turn)[i];
                if (m.inBounds(R, C) && board[R][C] == 'E') {
                    markerBoard[R][C] = 1;
                    setButtonText(R, C, "*", col_mark);
                }
            }
        }
    }

    private void addDotsCapture(int r, int c, char turn) {
        Functions.clearMarkerBoard(markerBoard);
        final char king = turn;
        char pawn = m.getLowercase(king);
        char op = turn == 'B' ? 'W' : 'B';
        if (board[r][c] == king || board[r][c] == pawn) {
            int en = board[r][c] == king ? 4 : 2;
            for (int i = 0; i < en; i++) {
                int R = r + m.getcR(turn)[i], C = c + m.getcC(turn)[i];
                if (m.inBounds(R, C) && board[R][C] == 'E') {
                    char captured = board[r + m.getnR(turn)[i]][c + m.getnC(turn)[i]];
                    if (captured == op || captured == m.getLowercase(op)) {
                        markerBoard[R][C] = 1;
                        setButtonText(R, C, "*", col_mark);
                    }
                }
            }
        }
    }

    private void removeDots() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                TextSwitcher cur = btnArray[r][c];
                if (cur != null && markerBoard[r][c] == 1) {
                    setButtonText(r, c, "", dark_back_col);
                }
            }
        }
        Functions.clearMarkerBoard(markerBoard);
    }

    private void setButtonText(Integer r, Integer c, String s, String col) {
        if (btnArray[r][c] != null) {
            if (s.equals("*")) {
                btnArray[r][c].setInAnimation(Main2Activity.this, R.anim.fade_in);
                btnArray[r][c].setOutAnimation(Main2Activity.this, R.anim.fade_out);
                btnArray[r][c].setText("");
                btnArray[r][c].setInAnimation(Main2Activity.this, R.anim.fade_in);
                btnArray[r][c].setOutAnimation(Main2Activity.this, R.anim.fade_out);
            }
            btnArray[r][c].setText(s);
            TextView tvCur = (TextView) btnArray[r][c].getCurrentView();
            if (s.equals("*")) {
                tvCur.setBackgroundColor(Color.parseColor(col_mark));
            } else {
                tvCur.setBackgroundColor(Color.parseColor(dark_back_col));
            }
            tvCur.setTextColor(Color.parseColor(col));
        }
    }

    int[] getCoordinate(View v) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (btnArray[r][c] != null && btnArray[r][c].getId() == v.getId())
                    return new int[]{r, c};
            }
        }
        return null;
    }

    void makeMove(View view, final char turn) {
        if (twoP || turn == 'B') {
            int[] point = getCoordinate(view);
            int row = point[0], col = point[1];
            String color = turn == 'B' ? black_col : white_col;
            char king = turn, pawn = m.getLowercase(king);
            char op = turn == 'B' ? 'W' : 'B';
            int rowEnd = turn == 'B' ? 0 : 7;
            String colorOp = turn == 'W' ? black_col : white_col;

            if (markerBoard[row][col] == 1) {
                numMoves++;
                markerBoard[row][col] = 0;
                removeDots();
                board[lp.exR][lp.exC] = 'E';
                setAnimation(lp.exR, lp.exC, row, col);
                setButtonText(lp.exR, lp.exC, "", color);
                boolean turnEnd = !lp.isKing && row == rowEnd;
                if (lp.isKing || row == rowEnd) {
                    setButtonText(row, col, "♛", color);
                    board[row][col] = king;
                } else {
                    setButtonText(row, col, "⬤", color);
                    board[row][col] = pawn;
                }
                if (lp.isCapturing) {
                    if (!turnEnd) {
                        if (mp != null) mp.release();
                        mp = MediaPlayer.create(Main2Activity.this, R.raw.sound);
                        mp.start();
                    }
                    int captR = m.getCapt(lp.exR, row);
                    int captC = m.getCapt(lp.exC, col);
                    setButtonText(captR, captC, "", color);
                    board[captR][captC] = 'E';
                }
                if (turnEnd) {
                    if (mp != null) mp.release();
                    mp = MediaPlayer.create(Main2Activity.this, R.raw.king_ta_da);
                    mp.start();
                }
                if (lp.isCapturing && m.nextCap(board, row, col, turn) && !turnEnd) {
                    addDotsCapture(row, col, turn);
                    lp.setLastPos(row, col, board[row][col] == king, true, true);
                } else {
                    lp.setLastPos(-1, -1, false, false, false);
                    if (resTimer != null) {
                        resTimer.cancel();
                        resTimer = null;
                    }
                    timer.cancel();
                    timer.start();
                    if (twoP) {
                        tvTimer.setTextColor(Color.parseColor(colorOp));
                        nextTurn = op;
                        if (!m.isCaptureAvailable(board, op) && !m.isNonCaptureAvailable(board, op)) {
                            Handler h = new Handler();
                            h.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    showWinningDialog(turn);
                                }
                            }, 300);
                        }
                    } else {
                        tvTimer.setTextColor(Color.parseColor(colorOp));
                        computerTurn();
                    }
                }
            } else {
                if ((board[row][col] == king || board[row][col] == pawn) && !lp.moveInProcess) {
                    if (m.isCaptureAvailable(board, turn)) {
                        removeDots();
                        if (m.nextCap(board, row, col, turn)) {
                            addDotsCapture(row, col, turn);
                            lp.setLastPos(row, col, board[row][col] == king, true, false);
                        } else {
                            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                            if (vibrator != null) {
                                vibrator.vibrate(100);
                            }
                            Snackbar.make(findViewById(R.id.clWrapper), "Capture Available", Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        if (m.isNonCaptureAvailable(board, turn)) {
                            removeDots();
                            if (m.nextNonCap(board, row, col, turn)) {
                                addDotsNonCapture(row, col, turn);
                                lp.setLastPos(row, col, board[row][col] == king, false, false);
                            }
                        }
                    }
                }
            }
        }
    }

    class AsynC extends AsyncTask<Integer, Integer, Void> {

        int prevR;
        int prevC;
        int nextR;
        int nextC;
        final int num;
        final int count;
        boolean King;

        public AsynC(int num, int count) {
            this.num = num;
            this.count = count;
        }

        public void setAsync(int prevR, int prevC, int nextR, int nextC, boolean king) {
            this.prevR = prevR;
            this.prevC = prevC;
            this.nextR = nextR;
            this.nextC = nextC;
            this.King = king;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            int n = integers[0];
            waitNsec(n);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            final String txt = King ? "♛" : "⬤";
            removeDots();
            setAnimation(prevR, prevC, nextR, nextC);
            setButtonText(prevR, prevC, "", black_col);
            int captR = m.getCapt(prevR, nextR);
            int captC = m.getCapt(prevC, nextC);
            boolean newKing = board[nextR][nextC] == 'w' && nextR == 7;
            if (captR != -1 && captC != -1) {
                setButtonText(captR, captC, "", black_col);
            }
            if (board[nextR][nextC] == 'w' && nextR == 7) {
                setButtonText(nextR, nextC, "♛", white_col);
                board[nextR][nextC] = 'W';
            } else {
                setButtonText(nextR, nextC, txt, white_col);
            }
            if (mp != null) mp.release();
            mp = MediaPlayer.create(Main2Activity.this, R.raw.sound);
            if (captR != -1 && captC != -1 && !newKing) {
                mp.start();
            }
            if (newKing) {
                if (mp != null) mp.release();
                mp = MediaPlayer.create(Main2Activity.this, R.raw.king_ta_da);
                mp.start();
            }
            if (count == num) {
                checkDraw();
                nextTurn = 'B';
                if (!twoP && activityVisible && !toResume) {
                    timer.cancel();
                    timer.start();
                }
                HumanMove humanMove = new HumanMove(0);
                if (humanMove.hasLost(board)) {
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showWinningDialog('W');
                        }
                    }, 300);
                } else {
                    tvTimer.setTextColor(Color.parseColor(black_col));
                }
            }
        }
    }

    void waitNsec(int n) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() < start + n) {
        }
    }

    class CalcMove extends AsyncTask<Void, Void, Stack<int[]>> {

        CompMove compMove;

        CalcMove(CompMove compMove) {
            this.compMove = compMove;
        }

        @Override
        protected Stack<int[]> doInBackground(Void... voids) {
            waitNsec(600);
            return compMove.nextMove(board, 1);
        }

        @Override
        protected void onPostExecute(Stack<int[]> move) {
            int count = 0;
            final int initSize = move.size() - 1;
            while (move.size() > 1) {
                count++;
                final Integer prevR = (move.peek())[0], prevC = (move.peek())[1];
                move.pop();
                Integer curR = (move.peek()[0]), curC = (move.peek()[1]);
                if (compMove.isCapture) {
                    Integer captR = m.getCapt(prevR, curR);
                    Integer captC = m.getCapt(prevC, curC);
                    board[captR][captC] = 'E';
                }
                board[curR][curC] = board[prevR][prevC];
                board[prevR][prevC] = 'E';
                markerBoard[curR][curC] = 1;
                // setButtonText(curR, curC, "⨀", col_mark);
                setButtonText(curR, curC, "*", col_mark);
                AsynC asynC = new AsynC(initSize, count);
                asynC.setAsync(prevR, prevC, curR, curC, board[curR][curC] == 'W');
                asynC.execute(600);
            }
        }


    }

    void computerTurn() {
        nextTurn = 'W';
        if (hasLost(board)) {
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showWinningDialog('B');
                }
            }, 300);
        }
        numMoves++;
        new com.ruchika.checkers.Main2Activity.CalcMove(new CompMove(40)).execute();
    }

    @Override
    public void onClick(View view) {
        // if (mp != null) mp.release();
        // mp = MediaPlayer.create(com.ruchika.checkers.Main2Activity.this, R.raw.sound);
        makeMove(view, nextTurn);
    }

    @Override
    public void onBackPressed() {
        pauseTimer();
        dialogOnDisplay = true;
        final ModalBottomSheetHome mSheet = new ModalBottomSheetHome();
        mSheet.setListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp;
                String constant = twoP ? Constants.TWO_PLAYERS : Constants.COMPUTER;
                sp = getSharedPreferences(constant, MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                if (mSheet.cbSave.isChecked()) {
                    editor.putString(Constants.STATE, CreateBoard.createState(board));
                    editor.apply();
                } else {
                    editor.putString(Constants.STATE, MAIN_STATE);
                    editor.apply();
                }
                dialogOnDisplay = false;
                mSheet.dismiss();
                com.ruchika.checkers.Main2Activity.super.onBackPressed();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOnDisplay = false;
                resumeTimer();
                mSheet.dismiss();
            }
        });
        mSheet.show(getSupportFragmentManager(), "HOME");
    }

    void showDrawDialog() {
        pauseTimer();
        dialogOnDisplay = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
        builder.setCancelable(false);
        View view = getLayoutInflater().inflate(R.layout.dialog_main, null, false);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        TextView tvQues = dialog.findViewById(R.id.tvText);
        Button btnAccept = dialog.findViewById(R.id.btnPositive);
        Button btnDeny = dialog.findViewById(R.id.btnNegative);
        btnAccept.setText("Accept");
        btnDeny.setText("Deny");
        String statement = "It seems like a draw!";
        tvQues.setText(statement);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
                dialogOnDisplay = false;
                resumeTimer();
                showWinningDialog('D');
            }
        });
        btnDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawLimit = drawLimit + 20;
                dialog.hide();
                dialogOnDisplay = false;
                resumeTimer();
            }
        });

    }

    void checkDraw() {
        if (numMoves > drawLimit) {
            showDrawDialog();
        }
    }

    void showWinningDialog(char w) {
        if (mp != null) mp.release();
        mp = MediaPlayer.create(Main2Activity.this, R.raw.applause2);
        pauseTimer();
        dialogOnDisplay = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        View view = getLayoutInflater().inflate(R.layout.winning_dialog, null, false);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        TextView tvWinner = dialog.findViewById(R.id.tvText);
        String winner;
        if (!twoP) {
            if (w != 'D') {
                winner = w == 'W' ? "You lose!" : "You Win!";
                if (w == 'B') mp.start();
                else {
                    if (mp != null) mp.release();
                    mp = MediaPlayer.create(Main2Activity.this, R.raw.losing_sound);
                    mp.start();
                }
            } else {
                winner = "It's a draw!";
                if (mp != null) mp.release();
                mp = MediaPlayer.create(Main2Activity.this, R.raw.draw);
                mp.start();
            }
        } else {
            winner = w == 'B' ? "Dark Wins!" : "White Wins!";
            mp.start();
        }
        tvWinner.setText(winner);
        ImageButton btnPlayAgain = dialog.findViewById(R.id.btnPositive);
        ImageButton btnHome = dialog.findViewById(R.id.btnNegative);
        Button btnP = dialog.findViewById(R.id.btnPos);
        Button btnN = dialog.findViewById(R.id.btnNeg);
        SharedPreferences sp;
        if (twoP) {
            sp = getSharedPreferences(Constants.TWO_PLAYERS, MODE_PRIVATE);
        } else {
            sp = getSharedPreferences(Constants.COMPUTER, MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.STATE, MAIN_STATE);
        editor.apply();
        View.OnClickListener pos = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
                dialogOnDisplay = false;
                Intent i = new Intent(com.ruchika.checkers.Main2Activity.this, com.ruchika.checkers.Main2Activity.class);
                i.putExtra("twoP", twoP);
                finish();
                startActivity(i);
            }
        };
        View.OnClickListener neg = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
                dialogOnDisplay = false;
                finish();
            }
        };
        btnHome.setOnClickListener(neg);
        btnN.setOnClickListener(neg);
        btnP.setOnClickListener(pos);
        btnPlayAgain.setOnClickListener(pos);


    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeTimer();
    }

    @Override
    protected void onDestroy() {
        activityVisible = false;
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        activityVisible = true;
    }

    void showRestartDialog() {
        pauseTimer();
        dialogOnDisplay = true;
        final ModalBottomSheet mSheet = new ModalBottomSheet();
        mSheet.setListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOnDisplay = false;
                Intent i = new Intent(Main2Activity.this, Main2Activity.class);
                i.putExtra("twoP", twoP);
                SharedPreferences sp = getSharedPreferences(Constants.STATE, MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(Constants.STATE, MAIN_STATE);
                editor.apply();
                finish();
                startActivity(i);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOnDisplay = false;
                resumeTimer();
                mSheet.dismiss();
            }
        });
        mSheet.show(getSupportFragmentManager(), "RESTART");
    }

    @Override
    protected void onStop() {
        pauseTimer();
        super.onStop();
    }

    class Timer extends CountDownTimer {

        long timerem;

        public Timer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            timerem = millisUntilFinished;
            String txt = "00:" + String.valueOf(millisUntilFinished / 1000);
            tvTimer.setText(txt);
        }

        @Override
        public void onFinish() {
            if (activityVisible) {
                final char w = nextTurn == 'W' ? 'B' : 'W';
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showWinningDialog(w);
                    }
                }, 300);

            }
        }

        public long getTimeRem() {
            return timerem;
        }

    }

    void pauseTimer() {
        if (activityVisible && !toResume) {
            activityVisible = false;
            toResume = true;
            if (resTimer != null) {
                timeRem = resTimer.getTimeRem();
                resTimer.cancel();
                resTimer = null;
            } else {
                timeRem = timer.getTimeRem();
            }
            timer.cancel();
        }
    }

    void resumeTimer() {
        if (toResume && !dialogOnDisplay) {
            resTimer = new Timer(timeRem, 1000); // resTimer is used because of varying constructor values
            resTimer.start();
            timeRem = 0;
            toResume = false;
            activityVisible = true;
        }
    }

    private void showPopup() {
        PopupMenu popupMenu = new PopupMenu(Main2Activity.this, btnAnchor);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.my_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mute:
                        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
                        if (am != null) {
                            if (!isMuted) {
                                curVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                                am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                                isMuted = true;
                            } else {
                                am.setStreamVolume(AudioManager.STREAM_MUSIC, curVolume, 0);
                                isMuted = false;
                            }
                        }
                        return true;
                    case R.id.restart:
                        showRestartDialog();
                        return true;
                    case R.id.menu_home:
                        onBackPressed();
                        return true;
                    case R.id.theme:
                        showThemeBottomSheet();
                        return true;
                    default:
                        return false;
                }
            }
        });
        String muteTitle = isMuted ? "Unmute" : "Mute";
        popupMenu.getMenu().findItem(R.id.mute).setTitle(muteTitle);
        popupMenu.show();
    }

    void showThemeBottomSheet() {
        pauseTimer();
        dialogOnDisplay = true;
        final ModalBottomSheetTheme mSheet = new ModalBottomSheetTheme();
        mSheet.setOnClickListeners(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int checked = mSheet.rgTheme.getCheckedRadioButtonId();
                        int chosenTheme;
                        if (checked == mSheet.rbClassic.getId()) chosenTheme = 0;
                        else if (checked == mSheet.rbDark.getId()) chosenTheme = 2;
                        else chosenTheme = 1;
                        setColours(chosenTheme);
                        setMyLayout(false);
                        themeOption = chosenTheme;
                        SharedPreferences sp = getSharedPreferences(Constants.THEME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt(Constants.THEME, themeOption);
                        editor.apply();
                        dialogOnDisplay = false;
                        resumeTimer();
                        mSheet.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogOnDisplay = false;
                        resumeTimer();
                        mSheet.dismiss();
                    }
                });
        mSheet.setCurrentTheme(themeOption);
        mSheet.show(getSupportFragmentManager(), "THEME");
    }

    void setColours(int mTheme) {
        white_col = Constants.light_piece[mTheme];
        black_col = Constants.dark_piece[mTheme];
        border_col = Constants.border[mTheme];
        dark_back_col = Constants.dark_bg[mTheme];
        light_back_col = Constants.light_bg[mTheme];
        back_col = Constants.bg[mTheme];
        col_mark = Constants.marker[mTheme];
        btnMore.setImageResource(Constants.btnMoreDrawable[mTheme]);
    }

    void setMyLayout(boolean firstTime) {
        mainBack.setBackgroundColor(Color.parseColor(back_col));
        if (nextTurn == 'W') {
            tvTimer.setTextColor(Color.parseColor(white_col));
        } else tvTimer.setTextColor(Color.parseColor(black_col));
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (btnArray[r][c] != null) {
                    btnArray[r][c].setBackgroundColor(Color.parseColor(dark_back_col));
                    TextView tv;
                    tv = (TextView) btnArray[r][c].getCurrentView();
                    tv.setBackgroundColor(Color.parseColor(dark_back_col));
                    tv = (TextView) btnArray[r][c].getNextView();
                    tv.setBackgroundColor(Color.parseColor(dark_back_col));
                    tv = (TextView) btnArray[r][c].getCurrentView();
                    tv.setBackgroundColor(Color.parseColor(dark_back_col));
                    if (!firstTime) {
                        if (board[r][c] == 'E') {
                            btnArray[r][c].setCurrentText("");
                        } else if (board[r][c] == 'b') {
                            btnArray[r][c].setCurrentText("⬤");
                            tv.setTextColor(Color.parseColor(black_col));
                        } else if (board[r][c] == 'w') {
                            btnArray[r][c].setCurrentText("⬤");
                            tv.setTextColor(Color.parseColor(white_col));
                        } else if (board[r][c] == 'B') {
                            btnArray[r][c].setCurrentText("♛");
                            tv.setTextColor(Color.parseColor(black_col));
                        } else if (board[r][c] == 'W') {
                            btnArray[r][c].setCurrentText("♛");
                            tv.setTextColor(Color.parseColor(white_col));
                        }
                    } else {
                        if (btnArray[r][c] != null) {
                            btnArray[r][c].setBackgroundColor(Color.parseColor(dark_back_col));
                            if (board[r][c] == 'E') setButtonText(r, c, "", black_col);
                            else if (board[r][c] == 'b') setButtonText(r, c, "⬤", black_col);
                            else if (board[r][c] == 'w') setButtonText(r, c, "⬤", white_col);
                            else if (board[r][c] == 'B') setButtonText(r, c, "♛", black_col);
                            else if (board[r][c] == 'W') setButtonText(r, c, "♛", white_col);
                        }
                    }
                }
            }
        }

        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 8; c++) {
                tv[r][c].setBackgroundColor(Color.parseColor(light_back_col));
            }
        }
    }

    void setAnimation(int exR, int exC, int curR, int curC) {
        if (curR == exR - 1 || curR == exR - 2) {
            if (curC == exC - 1 || curC == exC - 2) {
                btnArray[exR][exC].setInAnimation(this, R.anim.in_bottom_right);
                btnArray[exR][exC].setOutAnimation(this, R.anim.out_top_left);
                btnArray[curR][curC].setInAnimation(this, R.anim.in_bottom_right);
                btnArray[curR][curC].setOutAnimation(Main2Activity.this, R.anim.fade_out);
            } else {
                btnArray[exR][exC].setInAnimation(this, R.anim.in_bottom_left);
                btnArray[exR][exC].setOutAnimation(this, R.anim.out_top_right);
                btnArray[curR][curC].setInAnimation(this, R.anim.in_bottom_left);
                btnArray[curR][curC].setOutAnimation(Main2Activity.this, R.anim.fade_out);
            }
        } else if (curR == exR + 1 || curR == exR + 2) {
            if (curC == exC - 1 || curC == exC - 2) {
                btnArray[exR][exC].setInAnimation(this, R.anim.in_top_right);
                btnArray[exR][exC].setOutAnimation(this, R.anim.out_bottom_left);
                btnArray[curR][curC].setInAnimation(this, R.anim.in_top_right);
                btnArray[curR][curC].setOutAnimation(Main2Activity.this, R.anim.fade_out);
            } else {
                btnArray[exR][exC].setInAnimation(this, R.anim.in_top_left);
                btnArray[exR][exC].setOutAnimation(this, R.anim.out_bottom_right);
                btnArray[curR][curC].setInAnimation(this, R.anim.in_top_left);
                btnArray[curR][curC].setOutAnimation(Main2Activity.this, R.anim.fade_out);
            }
        }
        if (curR == exR - 2 || curR == exR + 2) {
            int r = m.getCapt(exR, curR);
            int c = m.getCapt(exC, curC);
            btnArray[r][c].setInAnimation(this, R.anim.fade_in);
            btnArray[r][c].setOutAnimation(this, R.anim.fade_out);
        }
    }
}