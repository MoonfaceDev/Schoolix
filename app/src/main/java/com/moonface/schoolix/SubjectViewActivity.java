package com.moonface.schoolix;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Objects;

public class SubjectViewActivity extends AppCompatActivity {

    private String[] days;
    private TextView teacher_view;
    private TextView room_view;
    private ActionBar actionBar;
    private RecyclerView tasks_list;
    private RecyclerView grades_list;

    private Subject subject;

    @Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.view_subject);
		Toolbar _toolbar = findViewById(R.id._toolbar);
		setSupportActionBar(_toolbar);
		if(getSupportActionBar() != null) {
		    actionBar = getSupportActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeButtonEnabled(true);
			_toolbar.setNavigationOnClickListener(_v -> onBackPressed());
		}
        days = getResources().getStringArray(R.array.days);
        TextView next_lesson_view = findViewById(R.id.next_lesson_view);
        teacher_view = findViewById(R.id.teacher_view);
        room_view = findViewById(R.id.room_view);
        tasks_list = findViewById(R.id.tasks_list);
        grades_list = findViewById(R.id.grades_list);
        Button delete_button = findViewById(R.id.delete_button);
        Button edit_button = findViewById(R.id.edit_button);
        if(getIntent().getStringExtra(getString(R.string.name_key)) != null) {
            subject = DataManager.getSubject(getApplicationContext(), getString(R.string.subject_data_preferences), getIntent().getStringExtra(getString(R.string.name_key)));

            actionBar.setTitle(subject.getTitle());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    actionBar.setBackgroundDrawable(new ColorDrawable(Objects.requireNonNull(((GradientDrawable) Objects.requireNonNull(getDrawable(Subject.ids[subject.getDrawableIndex()]))).getColor()).getDefaultColor()));
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(Objects.requireNonNull(((GradientDrawable) Objects.requireNonNull(getDrawable(Subject.ids[subject.getDrawableIndex()]))).getColor()).getDefaultColor());
            }
            String nextLesson = getNextLesson(subject.getTitle());
            if (!nextLesson.equals("")) {
                next_lesson_view.setText(getString(R.string.next_lesson_label).concat(getString(R.string.colon_char)).concat(getString(R.string.space_char)).concat(nextLesson));
            } else {
                next_lesson_view.setText(getString(R.string.no_label).concat(getString(R.string.space_char)).concat(subject.getTitle()).concat(getString(R.string.space_char)).concat(getString(R.string.lessons_found_label)));
            }

            teacher_view.setText(subject.getTeacher());
            room_view.setText(subject.getRoom());
            initializeTasksList(subject);
            initializeGradesList(subject);
            delete_button.setOnClickListener((View v) -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.delete_confirmation_label) + getString(R.string.space_char) + getString(R.string.quotation_mark_char) + subject.getTitle() + getString(R.string.quotation_mark_char) + getString(R.string.question_mark_char));
                builder.setPositiveButton(R.string.yes_label, (dialog, which) -> deleteSubject(true));
                builder.setNegativeButton(R.string.cancel_label, (dialog, which) -> {
                });
                builder.create().show();
            });
            edit_button.setOnClickListener((View v) -> openEditDialog());

        }
	}

    private void initializeTasksList(Subject subject) {
        ArrayList<Task> data = DataManager.getTasksArray(getApplicationContext(), getString(R.string.task_data_preferences));
        ArrayList<Task> subjectFiltered = new ArrayList<>();
        for(int i=0; i<data.size();i++) {
            if (data.get(i).getSubject().getTitle().equals(subject.getTitle())) {
                subjectFiltered.add(data.get(i));
            }
        }
        Collections.sort(subjectFiltered, (o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        RecyclerView.Adapter<TaskAdapter.ViewHolder> adapter = new TaskAdapter(false, subjectFiltered, getApplicationContext(), item -> {
            if (getApplicationContext() != null) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), TaskViewActivity.class);
                if (item.getSubject() != null) {
                    intent.putExtra(getString(R.string.name_key), item.getSubject().getTitle()+getString(R.string.underscore_char)+item.getTitle());
                    startActivity(intent);
                    finish();
                }
            }
        });
        tasks_list.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getApplicationContext()), DividerItemDecoration.VERTICAL));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        tasks_list.setLayoutManager(layoutManager);
        tasks_list.setItemAnimator(new DefaultItemAnimator());
        tasks_list.setAdapter(adapter);

    }

    private void initializeGradesList(Subject subject) {
        ArrayList<Grade> data = DataManager.getGradesArray(getApplicationContext(), getString(R.string.grade_data_preferences));
        ArrayList<Grade> subjectFiltered = new ArrayList<>();
        for(int i=0; i<data.size();i++) {
            if (data.get(i).getSubject().getTitle().equals(subject.getTitle())) {
                subjectFiltered.add(data.get(i));
            }
        }
        Collections.sort(subjectFiltered, (o1, o2) -> o2.getDate().compareTo(o1.getDate()));
        RecyclerView.Adapter<GradeAdapter.ViewHolder> adapter = new GradeAdapter(false, subjectFiltered, getApplicationContext(), item -> {
            if (getApplicationContext() != null) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), GradeViewActivity.class);
                if (item.getSubject() != null) {
                    intent.putExtra(getString(R.string.name_key), item.getSubject().getTitle()+getString(R.string.underscore_char)+item.getTitle());
                    startActivity(intent);
                    finish();
                }
            }
        });
        grades_list.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getApplicationContext()), DividerItemDecoration.VERTICAL));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        grades_list.setLayoutManager(layoutManager);
        grades_list.setItemAnimator(new DefaultItemAnimator());
        grades_list.setAdapter(adapter);

        TextView weighted_average_view = findViewById(R.id.weighted_average_view);
        weighted_average_view.setText(getString(R.string.weighted_average_label).concat(getString(R.string.colon_char)).concat(getString(R.string.space_char)).concat(subjectFiltered.size()>0 ? Grade.format(subjectFiltered.get(0).getGrading(), getWeightedAverage(subjectFiltered)): ""));
        if(subjectFiltered.size() == 0){
            weighted_average_view.setVisibility(View.GONE);
        } else{
            weighted_average_view.setVisibility(View.VISIBLE);
        }
    }

    private Float getWeightedAverage(ArrayList<Grade> data) {
        Float output = 0f;
        Float weightSum = 0f;
        for(Grade grade : data){
            weightSum += grade.getWeight();
        }
        for(int i=0; i<data.size(); i++){
            output += data.get(i).getGrade()/100 * data.get(i).getWeight()/weightSum;
        }
        return output*100;
    }

    private String getNextLesson(String title) {
        ArrayList<Class> classes = DataManager.getClassesArray(getApplicationContext(), getString(R.string.timetable_data_preferences));
        if (classes.size() > 0) {
            ArrayList<Class> subjectFiltered = new ArrayList<>();
            for (int i = 0; i < classes.size(); i++) {
                if (classes.get(i).getSubject().getTitle().equals(title)) {
                    subjectFiltered.add(classes.get(i));
                }
            }
            if (subjectFiltered.size() > 0) {
                Calendar calendar = Calendar.getInstance();
                int day;
                day = calendar.get(Calendar.DAY_OF_WEEK) - 2;
                if(day < 0){
                    day += 7;
                }
                if(day > 6){
                    day -= 7;
                }
                int today = day;
                ArrayList<Class> dayFiltered = new ArrayList<>();
                while (true) {
                    for (int i = 0; i < subjectFiltered.size(); i++) {
                        if (subjectFiltered.get(i).getDay() == day) {
                            if (subjectFiltered.get(i).getDay() == today) {
                                Calendar calendar1 = Calendar.getInstance();
                                calendar1.set(Calendar.HOUR_OF_DAY, DataManager.getPeriod(this, getString(R.string.period_data_preferences), subjectFiltered.get(i).getHour()).getStartingHour());
                                calendar1.set(Calendar.MINUTE, DataManager.getPeriod(this, getString(R.string.period_data_preferences), subjectFiltered.get(i).getHour()).getStartingMinute());
                                if (calendar1.getTimeInMillis() > System.currentTimeMillis()) {
                                    dayFiltered.add(subjectFiltered.get(i));
                                }
                            } else {
                                dayFiltered.add(subjectFiltered.get(i));
                            }
                        }
                    }
                    if (dayFiltered.size() > 0) {
                        break;
                    }
                    day++;
                    if (day == 7) {
                        day = 0;
                    }
                }
                Collections.sort(dayFiltered, (o1, o2) -> o1.getHour() - o2.getHour());
                if (dayFiltered.size() > 0) {
                    return days[dayFiltered.get(0).getDay()] + getString(R.string.comma_char) + getString(R.string.space_char) + getString(R.string.hour_label) + getString(R.string.space_char) + dayFiltered.get(0).getHour();
                }
            }
        }
        return "";
    }

    private void deleteSubject(boolean finish){
        DataManager.clearObject(getApplicationContext(), getString(R.string.subject_data_preferences), subject.getTitle());
        ArrayList<Class> classes = DataManager.getClassesArray(getApplicationContext(), getString(R.string.timetable_data_preferences));
        for(int i=0;i<classes.size();i++) {
            if (classes.get(i).getSubject().getTitle().equals(subject.getTitle())) {
                DataManager.clearObject(getApplicationContext(), getString(R.string.timetable_data_preferences), getString(R.string.d) + String.valueOf(classes.get(i).getDay()) + getString(R.string.h) + String.valueOf(classes.get(i).getHour()));
                NotificationManager.cancelClassAlarm(this, classes.get(i));
            }
        }
        ArrayList<Task> tasks = DataManager.getTasksArray(getApplicationContext(), getString(R.string.task_data_preferences));
        for(int i=0;i<tasks.size();i++) {
            if (tasks.get(i).getSubject().getTitle().equals(subject.getTitle())) {
                DataManager.clearObject(getApplicationContext(), getString(R.string.task_data_preferences), tasks.get(i).getSubject().getTitle()+getString(R.string.underscore_char)+tasks.get(i).getTitle());
            }
        }
        ArrayList<Grade> grades = DataManager.getGradesArray(getApplicationContext(), getString(R.string.grade_data_preferences));
        for(int i=0;i<grades.size();i++) {
            if (grades.get(i).getSubject().getTitle().equals(subject.getTitle())) {
                DataManager.clearObject(getApplicationContext(), getString(R.string.grade_data_preferences), grades.get(i).getSubject().getTitle()+getString(R.string.underscore_char)+grades.get(i).getTitle());
            }
        }
        subject = null;
        if(finish) {
            finish();
        }
    }
    private void openEditDialog(){
        SubjectCreatorDialog subjectCreatorDialog = new SubjectCreatorDialog(SubjectViewActivity.this, subject, output -> {
            ArrayList<Class> classes = DataManager.getClassesArray(getApplicationContext(), getString(R.string.timetable_data_preferences));
            for(int i=0; i<classes.size();i++){
                Class lesson = classes.get(i);
                if(lesson.getSubject() == subject){
                    lesson.setSubject(output);
                    DataManager.putClass(getApplicationContext(), getString(R.string.timetable_data_preferences), getString(R.string.d)+String.valueOf(lesson.getDay())+getString(R.string.h)+String.valueOf(lesson.getHour()), lesson);
                    NotificationManager.startClassAlarm(this, lesson, DataManager.getPeriod(this, getString(R.string.period_data_preferences), lesson.getHour()));
                }
            }
            deleteSubject(false);
            subject = output;
            actionBar.setTitle(output.getTitle());
            teacher_view.setText(output.getTeacher());
            room_view.setText(output.getRoom());
        });
        subjectCreatorDialog.show();
    }
    @Override
    public void onStart(){
        super.onStart();
        initializeTasksList(subject);
        initializeGradesList(subject);
    }

}
