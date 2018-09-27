package com.moonface.schoolix;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class GradeCreatorDialog extends Dialog implements View.OnClickListener {

    private Activity activity;
    public LinearLayout image;
    private EditText title_view, weight_view, grade_view;
    public TextView subject_view, date_view, type_view, letter;
    private OnGradeCreatedListener listener;
    private Grade edit_grade;
    private Subject subject;
    private String title;
    private Date date;
    private String type;
    private float weight;
    private float grade;

    GradeCreatorDialog(Activity activity, OnGradeCreatedListener listener) {
        super(activity);
        this.activity = activity;
        this.listener = listener;
    }
    GradeCreatorDialog(Activity activity, Grade grade, OnGradeCreatedListener listener) {
        super(activity);
        this.activity = activity;
        this.edit_grade = grade;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_grade_creator);
        Button pos = findViewById(R.id.positiveButton);
        Button neg = findViewById(R.id.negativeButton);
        pos.setOnClickListener(this);
        neg.setOnClickListener(this);
        image = findViewById(R.id.image);
        letter = findViewById(R.id.letter);
        title_view = findViewById(R.id.title_view);
        weight_view = findViewById(R.id.weight_view);
        grade_view = findViewById(R.id.grade_view);
        switch (DataManager.getIntPref(getContext(), getContext().getString(R.string.general_data_preferences), getContext().getString(R.string.grading_key))){
            case 0:
                grade_view.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                break;
            case 1:
                grade_view.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                break;
            case 2:
                grade_view.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                break;
        }
        subject_view = findViewById(R.id.subject_view);
        subject_view.setOnClickListener(v -> showSubjectPicker());
        date_view = findViewById(R.id.date_view);
        date_view.setOnClickListener(v -> showDatePicker());
        type_view = findViewById(R.id.type_view);
        type_view.setOnClickListener(v -> showTypePicker());
        if(edit_grade != null) {
            title = edit_grade.getTitle();
            subject = edit_grade.getSubject();
            date = edit_grade.getDate();
            weight = edit_grade.getWeight();
            grade = edit_grade.getGrade();
            type = edit_grade.getType();
            title_view.setText(title);
            subject_view.setText(subject.getTitle());
            date_view.setText(Grade.dateFormat.format(date));
            weight_view.setText(Grade.format(Grade.Grading.ONE_TO_HUNDRED, weight));
            grade_view.setText(Grade.format(edit_grade.getGrading(), grade));
            type_view.setText(type);
            TextView dialog_title = findViewById(R.id.dialog_title);
            dialog_title.setText(R.string.edit_grade_label);
            Drawable bg = getContext().getResources().getDrawable(Subject.ids[subject.getDrawableIndex()]);
            image.setBackground(bg);
            letter.setText(String.valueOf(subject.getTitle().charAt(0)));
        }
    }

    private void showTypePicker() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        CharSequence[] types = getContext().getResources().getStringArray(R.array.grade_types);
        builder.setItems(types, (dialog, which) -> {
            type = types[which].toString();
            type_view.setText(type);
        });
        builder.show();
    }

    private void showDatePicker() {
        Calendar now = Calendar.getInstance();
        if(date != null){
            now.setTime(date);
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, monthOfYear, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            date = calendar.getTime();
            date_view.setText(Grade.dateFormat.format(date));
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.positiveButton:
                boolean canCreate = true;
                String toast = null;
                if (title_view.getText().length() == 0) {
                    toast = getContext().getString(R.string.title_empty_toast);
                    canCreate = false;
                }
                if (subject == null && canCreate) {
                    toast = getContext().getString(R.string.pick_subject_toast);
                    canCreate = false;
                }
                if (date == null && canCreate) {
                    toast = getContext().getString(R.string.set_date_toast);
                    canCreate = false;
                }
                if (weight_view.getText().length() == 0 && canCreate) {
                    toast = getContext().getString(R.string.set_weight_toast);
                    canCreate = false;
                }
                if (grade_view.getText().length() == 0 && canCreate) {
                    toast = getContext().getString(R.string.grade_empty_toast);
                    canCreate = false;
                }
                if (type == null && canCreate) {
                    toast = getContext().getString(R.string.pick_type_toast);
                    canCreate = false;
                }
                if (!InputManager.isValid(weight_view.getText().toString(), InputManager.ValidType.Digits) && canCreate) {
                    toast = getContext().getString(R.string.weight_contain_digits);
                    canCreate = false;
                }
                switch (DataManager.getIntPref(getContext(), getContext().getString(R.string.general_data_preferences), getContext().getString(R.string.grading_key))) {
                    case 0:
                        if (!InputManager.isValid(grade_view.getText().toString(), InputManager.ValidType.Digits) && canCreate) {
                            toast = getContext().getString(R.string.grade_contain_numbers);
                            canCreate = false;
                        }
                        if (Float.parseFloat(grade_view.getText().toString()) < 0f || Float.parseFloat(grade_view.getText().toString()) > 100f) {
                            toast = getContext().getString(R.string.grade_between_0_100);
                            canCreate = false;
                        }
                        break;
                    case 1:
                        if (!InputManager.isValid(grade_view.getText().toString(), InputManager.ValidType.Digits) && canCreate) {
                            toast = getContext().getString(R.string.grade_contain_numbers);
                            canCreate = false;
                        }
                        if (Float.parseFloat(grade_view.getText().toString()) < 0f || Float.parseFloat(grade_view.getText().toString()) > 10f) {
                            toast = getContext().getString(R.string.grade_between_0_10);
                            canCreate = false;
                        }
                        break;
                    case 2:
                        switch (grade_view.getText().toString().length()) {
                            case 1:
                                if (!InputManager.isValid(grade_view.getText().toString().toCharArray()[0], new char[]{'A', 'B', 'C', 'D', 'F'}) && canCreate) {
                                    toast = getContext().getString(R.string.grade_between_F_A);
                                    canCreate = false;
                                    break;
                                }
                                break;
                            case 2:
                                if (!InputManager.isValid(grade_view.getText().toString().toCharArray()[0], new char[]{'A', 'B', 'C', 'D', 'F'}) && canCreate) {
                                    toast = getContext().getString(R.string.grade_between_F_A);
                                    canCreate = false;
                                    break;
                                }
                                if (!InputManager.isValid(grade_view.getText().toString().toCharArray()[1], new char[]{'+', '-'}) && canCreate) {
                                    toast = getContext().getString(R.string.grade_between_F_A);
                                    canCreate = false;
                                    break;
                                }
                                break;
                            default:
                                toast = getContext().getString(R.string.grade_between_F_A);
                                canCreate = false;
                                break;
                        }
                        break;
                }
                if (canCreate) {
                    title = title_view.getText().toString();
                    weight = Float.parseFloat(weight_view.getText().toString());
                    grade = getGrade();
                    Grade grade = new Grade(title);
                    grade.setSubject(subject);
                    grade.setDate(date);
                    grade.setWeight(weight);
                    switch (DataManager.getIntPref(getContext(), getContext().getString(R.string.general_data_preferences), getContext().getString(R.string.grading_key))) {
                        case 0:
                            grade.setGradeInPercents(this.grade);
                            break;
                        case 1:
                            grade.setGradeInOneToTen(this.grade);
                            break;
                        case 2:
                            grade.setGradeInAToF(Math.round(this.grade));
                            break;
                    }
                    grade.setType(type);
                    listener.onButtonClick(grade);
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

    private float getGrade() {
        String str = grade_view.getText().toString();
        switch (DataManager.getIntPref(getContext(), getContext().getString(R.string.general_data_preferences), getContext().getString(R.string.grading_key))) {
            case 0:
                return Float.parseFloat(str);
            case 1:
                return Float.parseFloat(str);
            case 2:
                switch (str) {
                    case "A+":
                        return 100;
                    case "A":
                        return 92;
                    case "A-":
                        return 83;
                    case "B+":
                        return 75;
                    case "B":
                        return 67;
                    case "B-":
                        return 58;
                    case "C+":
                        return 50;
                    case "C":
                        return 42;
                    case "C-":
                        return 33;
                    case "D+":
                        return 25;
                    case "D":
                        return 17;
                    case "D-":
                        return 8;
                    case "F":
                        return 0;
                    default:
                        return 0;
                }
            default:
                return 0;
        }
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
                Drawable bg = getContext().getResources().getDrawable(Subject.ids[subjectList.get(which-1).getDrawableIndex()]);
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
            Drawable bg = getContext().getResources().getDrawable(Subject.ids[map.getDrawableIndex()]);
            subject_view.setText(subject.getTitle());
            image.setBackground(bg);
            letter.setText(String.valueOf(subject.getTitle().charAt(0)));
        });
        subjectCreatorDialog.show();
    }

    public interface OnGradeCreatedListener {
        void onButtonClick(Grade grade);
    }
}
