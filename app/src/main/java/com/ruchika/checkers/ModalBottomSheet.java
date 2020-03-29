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

public class ModalBottomSheet extends BottomSheetDialogFragment {

    Button btnP;
    Button btnN;
    View.OnClickListener positive;
    View.OnClickListener negative;

    public ModalBottomSheet() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_restart, container, false);
        btnP = view.findViewById(R.id.btnRestart);
        btnN = view.findViewById(R.id.btnCancel);
        btnP.setOnClickListener(positive);
        btnN.setOnClickListener(negative);
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
