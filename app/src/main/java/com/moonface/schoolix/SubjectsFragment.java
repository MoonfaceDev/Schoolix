package com.moonface.schoolix;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Objects;

public class SubjectsFragment extends Fragment {
    GridView gridview;
    SubjectAdapter adapter;
    ArrayList<Subject> subjectsData;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subjects, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> showSubjectCreator());

        gridview = view.findViewById(R.id.subjects_grid);
        subjectsData = DataManager.getSubjectsArray(Objects.requireNonNull(getContext()), getString(R.string.subject_data_preferences));
        adapter = new SubjectAdapter(getContext(), subjectsData);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener((parent, v, position, id) -> {
            Intent intent = new Intent();
            intent.setClass(getContext(), SubjectViewActivity.class);
            intent.putExtra(getString(R.string.name_key), subjectsData.get(position).getTitle());
            startActivity(intent);
        });
    }
    private void showSubjectCreator() {
        SubjectCreatorDialog subjectCreatorDialog = new SubjectCreatorDialog(getActivity(), (Subject map) -> {
            DataManager.putSubject(Objects.requireNonNull(getContext()), getString(R.string.subject_data_preferences), map.getTitle(), map);
            subjectsData.add(map);
            adapter.notifyDataSetChanged();
        });
        subjectCreatorDialog.show();
    }
    @Override
    public void onStart(){
        super.onStart();
        subjectsData = DataManager.getSubjectsArray(Objects.requireNonNull(getContext()), getString(R.string.subject_data_preferences));
        adapter = new SubjectAdapter(getContext(), subjectsData);
        gridview.setAdapter(adapter);
    }
}
