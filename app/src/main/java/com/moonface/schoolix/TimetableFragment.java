package com.moonface.schoolix;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Objects;

public class TimetableFragment extends Fragment {

    private ArrayList<TextView> dayButtons;
    private RecyclerView hour_list;
    private LinearLayout days_layout;
    private int day;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timetable, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        FloatingActionButton add_class = view.findViewById(R.id.add_class);
        add_class.setOnClickListener(v -> {
            if(getContext() != null) {
                ClassCreatorDialog classCreatorDialog = new ClassCreatorDialog(getActivity(), Objects.requireNonNull(getActivity()).getSupportFragmentManager(), day, lesson ->
                {
                    DataManager.putClass(getContext(),getString(R.string.timetable_data_preferences), getString(R.string.d)+String.valueOf(lesson.getDay())+getString(R.string.h)+String.valueOf(lesson.getHour()), lesson);
                    NotificationManager.startClassAlarm(getContext(), lesson, DataManager.getPeriod(getContext(), getString(R.string.period_data_preferences), lesson.getHour()));
                    changeDay(lesson.getDay());
                });
                classCreatorDialog.show();
            }
        });
        dayButtons = new ArrayList<>();
        dayButtons.add(view.findViewById(R.id.Monday));
        dayButtons.add(view.findViewById(R.id.Tuesday));
        dayButtons.add(view.findViewById(R.id.Wednesday));
        dayButtons.add(view.findViewById(R.id.Thursday));
        dayButtons.add(view.findViewById(R.id.Friday));
        dayButtons.add(view.findViewById(R.id.Saturday));
        dayButtons.add(view.findViewById(R.id.Sunday));
        View.OnClickListener dayPressed = v -> changeDay(dayButtons.indexOf(v));
        for (TextView t: dayButtons) {
            t.setOnClickListener(dayPressed);
        }
        days_layout = view.findViewById(R.id.days_layout);
        int d;
        switch(DataManager.getIntPref(Objects.requireNonNull(getContext()), getString(R.string.general_data_preferences), getString(R.string.first_day_key))){
            case 0:
                if(Calendar.getInstance().getFirstDayOfWeek() != Calendar.SUNDAY){
                    d = Calendar.getInstance().getFirstDayOfWeek()-2;
                } else {
                    d = 6;
                }
                break;
            case 1:
                d=0;
                break;
            case 2:
                d=6;
                break;
            default:
                d=0;
                break;
        }
        setFirstDayOfWeek(d);
        hour_list = view.findViewById(R.id.hour_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        hour_list.setLayoutManager(layoutManager);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if(day != Calendar.SUNDAY) {
            changeDay(day-2);
        } else{
            changeDay(6);
        }
    }

    private void setFirstDayOfWeek(int day){
        days_layout.removeAllViews();
        int d=day;
        int i=0;
        while (i<7){
            days_layout.addView(dayButtons.get(d),i);
            d++;
            if(d==7){
                d=0;
            }
            i++;
        }
    }

    private ArrayList<Class> getData(int day) {
        ArrayList<Class> output = new ArrayList<>();
        if(getContext() != null) {
            ArrayList<Class> timetableData = DataManager.getClassesArray(getContext(), getString(R.string.timetable_data_preferences));
            ArrayList<String> namesList = DataManager.getObjectsNamesList(getContext(), getString(R.string.timetable_data_preferences));
            if (timetableData != null) {
                for(int i = 0; i<timetableData.size();i++){
                    if(namesList.get(i).substring(namesList.get(i).indexOf(getString(R.string.d)) + 1, namesList.get(i).indexOf(getString(R.string.d)) + 2).equals(String.valueOf(day))){
                        Class lesson = new Class(day, Integer.parseInt(namesList.get(i).substring(namesList.get(i).indexOf(getString(R.string.h)) + 1)));
                        Class map = timetableData.get(i);
                        lesson.setSubject(map.getSubject());
                        output.add(lesson);
                    }
                }
            }
        }
        return output;
    }

    private void changeDay(int day){
        this.day = day;
        for (TextView t:dayButtons) {
            if(getActivity() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        t.setBackground(getActivity().getDrawable(R.drawable.day_item_bg));
                    }
                } else {
                    t.setBackground(getActivity().getResources().getDrawable(R.drawable.day_item_bg));
                }
            }
        }
        if(getActivity() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dayButtons.get(day).setBackground(getActivity().getDrawable(R.drawable.day_item_bg_selected));
                }
            } else {
                dayButtons.get(day).setBackground(getActivity().getResources().getDrawable(R.drawable.day_item_bg_selected));
            }
        }
        ArrayList<Class> data = getData(day);
        Collections.sort(data, (o1, o2) -> o1.getHour() - o2.getHour());
        if(data.size() > 0) {
            int lasthour = data.get(data.size() - 1).getHour();
            for (int i = 1; i < lasthour; i++) {
                if(DataManager.getClass(Objects.requireNonNull(getContext()), getString(R.string.timetable_data_preferences), getString(R.string.d)+String.valueOf(day)+getString(R.string.h)+String.valueOf(i)) == null){
                    data.add(new Class(day,i));
                }
            }
            Collections.sort(data, (o1, o2) -> o1.getHour() - o2.getHour());
        }
        RecyclerView.Adapter<TableAdapter.ViewHolder> adapter = new TableAdapter(data, getContext(), item -> {
            if(getContext() != null) {
                Intent intent = new Intent();
                intent.setClass(getContext(), ClassViewActivity.class);
                if(item.getSubject() != null) {
                    intent.putExtra(getString(R.string.name_key), getString(R.string.d)+String.valueOf(item.getDay())+getString(R.string.h)+String.valueOf(item.getHour()));
                    startActivity(intent);
                }
            }
        });
        hour_list.setAdapter(adapter);
    }
    @Override
    public void onStart(){
        super.onStart();
        changeDay(day);
        int d;
        switch(DataManager.getIntPref(Objects.requireNonNull(getContext()), getString(R.string.general_data_preferences), getString(R.string.first_day_key))){
            case 0:
                if(Calendar.getInstance().getFirstDayOfWeek() != Calendar.SUNDAY){
                    d = Calendar.getInstance().getFirstDayOfWeek()-2;
                } else {
                    d = 6;
                }
                break;
            case 1:
                d=0;
                break;
            case 2:
                d=6;
                break;
            default:
                d=0;
                break;
        }
        setFirstDayOfWeek(d);
    }
}
