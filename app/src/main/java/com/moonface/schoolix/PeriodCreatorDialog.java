package com.moonface.schoolix;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PeriodCreatorDialog extends Dialog implements View.OnClickListener {

    private TextView starting_hour_view;
    private TextView ending_hour_view;
    private OnPeriodCreatedListener listener;
    private Period edit_period;
    private ArrayList<Period> periodsList;
    private int number;
    private int starting_hour;
    private int starting_minute;
    private int ending_hour;
    private int ending_minute;
    private boolean isNew;

    PeriodCreatorDialog(Activity activity, Period period, ArrayList<Period> periodsList, boolean isNew, OnPeriodCreatedListener listener) {
        super(activity);
        this.edit_period = period;
        this.periodsList = periodsList;
        this.listener = listener;
        this.isNew = isNew;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_period_creator);
        Button pos = findViewById(R.id.positiveButton);
        Button neg = findViewById(R.id.negativeButton);
        pos.setOnClickListener(this);
        neg.setOnClickListener(this);
        TextView title_view = findViewById(R.id.title_view);
        starting_hour_view = findViewById(R.id.starting_hour_view);
        starting_hour_view.setOnClickListener(v -> showStartingHourPicker());
        ending_hour_view = findViewById(R.id.ending_hour_view);
        ending_hour_view.setOnClickListener(v -> showEndingHourPicker());
        if(edit_period != null) {
            number = edit_period.getNumber();
            starting_hour = edit_period.getStartingHour();
            starting_minute = edit_period.getStartingMinute();
            ending_hour = edit_period.getEndingHour();
            ending_minute = edit_period.getEndingMinute();
            title_view.setText(getOrdinalNumber(number).concat(getContext().getString(R.string.space_char)).concat(getContext().getString(R.string.period_label)));
            if (Locale.getDefault().getLanguage().equals(getContext().getString(R.string.hebrew_code))){
                title_view.setText(getContext().getString(R.string.period_label).concat(getContext().getString(R.string.space_char)).concat(getOrdinalNumber(number)));
            }
            starting_hour_view.setText(Period.simpleHourFormat.format(starting_hour, starting_minute));
            ending_hour_view.setText(Period.simpleHourFormat.format(ending_hour, ending_minute));
            TextView dialog_title = findViewById(R.id.dialog_title);
            if(isNew){
                dialog_title.setText(R.string.add_period_label);
            } else {
                dialog_title.setText(R.string.edit_period_label);
            }
        }
    }

    private void showStartingHourPicker() {
        TimePickerDialog hourPickerDialog = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            starting_hour = hourOfDay;
            starting_minute = minute;
            starting_hour_view.setText(Period.simpleHourFormat.format(starting_hour, starting_minute));
        }, starting_hour, starting_minute, true);
        hourPickerDialog.show();
    }
    private void showEndingHourPicker() {
        TimePickerDialog hourPickerDialog = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            ending_hour = hourOfDay;
            ending_minute = minute;
            ending_hour_view.setText(Period.simpleHourFormat.format(ending_hour, ending_minute));
        }, ending_hour, ending_minute, true);
        hourPickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.positiveButton:
                boolean canCreate = true;
                String toast = null;
                if(hourIsAfter(starting_hour, starting_minute, ending_hour, ending_minute)){
                    toast = getContext().getString(R.string.ending_hour_clashing_toast);
                    canCreate = false;
                }
                for(int i=0;i<periodsList.size();i++){
                    if(hourIsBefore(starting_hour, starting_minute, periodsList.get(i).getStartingHour(), periodsList.get(i).getStartingMinute()) && hourIsAfter(ending_hour, ending_minute, periodsList.get(i).getEndingHour(), periodsList.get(i).getEndingMinute()) && ((number != periodsList.get(i).getNumber() && periodsList.get(i).getTag().equals(Period.TAG)) || !periodsList.get(i).getTag().equals(Period.TAG)) && canCreate){
                        toast = getContext().getString(R.string.period_clashing_toast);
                        canCreate = false;
                    }
                    if(hourIsAfter(starting_hour, starting_minute, periodsList.get(i).getStartingHour(), periodsList.get(i).getStartingMinute()) && hourIsBefore(starting_hour, starting_minute, periodsList.get(i).getEndingHour(), periodsList.get(i).getEndingMinute()) && ((number != periodsList.get(i).getNumber() && periodsList.get(i).getTag().equals(Period.TAG)) || !periodsList.get(i).getTag().equals(Period.TAG)) && canCreate){
                        toast = getContext().getString(R.string.starting_hour_clashing_toast);
                        canCreate = false;
                    }
                    if(hourIsAfter(ending_hour, ending_minute, periodsList.get(i).getStartingHour(), periodsList.get(i).getStartingMinute()) && hourIsBefore(ending_hour, ending_minute, periodsList.get(i).getEndingHour(), periodsList.get(i).getEndingMinute()) && ((number != periodsList.get(i).getNumber() && periodsList.get(i).getTag().equals(Period.TAG)) || !periodsList.get(i).getTag().equals(Period.TAG)) && canCreate){
                        toast = getContext().getString(R.string.ending_hour_clashing_toast);
                        canCreate = false;
                    }
                }
                if(canCreate) {
                    Period period = new Period(number, Period.TAG);
                    period.setStartingHour(starting_hour);
                    period.setStartingMinute(starting_minute);
                    period.setEndingHour(ending_hour);
                    period.setEndingMinute(ending_minute);
                    listener.onButtonClick(period);
                    dismiss();
                } else {
                    Toast.makeText(getContext(), toast, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.negativeButton:
                dismiss();
                break;
            default:
                dismiss();
                break;
        }
    }

    private String getOrdinalNumber(int number){
        String[] suffixes = getContext().getResources().getStringArray(R.array.suffixes);
        switch (number % 100) {
            case 11:
            case 12:
            case 13:
                return number + suffixes[0];
            default:
                return number + suffixes[number % 10];

        }
    }

    public interface OnPeriodCreatedListener {
        void onButtonClick(Period period);
    }

    private boolean hourIsAfter(int hour1, int minute1, int hour2, int minute2){
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.set(Calendar.HOUR_OF_DAY, hour1);
        cal1.set(Calendar.MINUTE, minute1);
        cal2.set(Calendar.HOUR_OF_DAY, hour2);
        cal2.set(Calendar.MINUTE, minute2);
        return cal1.after(cal2);
    }



    private boolean hourIsBefore(int hour1, int minute1, int hour2, int minute2){
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.set(Calendar.HOUR_OF_DAY, hour1);
        cal1.set(Calendar.MINUTE, minute1);
        cal2.set(Calendar.HOUR_OF_DAY, hour2);
        cal2.set(Calendar.MINUTE, minute2);
        return cal1.before(cal2);
    }
}
