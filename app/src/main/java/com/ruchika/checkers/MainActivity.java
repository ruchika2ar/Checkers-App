package com.ruchika.checkers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.ruchika.checkers.Constants.Constants;

public class MainActivity extends AppCompatActivity {
    LinearLayout llModes;
    ImageButton btnPlay, btnRules, btnVolume, btnTwoP, btnComp, btnBack;
    public static final String MAIN_STATE =
            "EwEwEwEw" + "wEwEwEwE" + "EwEwEwEw" +
                    "EEEEEEEE" + "EEEEEEEE" +
                    "bEbEbEbE" + "EbEbEbEb" + "bEbEbEbE";
    Boolean isMuted = false;
    Integer curVolume = 0;
    SeekBar seekBarLevels;
    int level = 0;
    int curRule = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        btnPlay = findViewById(R.id.btnPlay);
        btnRules = findViewById(R.id.btnRules);
        btnVolume = findViewById(R.id.btnVolume);
        btnTwoP = findViewById(R.id.btnTwoP);
        btnComp = findViewById(R.id.btnComp);
        llModes = findViewById(R.id.llMode);
        btnBack = findViewById(R.id.btnBack);
        seekBarLevels = findViewById(R.id.seekBarLevels);
        btnBack.setVisibility(View.GONE);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPlay.setVisibility(View.GONE);
                llModes.setVisibility(View.VISIBLE);
                btnTwoP.setVisibility(View.VISIBLE);
                btnComp.setVisibility(View.VISIBLE);
                btnBack.setVisibility(View.VISIBLE);
                btnBack.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in));
            }
        });

        btnTwoP.setOnClickListener(new MyOnClickListener(true));
        btnComp.setOnClickListener(new MyOnClickListener(false));
        btnVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
                if (am != null) {
                    if (!isMuted) {
                        btnVolume.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_first_half));
                        btnVolume.setImageResource(R.drawable.ic_volume_off);
                        btnVolume.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_rest_half));
                        curVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                        am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                        isMuted = true;
                    } else {
                        btnVolume.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_first_half));
                        btnVolume.setImageResource(R.drawable.ic_volume_up);
                        btnVolume.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_rest_half));
                        am.setStreamVolume(AudioManager.STREAM_MUSIC, curVolume, 0);
                        isMuted = false;
                    }
                }
            }
        });

        btnRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curRule = 0;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View view = getLayoutInflater().inflate(R.layout.rules, null, false);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                final ImageButton btnPrev, btnNext, btnHideDialog;
                btnPrev = view.findViewById(R.id.btnPrev);
                btnNext = view.findViewById(R.id.btnNext);
                btnHideDialog = view.findViewById(R.id.btnHideDialog);
                final TextSwitcher tvRule = view.findViewById(R.id.tvRule);
                tvRule.setFactory(new ViewSwitcher.ViewFactory() {
                    @Override
                    public View makeView() {
                        TextView tv = new TextView(MainActivity.this);
                        tv.setTextColor(Color.parseColor("#2c2c2c"));
                        tv.setTextSize(17);
                        tv.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                        return tv;
                    }
                });
                final TextView tvHeading = view.findViewById(R.id.tvHeading);
                tvRule.setText(Constants.rules[0]);
                tvHeading.setText(Constants.heading_rules[0]);
                btnPrev.setVisibility(View.GONE);
                btnPrev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (curRule - 1 >= 0) {
                            tvRule.setInAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));
                            curRule--;
                            String prev = Constants.rules[curRule];
                            String heading = Constants.heading_rules[curRule];
                            tvHeading.setText(heading);
                            tvRule.setText(prev);
                            if (curRule == 7) btnNext.setVisibility(View.GONE);
                            else btnNext.setVisibility(View.VISIBLE);
                            if (curRule > 0) btnPrev.setVisibility(View.VISIBLE);
                            else btnPrev.setVisibility(View.GONE);
                        }
                    }
                });
                btnNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (curRule + 1 <= 7) {
                            tvRule.setInAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_left));
                            curRule++;
                            String next = Constants.rules[curRule];
                            String heading = Constants.heading_rules[curRule];
                            tvHeading.setText(heading);
                            tvRule.setText(next);
                            if (curRule > 0) btnPrev.setVisibility(View.VISIBLE);
                            else btnPrev.setVisibility(View.GONE);
                            if (curRule == 7) btnNext.setVisibility(View.GONE);
                            else btnNext.setVisibility(View.VISIBLE);
                        }
                    }
                });
                btnHideDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.hide();
                    }
                });
                dialog.show();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnBack.getVisibility() == View.VISIBLE) {
                    btnPlay.setVisibility(View.VISIBLE);
                    llModes.setVisibility(View.GONE);
                    btnTwoP.setVisibility(View.GONE);
                    btnComp.setVisibility(View.GONE);
                    btnBack.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out));
                    btnBack.setVisibility(View.GONE);
                }
            }
        });
    }

    class MyOnClickListener implements View.OnClickListener {
        Boolean twoP;

        public MyOnClickListener(Boolean twoP) {
            this.twoP = twoP;
        }

        @Override
        public void onClick(View v) {
            final SharedPreferences sp;
            String spName = twoP ? Constants.TWO_PLAYERS : Constants.COMPUTER;
            sp = getSharedPreferences(spName, MODE_PRIVATE);
            if (!sp.getString(Constants.STATE, MAIN_STATE).equals(MAIN_STATE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View view = getLayoutInflater().inflate(R.layout.dialog_main, null, false);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.show();
                Button btnNewGame = dialog.findViewById(R.id.btnNegative);
                Button btnContinue = dialog.findViewById(R.id.btnPositive);

                btnNewGame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString(Constants.STATE, MAIN_STATE);
                        editor.apply();
                        startGameActivity(twoP);
                        dialog.hide();
                    }
                });

                btnContinue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startGameActivity(twoP);
                        dialog.hide();
                    }
                });
            } else {
                startGameActivity(twoP);
            }
        }
    }

    void startGameActivity(boolean twoP) {
        Intent i = new Intent(MainActivity.this, Main2Activity.class);
        i.putExtra("twoP", twoP);
        if (!twoP) {
            i.putExtra("level", level);
        }
        startActivity(i);
    }
}
