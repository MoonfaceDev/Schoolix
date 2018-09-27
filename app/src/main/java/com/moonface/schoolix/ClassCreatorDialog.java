package com.moonface.schoolix;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class ClassCreatorDialog extends Dialog implements View.OnClickListener, NumberPicker.OnValueChangeListener {

    private FragmentManager fragmentManager;
    private int hour = 1;
    private int day = 0;
    private int dayIndex;
    private Subject subject;
    private Drawable bg;
    private String[] days;
    private HourPickerDialog hourPickerDialog;
    private Activity activity;
    private Class edit_lesson;

    private TextView subject_view;
    private TextView day_view;
    private TextView hour_view;
    private LinearLayout image;
    private TextView letter;
    private OnSubjectCreatedListener listener;

    ClassCreatorDialog(Activity activity, FragmentManager fragmentManager, int day, OnSubjectCreatedListener listener) {
        super(activity);
        this.listener = listener;
        this.day = day;
        this.fragmentManager = fragmentManager;
        this.activity = activity;
        switch (DataManager.getIntPref(getContext(), getContext().getString(R.string.general_data_preferences), getContext().getString(R.string.first_day_key))){
            case 0:
                dayIndex = day+Calendar.getInstance().getFirstDayOfWeek();
                break;
            case 1:
                dayIndex = day;
                break;
            case 2:
                dayIndex = day-1;
                break;
            default:
                dayIndex = day;
                break;
        }
        if(dayIndex < 0){
            dayIndex += 7;
        }
        if(dayIndex > 6){
            dayIndex -= 7;
        }
    }

    ClassCreatorDialog(Activity activity, FragmentManager fragmentManager, Class lesson, OnSubjectCreatedListener listener) {
        super(activity);
        this.listener = listener;
        this.edit_lesson = lesson;
        this.fragmentManager = fragmentManager;
        this.activity = activity;
        switch (DataManager.getIntPref(getContext(), getContext().getString(R.string.general_data_preferences), getContext().getString(R.string.first_day_key))){
            case 0:
                dayIndex = edit_lesson.getDay()+Calendar.getInstance().getFirstDayOfWeek();
                break;
            case 1:
                dayIndex = edit_lesson.getDay();
                break;
            case 2:
                dayIndex = edit_lesson.getDay()-1;
                break;
            default:
                dayIndex = edit_lesson.getDay();
                break;
        }
        if(dayIndex < 0){
            dayIndex += 7;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_class_creator);
        Button pos = findViewById(R.id.positiveButton);
        Button neg = findViewById(R.id.negativeButton);
        pos.setOnClickListener(this);
        neg.setOnClickListener(this);
        days = getContext().getResources().getStringArray(R.array.days);
        int d;
        switch (DataManager.getIntPref(getContext(), getContext().getString(R.string.general_data_preferences), getContext().getString(R.string.first_day_key))){
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
        int i=0;
        String[] temp = new String[7];
        while (i<7){
            temp[i] = days[d];
            d++;
            if(d==7){
                d=0;
            }
            i++;
        }
        days = temp;
        hourPickerDialog = new HourPickerDialog();
        subject_view = findViewById(R.id.subject_view);
        subject_view.setOnClickListener(v -> showSubjectPicker());
        day_view = findViewById(R.id.day_view);
        day_view.setOnClickListener(v -> showDayPicker());
        hour_view = findViewById(R.id.hour_view);
        hour_view.setOnClickListener(v -> showNumberPicker(hour));
        day_view.setText(days[dayIndex]);
        hour_view.setText(getContext().getString(R.string.hour_label).concat(getContext().getString(R.string.space_char)).concat(String.valueOf(hour)));
        image = findViewById(R.id.image);
        letter = findViewById(R.id.letter);
        if(edit_lesson != null){
            subject = edit_lesson.getSubject();
            bg = getContext().getResources().getDrawable(Subject.ids[subject.getDrawableIndex()]);
            subject_view.setText(subject.getTitle());
            image.setBackground(bg);
            letter.setText(String.valueOf(subject.getTitle().charAt(0)));
            day = edit_lesson.getDay();
            hour = edit_lesson.getHour();
            day_view.setText(days[dayIndex]);
            hour_view.setText(getContext().getString(R.string.hour_label).concat(getContext().getString(R.string.space_char)).concat(String.valueOf(hour)));
            TextView dialog_title = findViewById(R.id.dialog_title);
            dialog_title.setText(R.string.edit_class_label);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.positiveButton:
                boolean canCreate = true;
                String toast = null;
                if(subject == null) {
                    toast = getContext().getString(R.string.pick_subject_toast);
                    canCreate = false;
                }
                if(day > 6 && canCreate){
                    toast = getContext().getString(R.string.pick_day_toast);
                    canCreate = false;
                }
                if(hour == 0 && canCreate) {
                    toast = getContext().getString(R.string.pick_hour_toast);
                    canCreate = false;
                }
                for(Class c : DataManager.getClassesArray(getContext(), getContext().getString(R.string.timetable_data_preferences))){
                    if(c.getDay() == day && c.getHour() == hour && canCreate){
                        toast = getContext().getString(R.string.class_clashing_toast);
                    }
                }
                if(hour > getLastPeriod() && canCreate){
                    toast = getContext().getString(R.string.class_outside_timetable_toast);
                    canCreate = false;
                }
                if(canCreate){
                    Class lesson = new Class(day, hour);
                    lesson.setSubject(subject);
                    listener.onButtonClick(lesson);
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
    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        hour = numberPicker.getValue();
        hour_view.setText(getContext().getString(R.string.hour_label).concat(getContext().getString(R.string.space_char)).concat(String.valueOf(hour)));
    }
    private void showSubjectPicker(){
        ArrayList<Subject> subjectList = DataManager.getSubjectsArray(getContext(), getContext().getString(R.string.subject_data_preferences));
        CharSequence[] subjects = new CharSequence[subjectList.size()+1];
        SpannableString boldOption = new SpannableString(getContext().getString(R.string.create_new_subject_label));
        boldOption.setSpan(new StyleSpan(Typeface.BOLD), 0, (getContext().getString(R.string.create_new_subject_label)).length(),0);
        subjects[0] = boldOption;
        for(int i=1;i<subjects.length;i++){
            subjects[i] = subjectList.get(i-1).getTitle();
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(subjects, (dialog, which) -> {
            if(which == 0){
                showSubjectCreator();
            } else {
                subject = subjectList.get(which-1);
                bg = getContext().getResources().getDrawable(Subject.ids[subjectList.get(which-1).getDrawableIndex()]);
                subject_view.setText(subject.getTitle());
                image.setBackground(bg);
                letter.setText(String.valueOf(subject.getTitle().charAt(0)));
            }
        });
        builder.show();
    }

    private void showSubjectCreator() {
        SubjectCreatorDialog subjectCreatorDialog = new SubjectCreatorDialog(activity, (Subject map) -> {
            DataManager.putSubject(Objects.requireNonNull(getContext()), getContext().getString(R.string.subject_data_preferences), map.getTitle(), map);
            subject = map;
            bg = getContext().getResources().getDrawable(Subject.ids[map.getDrawableIndex()]);
            subject_view.setText(subject.getTitle());
            image.setBackground(bg);
            letter.setText(String.valueOf(subject.getTitle().charAt(0)));
        });
        subjectCreatorDialog.show();
    }

    private void showNumberPicker(int number){
        hourPickerDialog.setMax(getLastPeriod());
        hourPickerDialog.setNumber(number);
        hourPickerDialog.setValueChangeListener(this);
        hourPickerDialog.show(fragmentManager, getContext().getString(R.string.hour_picker_tag));
    }
    private void showDayPicker(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(days, (dialog, which) -> {
            switch (DataManager.getIntPref(getContext(), getContext().getString(R.string.general_data_preferences), getContext().getString(R.string.first_day_key))){
                case 0:
                    if(Calendar.getInstance().getFirstDayOfWeek() != Calendar.SUNDAY){
                        day = which + Calendar.getInstance().getFirstDayOfWeek() - 2;
                    } else {
                        day = which + Calendar.getInstance().getFirstDayOfWeek() - 2;
                        if(day == -1){
                            day = 6;
                        }
                    }
                    break;
                case 1:
                    day = which;
                    break;
                case 2:
                    day = which - 1;
                    if(day == -1){
                        day = 6;
                    }
                    break;
                default:
                    day = which;
                    break;
            }
            day_view.setText(days[which]);
        });
        builder.show();
    }

    public interface OnSubjectCreatedListener {
        void onButtonClick(Class lesson);
    }

    private int getLastPeriod(){
        ArrayList<Period> periods = DataManager.getPeriodsArray(Objects.requireNonNull(getContext()), getContext().getString(R.string.period_data_preferences));
        int output=0;
        for(int i=0;i<periods.size();i++){
            if(periods.get(i).getNumber() > output){
                output = periods.get(i).getNumber();
            }
        }
        return output;
    }
}
