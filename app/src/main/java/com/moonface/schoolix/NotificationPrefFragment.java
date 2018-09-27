package com.moonface.schoolix;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class NotificationPrefFragment extends Fragment {
    private TextView time_view;
    private int hour=18, minute=0;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification_pref, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle){
        Switch timetable_switch = view.findViewById(R.id.timetable_switch);
        timetable_switch.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if(isChecked) {
                DataManager.putBooleanPref(Objects.requireNonNull(getContext()), getString(R.string.general_data_preferences), getString(R.string.timetable_notification_key), true);
                ArrayList<Class> classes = DataManager.getClassesArray(getContext(), getString(R.string.timetable_data_preferences));
                for(Class lesson : classes){
                    Period period = DataManager.getPeriod(getContext(), getString(R.string.period_data_preferences), lesson.getHour());
                    NotificationManager.startClassAlarm(getContext(), lesson, period);
                }
            } else {
                DataManager.putBooleanPref(Objects.requireNonNull(getContext()), getString(R.string.general_data_preferences), getString(R.string.timetable_notification_key), false);
                ArrayList<Class> classes = DataManager.getClassesArray(getContext(), getString(R.string.timetable_data_preferences));
                for(Class lesson : classes){
                    NotificationManager.cancelClassAlarm(getContext(), lesson);
                }
            }
        }));
        Switch daily_switch = view.findViewById(R.id.daily_switch);
        time_view  = view.findViewById(R.id.time_view);
        time_view.setOnClickListener(v -> showTimePicker());
        hour = DataManager.getIntPref(Objects.requireNonNull(getContext()), getString(R.string.general_data_preferences), getString(R.string.notification_hour_key));
        minute = DataManager.getIntPref(getContext(), getString(R.string.general_data_preferences), getString(R.string.notification_minute_key));
        daily_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                time_view.setVisibility(View.VISIBLE);
                DataManager.putBooleanPref(getContext(), getString(R.string.general_data_preferences), getString(R.string.daily_notification_key), true);
                DataManager.putIntPref(getContext(), getString(R.string.general_data_preferences), getString(R.string.notification_hour_key), hour);
                DataManager.putIntPref(getContext(), getString(R.string.general_data_preferences), getString(R.string.notification_minute_key), minute);
                NotificationManager.startDailyAlarm(getContext(), hour, minute);
            } else {
                DataManager.putBooleanPref(getContext(), getString(R.string.general_data_preferences), getString(R.string.daily_notification_key), false);
                time_view.setVisibility(View.GONE);
                NotificationManager.cancelDailyAlarm(getContext());
            }
        });
        daily_switch.setChecked(DataManager.getBooleanPref(Objects.requireNonNull(getContext()), getString(R.string.general_data_preferences), getString(R.string.daily_notification_key)));
        time_view.setText(Period.simpleHourFormat.format(hour, minute));
    }
    private void showTimePicker() {
        TimePickerDialog hourPickerDialog = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            this.hour = hourOfDay;
            this.minute = minute;
            time_view.setText(Period.simpleHourFormat.format(hourOfDay, minute));
            DataManager.putIntPref(Objects.requireNonNull(getContext()), getString(R.string.general_data_preferences), getString(R.string.notification_hour_key), hour);
            DataManager.putIntPref(getContext(), getString(R.string.general_data_preferences), getString(R.string.notification_minute_key), minute);
            NotificationManager.startDailyAlarm(getContext(), hour, minute);
        }, hour, minute, true);
        hourPickerDialog.show();
    }
}
