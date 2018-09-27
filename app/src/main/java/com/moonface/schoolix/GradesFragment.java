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

public class GradesFragment extends Fragment {

    RecyclerView grades_list;
    Spinner sort_category_view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grades, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        grades_list = view.findViewById(R.id.grades_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        grades_list.setLayoutManager(layoutManager);
        grades_list.setItemAnimator(new DefaultItemAnimator());
        sort_category_view = view.findViewById(R.id.sort_category_view);
        FloatingActionButton add_grade = view.findViewById(R.id.add_grade);

        add_grade.setOnClickListener(v -> {
            GradeCreatorDialog gradeCreatorDialog = new GradeCreatorDialog(getActivity(), grade -> {
                DataManager.putGrade(Objects.requireNonNull(getContext()), getString(R.string.grade_data_preferences), grade.getSubject().getTitle()+getString(R.string.underscore_char)+grade.getTitle(), grade);
                syncData(sort_category_view.getSelectedItemPosition());
            });
            gradeCreatorDialog.show();
        });

        String[] categories = getResources().getStringArray(R.array.grade_sort_categories);
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

    private ArrayList<Grade> getData() {
        if(getContext() != null) {
            return DataManager.getGradesArray(getContext(), getString(R.string.grade_data_preferences));
        }
        return null;
    }

    private void syncData(int position){
        ArrayList<Grade> data = getData();
        if(data != null) {
            switch (position) {
                case 0:
                    Collections.sort(data, (o1, o2) -> o2.getDate().compareTo(o1.getDate()));
                    break;
                case 1:
                    Collections.sort(data, (o1, o2) -> o1.getSubject().getTitle().compareTo(o2.getSubject().getTitle()));
                    break;
                case 2:
                    Collections.sort(data, (o1, o2) -> o1.getType().compareTo(o2.getType()));
                    break;
                case 3:
                    Collections.sort(data, (o1, o2) -> o2.getWeight().compareTo(o1.getWeight()));
                    break;
                case 4:
                    Collections.sort(data, (o1, o2) -> o2.getGrade().compareTo(o1.getGrade()));
                    break;
            }
            RecyclerView.Adapter<GradeAdapter.ViewHolder> adapter = new GradeAdapter(true, data, getContext(), item -> {
                if (getContext() != null) {
                    Intent intent = new Intent();
                    intent.setClass(getContext(), GradeViewActivity.class);
                    if (item.getSubject() != null) {
                        intent.putExtra(getString(R.string.name_key), item.getSubject().getTitle()+getString(R.string.underscore_char)+item.getTitle());
                        startActivity(intent);
                    }
                }
            });
            grades_list.setAdapter(adapter);
        }
    }
    @Override
    public void onStart(){
        super.onStart();
        syncData(sort_category_view.getSelectedItemPosition());
    }
}
