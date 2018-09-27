package com.moonface.schoolix;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TaskViewActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private Task task;
    private TextView submission_view, subject_view, type_view, letter;
    private LinearLayout image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_task);
        android.support.v7.widget.Toolbar _toolbar = findViewById(R.id._toolbar);
        setSupportActionBar(_toolbar);
        if(getSupportActionBar() != null) {
            actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            _toolbar.setNavigationOnClickListener(_v -> onBackPressed());
        }

        submission_view = findViewById(R.id.submission_view);
        subject_view = findViewById(R.id.subject_view);
        type_view = findViewById(R.id.type_view);
        image = findViewById(R.id.image);
        letter = findViewById(R.id.letter);
        View subject_link = findViewById(R.id.subject_link);
        Button edit_button = findViewById(R.id.edit_button);
        Button done_button = findViewById(R.id.done_button);

        if(getIntent().getStringExtra(getString(R.string.name_key)) != null) {
            task = DataManager.getTask(getApplicationContext(), getString(R.string.task_data_preferences), getIntent().getStringExtra(getString(R.string.name_key)));
            actionBar.setTitle(task.getTitle());
            submission_view.setText(Task.dateFormat.format(task.getDate()));
            subject_view.setText(task.getSubject().getTitle());
            type_view.setText(task.getType());
            Drawable bg = getResources().getDrawable(Subject.ids[task.getSubject().getDrawableIndex()]);
            image.setBackground(bg);
            letter.setText(String.valueOf(task.getSubject().getTitle().charAt(0)));
            subject_link.setOnClickListener((View v) -> viewSubject(task.getSubject()));
            done_button.setOnClickListener((View v) -> deleteTask(true));
            edit_button.setOnClickListener((View v) -> openEditDialog());
        }

    }
    private void viewSubject(Subject subject){
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), SubjectViewActivity.class);
        intent.putExtra(getString(R.string.name_key), subject.getTitle());
        startActivity(intent);
        finish();
    }
    private void deleteTask(boolean finish){
        DataManager.clearObject(getApplicationContext(), getString(R.string.task_data_preferences), task.getSubject().getTitle()+getString(R.string.underscore_char)+task.getTitle());
        task = null;
        if(finish) {
            finish();
        }
    }
    private void openEditDialog(){
        TaskCreatorDialog taskCreatorDialog = new TaskCreatorDialog(TaskViewActivity.this, task, output -> {
            deleteTask(false);
            DataManager.putTask(getApplicationContext(), getString(R.string.task_data_preferences), output.getSubject().getTitle()+getString(R.string.underscore_char)+output.getTitle(), output);
            task = output;
            actionBar.setTitle(task.getTitle());
            submission_view.setText(Task.dateFormat.format(task.getDate()));
            subject_view.setText(task.getSubject().getTitle());
            type_view.setText(task.getType());
            Drawable bg = getResources().getDrawable(Subject.ids[task.getSubject().getDrawableIndex()]);
            image.setBackground(bg);
            letter.setText(String.valueOf(task.getSubject().getTitle().charAt(0)));

        });
        taskCreatorDialog.show();
    }
}
