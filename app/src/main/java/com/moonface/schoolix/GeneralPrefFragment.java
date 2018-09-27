package com.moonface.schoolix;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Objects;

public class GeneralPrefFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_general_pref, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle){
        setupFirstDayPref(view.findViewById(R.id.first_day_spinner));
        setupGradingPref(view.findViewById(R.id.grading_spinner));
    }
    private void setupFirstDayPref(Spinner first_day_spinner){
        String[] categories = getResources().getStringArray(R.array.first_day_categories);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.custom_spinner_sropdown_item, categories);
        first_day_spinner.setAdapter(adapter);
        first_day_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DataManager.putIntPref(getContext(), getString(R.string.general_data_preferences), getString(R.string.first_day_key), position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        first_day_spinner.setSelection(DataManager.getIntPref(Objects.requireNonNull(getContext()), getString(R.string.general_data_preferences), getString(R.string.first_day_key)));
    }
    private void setupGradingPref(Spinner grading_spinner){
        String[] categories = getResources().getStringArray(R.array.grading_categories);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.custom_spinner_sropdown_item, categories);
        grading_spinner.setAdapter(adapter);
        grading_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DataManager.putIntPref(getContext(), getString(R.string.general_data_preferences), getString(R.string.grading_key), position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        grading_spinner.setSelection(DataManager.getIntPref(Objects.requireNonNull(getContext()), getString(R.string.general_data_preferences), getString(R.string.grading_key)));
    }
}
