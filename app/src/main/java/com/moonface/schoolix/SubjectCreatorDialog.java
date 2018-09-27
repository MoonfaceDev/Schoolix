package com.moonface.schoolix;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class SubjectCreatorDialog extends Dialog implements android.view.View.OnClickListener {

    private Activity activity;
    public LinearLayout image;
    public EditText title_view, teacher_view, room_view;
    public TextView letter;
    private int res_id;
    private OnSubjectCreatedListener listener;
    private Subject edit_subject;

    SubjectCreatorDialog(Activity activity, OnSubjectCreatedListener listener) {
        super(activity);
        this.activity = activity;
        this.listener = listener;
    }
    SubjectCreatorDialog(Activity activity, Subject subject, OnSubjectCreatedListener listener) {
        super(activity);
        this.activity = activity;
        this.edit_subject = subject;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_subject_creator);
        Button pos = findViewById(R.id.positiveButton);
        Button neg = findViewById(R.id.negativeButton);
        pos.setOnClickListener(this);
        neg.setOnClickListener(this);
        image = findViewById(R.id.image);
        letter = findViewById(R.id.letter);
        title_view = findViewById(R.id.title_view);
        teacher_view = findViewById(R.id.teacher_view);
        room_view = findViewById(R.id.room_view);
        if(edit_subject != null) {
            title_view.setText(edit_subject.getTitle());
            teacher_view.setText(edit_subject.getTeacher());
            room_view.setText(edit_subject.getRoom());
            res_id = edit_subject.getDrawableIndex();
            TextView dialog_title = findViewById(R.id.dialog_title);
            dialog_title.setText(R.string.edit_subject_label);
        } else {
            Random random = new Random();
            res_id = random.nextInt(Subject.ids.length);
        }
        Drawable drawable = activity.getResources().getDrawable(Subject.ids[res_id]);
        image.setBackground(drawable);
        title_view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length() > 0) {
                    letter.setText(String.valueOf(s.charAt(0)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.positiveButton:
                String title = title_view.getText().toString();
                String teacher = teacher_view.getText().toString();
                String room = room_view.getText().toString();
                boolean canCreate = true;
                String toast = null;
                if(title_view.getText().length() == 0){
                    toast = getContext().getString(R.string.title_empty_toast);
                    canCreate = false;
                }
                if(teacher_view.getText().length() == 0 && canCreate){
                    toast = getContext().getString(R.string.teacher_name_empty_toast);
                    canCreate = false;
                }
                if(room_view.getText().length() == 0 && canCreate){
                    toast = getContext().getString(R.string.room_name_empty_toast);
                    canCreate = false;
                }
                for(Subject s : DataManager.getSubjectsArray(getContext(), getContext().getString(R.string.subject_data_preferences))){
                    if(s.getTitle().equals(title_view.getText().toString())){
                        toast = getContext().getString(R.string.title_unique_toast);
                        canCreate = false;
                    }
                }
                if(canCreate){
                    Subject subject = new Subject(title);
                    subject.setTeacher(teacher);
                    subject.setRoom(room);
                    subject.setDrawableIndex(res_id);
                    listener.onButtonClick(subject);
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

    public interface OnSubjectCreatedListener {
        void onButtonClick(Subject subject);
    }
}
