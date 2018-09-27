package com.moonface.schoolix;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class ClassViewActivity extends AppCompatActivity {

    private String[] days;
    private TextView day_view;
    private TextView hour_view;
    private TextView teacher_view;
    private TextView room_view;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private Class lesson;

    @Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.view_class);
		Toolbar _toolbar = findViewById(R.id._toolbar);
		setSupportActionBar(_toolbar);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
		if(getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);
			_toolbar.setNavigationOnClickListener(_v -> onBackPressed());
		}
        days = getResources().getStringArray(R.array.days);
        day_view = findViewById(R.id.day_view);
        hour_view = findViewById(R.id.hour_view);
        teacher_view = findViewById(R.id.teacher_view);
        room_view = findViewById(R.id.room_view);
        TextView subject_view = findViewById(R.id.subject_view);
        Button delete_button = findViewById(R.id.delete_button), edit_button = findViewById(R.id.edit_button);
        if(getIntent().getStringExtra(getString(R.string.name_key)) != null) {
            lesson = DataManager.getClass(getApplicationContext(), getString(R.string.timetable_data_preferences), getIntent().getStringExtra(getString(R.string.name_key)));
            Period period = DataManager.getPeriod(getApplicationContext(), getString(R.string.period_data_preferences), lesson.getHour());
            collapsingToolbarLayout.setTitle(lesson.getSubject().getTitle());
            teacher_view.setText(lesson.getSubject().getTeacher());
            room_view.setText(lesson.getSubject().getRoom());
            day_view.setText(days[lesson.getDay()]);
            hour_view.setText(getString(R.string.hour_label).concat(getString(R.string.space_char)).concat(String.valueOf(lesson.getHour())).concat(getString(R.string.space_char)).concat(getString(R.string.opening_bracket)).concat(Period.simpleHourFormat.format(period.getStartingHour(), period.getStartingMinute())).concat(getString(R.string.space_char)).concat(getString(R.string.hyphen_char)).concat(getString(R.string.space_char)).concat(Period.simpleHourFormat.format(period.getEndingHour(), period.getEndingMinute())).concat(getString(R.string.closing_bracket)));
            subject_view.setOnClickListener((View v) -> viewSubject(lesson.getSubject()));
            delete_button.setOnClickListener((View v) -> deleteLesson(true));
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
    private void deleteLesson(boolean finish){
        DataManager.clearObject(getApplicationContext(), getString(R.string.timetable_data_preferences), getString(R.string.d)+String.valueOf(lesson.getDay())+getString(R.string.h)+String.valueOf(lesson.getHour()));
        NotificationManager.cancelClassAlarm(this, lesson);
        lesson = null;
        if(finish) {
            finish();
        }
    }
    private void openEditDialog(){
        ClassCreatorDialog classCreatorDialog = new ClassCreatorDialog(ClassViewActivity.this, Objects.requireNonNull(ClassViewActivity.this).getSupportFragmentManager(), lesson, output -> {
            deleteLesson(false);
            lesson = output;
            Period period = DataManager.getPeriod(getApplicationContext(), getString(R.string.period_data_preferences), lesson.getHour());
            DataManager.putClass(getApplicationContext(),getString(R.string.timetable_data_preferences), getString(R.string.d)+String.valueOf(lesson.getDay())+getString(R.string.h)+String.valueOf(lesson.getHour()), lesson);
            NotificationManager.startClassAlarm(this, lesson, DataManager.getPeriod(this, getString(R.string.period_data_preferences), lesson.getHour()));
            collapsingToolbarLayout.setTitle(output.getSubject().getTitle());
            teacher_view.setText(output.getSubject().getTeacher());
            room_view.setText(output.getSubject().getRoom());
            day_view.setText(days[output.getDay()]);
            hour_view.setText(getString(R.string.hour_label).concat(getString(R.string.space_char)).concat(String.valueOf(lesson.getHour())).concat(getString(R.string.space_char)).concat(getString(R.string.opening_bracket)).concat(Period.simpleHourFormat.format(period.getStartingHour(), period.getStartingMinute())).concat(getString(R.string.space_char)).concat(getString(R.string.hyphen_char)).concat(getString(R.string.space_char)).concat(Period.simpleHourFormat.format(period.getEndingHour(), period.getEndingMinute())).concat(getString(R.string.closing_bracket)));
        });
        classCreatorDialog.show();
    }
}
