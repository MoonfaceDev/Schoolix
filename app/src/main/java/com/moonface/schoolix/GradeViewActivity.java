package com.moonface.schoolix;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GradeViewActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private Grade grade;
    private TextView date_view, subject_view, type_view, grade_view, weight_view, letter;
    private LinearLayout image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_grade);
        android.support.v7.widget.Toolbar _toolbar = findViewById(R.id._toolbar);
        setSupportActionBar(_toolbar);
        if(getSupportActionBar() != null) {
            actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            _toolbar.setNavigationOnClickListener(_v -> onBackPressed());
        }

        date_view = findViewById(R.id.date_view);
        subject_view = findViewById(R.id.subject_view);
        type_view = findViewById(R.id.type_view);
        grade_view = findViewById(R.id.grade_view);
        weight_view = findViewById(R.id.weight_view);
        image = findViewById(R.id.image);
        letter = findViewById(R.id.letter);
        View subject_link = findViewById(R.id.subject_link);
        Button edit_button = findViewById(R.id.edit_button);
        Button delete_button = findViewById(R.id.delete_button);

        if(getIntent().getStringExtra(getString(R.string.name_key)) != null) {
            grade = DataManager.getGrade(getApplicationContext(), getString(R.string.grade_data_preferences), getIntent().getStringExtra(getString(R.string.name_key)));
            actionBar.setTitle(grade.getTitle());
            date_view.setText(Grade.dateFormat.format(grade.getDate()));
            subject_view.setText(grade.getSubject().getTitle());
            type_view.setText(grade.getType());
            Drawable bg = getResources().getDrawable(Subject.ids[grade.getSubject().getDrawableIndex()]);
            image.setBackground(bg);
            letter.setText(String.valueOf(grade.getSubject().getTitle().charAt(0)));
            grade_view.setText(Grade.format(grade.getGrading(), grade.getGrade()));
            weight_view.setText(Grade.format(Grade.Grading.ONE_TO_HUNDRED, grade.getWeight()).concat(getString(R.string.percents_char)));
            subject_link.setOnClickListener((View v) -> viewSubject(grade.getSubject()));
            delete_button.setOnClickListener((View v) -> deleteGrade(true));
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
    private void deleteGrade(boolean finish){
        DataManager.clearObject(getApplicationContext(), getString(R.string.grade_data_preferences), grade.getSubject().getTitle()+getString(R.string.underscore_char)+grade.getTitle());
        grade = null;
        if(finish) {
            finish();
        }
    }
    private void openEditDialog(){
        GradeCreatorDialog gradeCreatorDialog = new GradeCreatorDialog(GradeViewActivity.this, grade, output -> {
            deleteGrade(false);
            DataManager.putGrade(getApplicationContext(), getString(R.string.grade_data_preferences), output.getSubject().getTitle()+getString(R.string.underscore_char)+output.getTitle(), output);
            grade = output;
            actionBar.setTitle(grade.getTitle());
            date_view.setText(Grade.dateFormat.format(grade.getDate()));
            subject_view.setText(grade.getSubject().getTitle());
            type_view.setText(grade.getType());
            Drawable bg = getResources().getDrawable(Subject.ids[grade.getSubject().getDrawableIndex()]);
            image.setBackground(bg);
            letter.setText(String.valueOf(grade.getSubject().getTitle().charAt(0)));
            grade_view.setText(Grade.format(grade.getGrading(), grade.getGrade()));
            weight_view.setText(Grade.format(Grade.Grading.ONE_TO_HUNDRED, grade.getWeight()).concat(getString(R.string.percents_char)));

        });
        gradeCreatorDialog.show();
    }
}
