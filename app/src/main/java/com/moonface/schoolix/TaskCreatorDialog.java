package com.moonface.schoolix;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

public class TaskCreatorDialog extends Dialog implements View.OnClickListener {

    private Activity activity;
    public LinearLayout image;
    public EditText title_view;
    public TextView subject_view, submission_view, type_view, letter;
    private OnTaskCreatedListener listener;
    private Task edit_task;
    private Subject subject;
    private String title;
    private Date submission;
    private String type;

    TaskCreatorDialog(Activity activity, OnTaskCreatedListener listener) {
        super(activity);
        this.activity = activity;
        this.listener = listener;
    }
    TaskCreatorDialog(Activity activity, Task task, OnTaskCreatedListener listener) {
        super(activity);
        this.activity = activity;
        this.edit_task = task;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_task_creator);
        Button pos = findViewById(R.id.positiveButton);
        Button neg = findViewById(R.id.negativeButton);
        pos.setOnClickListener(this);
        neg.setOnClickListener(this);
        image = findViewById(R.id.image);
        letter = findViewById(R.id.letter);
        title_view = findViewById(R.id.title_view);
        subject_view = findViewById(R.id.subject_view);
        subject_view.setOnClickListener(v -> showSubjectPicker());
        submission_view = findViewById(R.id.submission_view);
        submission_view.setOnClickListener(v -> showDatePicker());
        type_view = findViewById(R.id.type_view);
        type_view.setOnClickListener(v -> showTypePicker());
        if(edit_task != null) {
            title = edit_task.getTitle();
            subject = edit_task.getSubject();
            submission = edit_task.getDate();
            type = edit_task.getType();
            title_view.setText(title);
            subject_view.setText(subject.getTitle());
            submission_view.setText(Task.dateFormat.format(submission));
            type_view.setText(edit_task.getType());
            TextView dialog_title = findViewById(R.id.dialog_title);
            dialog_title.setText(R.string.edit_task_label);
            Drawable bg = getContext().getResources().getDrawable(Subject.ids[subject.getDrawableIndex()]);
            image.setBackground(bg);
            letter.setText(String.valueOf(subject.getTitle().charAt(0)));
        }
    }

    private void showTypePicker() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        CharSequence[] types = getContext().getResources().getStringArray(R.array.task_types);
        builder.setItems(types, (dialog, which) -> {
            type = types[which].toString();
            type_view.setText(type);
        });
        builder.show();
    }

    private void showDatePicker() {
        Calendar now = Calendar.getInstance();
        if(submission != null){
            now.setTime(submission);
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, monthOfYear, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            submission = calendar.getTime();
            submission_view.setText(Task.dateFormat.format(submission));
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.positiveButton:
                title = title_view.getText().toString();
                boolean canCreate = true;
                String toast = null;
                if(title.length() == 0) {
                    toast = getContext().getString(R.string.title_empty_toast);
                    canCreate = false;
                }
                if(subject == null && canCreate) {
                    toast = getContext().getString(R.string.pick_subject_toast);
                    canCreate = false;
                }
                if(submission == null && canCreate) {
                    toast = getContext().getString(R.string.set_date_toast);
                    canCreate = false;
                }
                if(type == null && canCreate) {
                    toast = getContext().getString(R.string.pick_type_toast);
                    canCreate = false;
                }
                if(canCreate){
                    Task task = new Task(title);
                    task.setSubject(subject);
                    task.setDate(submission);
                    task.setType(type);
                    listener.onButtonClick(task);
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

    public interface OnTaskCreatedListener {
        void onButtonClick(Task task);
    }
}
