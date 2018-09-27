package com.moonface.schoolix;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Objects;

public class GradingFragment extends Fragment {
    Spinner grading_spinner;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.setup_grading, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        grading_spinner = view.findViewById(R.id.grading_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.custom_spinner_sropdown_item, getResources().getStringArray(R.array.grading_categories));
        grading_spinner.setAdapter(adapter);
    }
    public int getSelection(){
        return grading_spinner.getSelectedItemPosition();
    }
}
