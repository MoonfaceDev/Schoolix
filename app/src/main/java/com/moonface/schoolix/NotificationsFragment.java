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

public class NotificationsFragment extends Fragment {
    private Switch daily_switch, timetable_switch;
    private TextView daily_time_view;
    private int daily_hour =18, daily_minute =0;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.setup_notifications, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        daily_switch = view.findViewById(R.id.daily_switch);
        daily_time_view = view.findViewById(R.id.daily_time_view);
        daily_time_view.setOnClickListener(v -> showTimePicker());
        daily_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                daily_time_view.setVisibility(View.VISIBLE);
            } else {
                daily_time_view.setVisibility(View.GONE);
            }
        });
        daily_time_view.setText(Period.simpleHourFormat.format(daily_hour, daily_minute));
        timetable_switch = view.findViewById(R.id.timetable_switch);
    }
    private void showTimePicker() {
        TimePickerDialog hourPickerDialog = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            this.daily_hour = hourOfDay;
            this.daily_minute = minute;
            daily_time_view.setText(Period.simpleHourFormat.format(hourOfDay, minute));
        }, daily_hour, daily_minute, true);
        hourPickerDialog.show();
    }
    public boolean isDailyChecked() {
        return daily_switch.isChecked();
    }
    public boolean isTimetableChecked() {
        return timetable_switch.isChecked();
    }
    public int getDaily_hour(){
        return daily_hour;
    }
    public int getDaily_minute(){
        return daily_minute;
    }
}
