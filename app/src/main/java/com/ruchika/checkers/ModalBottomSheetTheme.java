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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ruchika.checkers.Constants.Constants;

public class ModalBottomSheetTheme extends BottomSheetDialogFragment {
    RadioButton rbDefault, rbClassic, rbDark;
    Button btCancel, btApply;
    RadioGroup rgTheme;
    View.OnClickListener apply, cancel;
    int currentTheme;

    public ModalBottomSheetTheme() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modal_sheet_theme, container, false);
        btApply = view.findViewById(R.id.btApply);
        btCancel = view.findViewById(R.id.btCancel);
        rbDefault =view.findViewById(R.id.rbDefault);
        rbClassic = view.findViewById(R.id.rbClassic);
        rbDark = view.findViewById(R.id.rbDark);
        rgTheme = view.findViewById(R.id.rgTheme);
        btApply.setOnClickListener(apply);
        btCancel.setOnClickListener(cancel);
        rgTheme.check(Constants.radioButton[currentTheme]);
        return view;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        btCancel.performClick();
        super.onCancel(dialog);
    }

    void setCurrentTheme (int theme) {
        this.currentTheme = theme;
    }

    void setOnClickListeners (View.OnClickListener apply, View.OnClickListener cancel) {
        this.apply = apply;
        this.cancel = cancel;
    }
}
