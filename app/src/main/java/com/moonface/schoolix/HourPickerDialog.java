package com.moonface.schoolix;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.NumberPicker;

public class HourPickerDialog extends DialogFragment {
    private NumberPicker.OnValueChangeListener valueChangeListener;
    private NumberPicker numberPicker;
    private int number = 1;
    private int max = 1;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        numberPicker = new NumberPicker(getActivity());
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(max);
        numberPicker.setValue(number);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.class_hour_label);
        builder.setPositiveButton(R.string.ok_label, (dialog, which) -> valueChangeListener.onValueChange(numberPicker, numberPicker.getValue(), numberPicker.getValue()));
        builder.setNegativeButton(R.string.cancel_label, (dialog, which) -> {});

        builder.setView(numberPicker);
        return builder.create();
    }

    public void setValueChangeListener(NumberPicker.OnValueChangeListener valueChangeListener) {
        this.valueChangeListener = valueChangeListener;
    }

    public void setNumber(int number){
        this.number = number;
    }

    public void setMax(int max){
        this.max = max;
    }
}
