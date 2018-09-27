package com.moonface.schoolix;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class TasksFragment extends Fragment {

    RecyclerView tasks_list;
    Spinner sort_category_view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        tasks_list = view.findViewById(R.id.tasks_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        tasks_list.setLayoutManager(layoutManager);
        tasks_list.setItemAnimator(new DefaultItemAnimator());
        sort_category_view = view.findViewById(R.id.sort_category_view);
        FloatingActionButton add_task = view.findViewById(R.id.add_task);

        add_task.setOnClickListener(v -> {
            TaskCreatorDialog taskCreatorDialog = new TaskCreatorDialog(getActivity(), task -> {
                DataManager.putTask(Objects.requireNonNull(getContext()), getString(R.string.task_data_preferences), task.getSubject().getTitle()+getString(R.string.underscore_char)+task.getTitle(), task);
                syncData(sort_category_view.getSelectedItemPosition());
            });
            taskCreatorDialog.show();
        });

        String[] categories = getResources().getStringArray(R.array.task_sort_categories);
        ArrayAdapter<String> sortCategoriesAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_dropdown_item, categories);
        sort_category_view.setAdapter(sortCategoriesAdapter);

        sort_category_view.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                syncData(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private ArrayList<Task> getData() {
        if(getContext() != null) {
            return DataManager.getTasksArray(getContext(), getString(R.string.task_data_preferences));
        }
        return null;
    }

    private void syncData(int position){
        ArrayList<Task> data = getData();
        if(data != null) {
            switch (position) {
                case 0:
                    Collections.sort(data, (o1, o2) -> o1.getDate().compareTo(o2.getDate()));
                    break;
                case 1:
                    Collections.sort(data, (o1, o2) -> o1.getSubject().getTitle().compareTo(o2.getSubject().getTitle()));
                    break;
                case 2:
                    Collections.sort(data, (o1, o2) -> o1.getType().compareTo(o2.getType()));
                    break;
            }
            RecyclerView.Adapter<TaskAdapter.ViewHolder> adapter = new TaskAdapter(true, data, getContext(), item -> {
                if (getContext() != null) {
                    Intent intent = new Intent();
                    intent.setClass(getContext(), TaskViewActivity.class);
                    if (item.getSubject() != null) {
                        intent.putExtra(getString(R.string.name_key), item.getSubject().getTitle()+getString(R.string.underscore_char)+item.getTitle());
                        startActivity(intent);
                    }
                }
            });
            tasks_list.setAdapter(adapter);
        }
    }
    @Override
    public void onStart(){
        super.onStart();
        syncData(sort_category_view.getSelectedItemPosition());
    }
}
