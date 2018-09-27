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

public class FirstDayFragment extends Fragment {
    private Spinner first_day_spinner;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.setup_first_day, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        first_day_spinner = view.findViewById(R.id.first_day_spinner);
        String[] categories = getResources().getStringArray(R.array.first_day_categories);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.custom_spinner_sropdown_item, categories);
        first_day_spinner.setAdapter(adapter);
    }
    public int getSelection(){
        return first_day_spinner.getSelectedItemPosition();
    }
}
