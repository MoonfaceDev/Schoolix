package com.moonface.schoolix;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Objects;

public class WelcomeFragment extends Fragment {
    private ImageView image;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.setup_welcome, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        image = view.findViewById(R.id.image);

        ElevatedImageView ic_timetable = view.findViewById(R.id.ic_timetable);
        ElevatedImageView ic_subjects = view.findViewById(R.id.ic_subjects);
        ElevatedImageView ic_grades = view.findViewById(R.id.ic_grades);
        ElevatedImageView ic_tasks = view.findViewById(R.id.ic_tasks);

        ic_timetable.setElevation(16);

        ic_timetable.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image.setImageDrawable(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.screenshot1));
            } else {
                image.setImageDrawable(Objects.requireNonNull(getActivity()).getResources().getDrawable(R.drawable.screenshot1));
            }
            ic_timetable.setElevation(0);
            ic_subjects.setElevation(0);
            ic_grades.setElevation(0);
            ic_tasks.setElevation(0);

            ic_timetable.setElevation(16);
        });
        ic_subjects.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image.setImageDrawable(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.screenshot2));
            } else {
                image.setImageDrawable(Objects.requireNonNull(getActivity()).getResources().getDrawable(R.drawable.screenshot2));
            }
            ic_timetable.setElevation(0);
            ic_subjects.setElevation(0);
            ic_grades.setElevation(0);
            ic_tasks.setElevation(0);

            ic_subjects.setElevation(16);
        });
        ic_grades.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image.setImageDrawable(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.screenshot3));
            } else {
                image.setImageDrawable(Objects.requireNonNull(getActivity()).getResources().getDrawable(R.drawable.screenshot3));
            }
            ic_timetable.setElevation(0);
            ic_subjects.setElevation(0);
            ic_grades.setElevation(0);
            ic_tasks.setElevation(0);

            ic_grades.setElevation(16);
        });
        ic_tasks.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image.setImageDrawable(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.screenshot4));
            } else {
                image.setImageDrawable(Objects.requireNonNull(getActivity()).getResources().getDrawable(R.drawable.screenshot4));
            }
            ic_timetable.setElevation(0);
            ic_subjects.setElevation(0);
            ic_grades.setElevation(0);
            ic_tasks.setElevation(0);

            ic_tasks.setElevation(16);
        });
    }
}
