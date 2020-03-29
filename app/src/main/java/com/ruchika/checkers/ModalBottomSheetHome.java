package com.ruchika.checkers;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

public class ModalBottomSheetHome extends BottomSheetDialogFragment {
    Button btnP;
    Button btnN;
    CheckBox cbSave;
    View.OnClickListener positive;
    View.OnClickListener negative;
    View.OnClickListener cbListener;

    public ModalBottomSheetHome() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_home, container, false);
        btnP = view.findViewById(R.id.btnHome);
        btnN = view.findViewById(R.id.btnCancel2);
        cbSave = view.findViewById(R.id.cbSave);
        btnP.setOnClickListener(positive);
        btnN.setOnClickListener(negative);
        cbSave.setOnClickListener(cbListener);
        Button btnSaveGame = view.findViewById(R.id.btnSaveGame);
        btnSaveGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbSave.toggle();
            }
        });
        return view;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        btnN.performClick();
        super.onCancel(dialog);
    }

    void setListeners(View.OnClickListener p, View.OnClickListener n) {
        this.positive = p;
        this.negative = n;
    }
}

