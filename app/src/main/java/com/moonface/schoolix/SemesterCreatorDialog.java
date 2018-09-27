package com.moonface.schoolix;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class SemesterCreatorDialog extends Dialog implements View.OnClickListener {

    private TextView starting_date_view;
    private TextView ending_date_view;
    private OnSemesterCreatedListener listener;
    private Semester edit_semester;
    private ArrayList<Semester> semestersList;
    private int number;
    private Date starting_date;
    private Date ending_date;
    private boolean isNew;

    SemesterCreatorDialog(Activity activity, Semester semester, ArrayList<Semester> semestersList, boolean isNew, OnSemesterCreatedListener listener) {
        super(activity);
        this.edit_semester = semester;
        this.semestersList = semestersList;
        this.listener = listener;
        this.isNew = isNew;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_semester_creator);
        Button pos = findViewById(R.id.positiveButton);
        Button neg = findViewById(R.id.negativeButton);
        pos.setOnClickListener(this);
        neg.setOnClickListener(this);
        TextView title_view = findViewById(R.id.title_view);
        starting_date_view = findViewById(R.id.starting_date_view);
        starting_date_view.setOnClickListener(v -> showStartingDatePicker());
        ending_date_view = findViewById(R.id.ending_date_view);
        ending_date_view.setOnClickListener(v -> showEndingDatePicker());
        if(edit_semester != null) {
            number = edit_semester.getNumber();
            starting_date = edit_semester.getStartingDate();
            ending_date = edit_semester.getEndingDate();
            title_view.setText(getOrdinalNumber(number).concat(getContext().getString(R.string.space_char)).concat(getContext().getString(R.string.semester_label)));
            if(Locale.getDefault().getLanguage().equals(getContext().getString(R.string.hebrew_code))){
                title_view.setText(getContext().getString(R.string.semester_label).concat(getContext().getString(R.string.space_char)).concat(getOrdinalNumber(number)));
            }
            starting_date_view.setText(Semester.dateFormat.format(starting_date));
            ending_date_view.setText(Semester.dateFormat.format(ending_date));
            TextView dialog_title = findViewById(R.id.dialog_title);
            if(isNew){
                dialog_title.setText(R.string.add_semester_label);
            } else {
                dialog_title.setText(R.string.edit_semester_label);
            }
        }
    }

    private void showStartingDatePicker() {
        Calendar now = Calendar.getInstance();
        if(starting_date != null){
            now.setTime(starting_date);
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, monthOfYear, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            starting_date = calendar.getTime();
            starting_date_view.setText(Semester.dateFormat.format(starting_date));
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    private void showEndingDatePicker() {
        Calendar now = Calendar.getInstance();
        if(ending_date != null){
            now.setTime(ending_date);
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, monthOfYear, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            ending_date = calendar.getTime();
            ending_date_view.setText(Semester.dateFormat.format(ending_date));
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.positiveButton:
                boolean canCreate = true;
                String toast = null;
                if(starting_date == null) {
                    toast = getContext().getString(R.string.pick_starting_date_toast);
                    canCreate = false;
                }
                if(ending_date == null && canCreate) {
                    toast = getContext().getString(R.string.pick_ending_date_toast);
                    canCreate = false;
                }
                if(Objects.requireNonNull(starting_date).after(ending_date) && canCreate){
                    toast = getContext().getString(R.string.ending_date_after_starting_date_toast);
                    canCreate = false;
                }
                for(int i=0;i<semestersList.size();i++){
                    if((starting_date.before(semestersList.get(i).getStartingDate()) || datesAreEqual(starting_date, semestersList.get(i).getStartingDate())) && (ending_date.after(semestersList.get(i).getEndingDate()) || datesAreEqual(ending_date, semestersList.get(i).getEndingDate())) && (number != semestersList.get(i).getNumber()) && canCreate){
                        toast = getContext().getString(R.string.semester_clashing_toast);
                        canCreate = false;
                    }
                    if((starting_date.after(semestersList.get(i).getStartingDate()) || datesAreEqual(starting_date, semestersList.get(i).getStartingDate())) && (starting_date.before(semestersList.get(i).getEndingDate()) || datesAreEqual(starting_date, semestersList.get(i).getEndingDate())) && (number != semestersList.get(i).getNumber()) && canCreate){
                        toast = getContext().getString(R.string.starting_date_clashing);
                        canCreate = false;
                    }
                    if((ending_date.after(semestersList.get(i).getStartingDate()) || datesAreEqual(ending_date, semestersList.get(i).getStartingDate())) && (ending_date.before(semestersList.get(i).getEndingDate()) || datesAreEqual(ending_date, semestersList.get(i).getEndingDate())) && number != semestersList.get(i).getNumber() && canCreate){
                        toast = getContext().getString(R.string.ending_date_clashing);
                        canCreate = false;
                    }
                }
                if(canCreate){
                    Semester semester = new Semester(number);
                    semester.setStartingDate(starting_date);
                    semester.setEndingDate(ending_date);
                    listener.onButtonClick(semester);
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

    private boolean datesAreEqual(Date date1, Date date2){
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    public interface OnSemesterCreatedListener {
        void onButtonClick(Semester semester);
    }
}
