package com.moonface.schoolix;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class BackupAndRestorePrefFragment extends Fragment {
    String FILE_NAME = "backup";
    File folder;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_backup_and_restore_pref, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle){
        folder = Objects.requireNonNull(Objects.requireNonNull(getContext()).getExternalFilesDir(Environment.DIRECTORY_DCIM)).getParentFile();
        Button backup_button = view.findViewById(R.id.backup_button);
        Button restore_button = view.findViewById(R.id.restore_button);
        Button erase_button = view.findViewById(R.id.erase_button);
        backup_button.setOnClickListener(v -> {
            save(getJsonString());
            Toast.makeText(getContext(), getString(R.string.backup_saved_toast) + folder+getString(R.string.slash_char)+FILE_NAME, Toast.LENGTH_LONG).show();
        });
        restore_button.setOnClickListener(v -> {
            if(restoreData(load())){
                Toast.makeText(getContext(), R.string.data_restored_toast, Toast.LENGTH_SHORT).show();
                if (DataManager.getBooleanPref(getContext(), getString(R.string.general_data_preferences), getString(R.string.daily_notification_key))) {
                    NotificationManager.startDailyAlarm(getContext(), DataManager.getIntPref(getContext(), getString(R.string.general_data_preferences), getString(R.string.notification_hour_key)), DataManager.getIntPref(getContext(), getString(R.string.general_data_preferences), getString(R.string.notification_minute_key)));
                }
                if(DataManager.getBooleanPref(getContext(), getString(R.string.general_data_preferences), getString(R.string.timetable_notification_key))){
                    ArrayList<Class> classes = DataManager.getClassesArray(getContext(), getString(R.string.timetable_data_preferences));
                    for(Class lesson : classes){
                        Period period = DataManager.getPeriod(getContext(), getString(R.string.period_data_preferences), lesson.getHour());
                        NotificationManager.startClassAlarm(getContext(), lesson, period);
                    }
                }
            } else {
                Toast.makeText(getContext(), R.string.cannot_find_backup_toast, Toast.LENGTH_SHORT).show();
            }
        });
        erase_button.setOnClickListener(v-> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(getString(R.string.delete_confirmation_label)+getString(R.string.space_char)+getString(R.string.schoolix_data)+getString(R.string.question_mark_char));
            builder.setNegativeButton(R.string.cancel_label, (dialog, which) -> {});
            builder.setPositiveButton(R.string.yes_label, (dialog, which) -> {
                eraseData();
                Toast.makeText(getContext(), R.string.data_erased_toast, Toast.LENGTH_SHORT).show();
                Intent i = getContext().getPackageManager().getLaunchIntentForPackage(getContext().getPackageName());
                if (i != null) {
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                startActivity(i);
            });
            builder.show();
        });
    }
    private String getJsonString(){
        String[] dirs = new String[]{getString(R.string.timetable_data_preferences),getString(R.string.subject_data_preferences),getString(R.string.task_data_preferences),getString(R.string.grade_data_preferences),getString(R.string.semester_data_preferences),getString(R.string.period_data_preferences),getString(R.string.break_data_preferences),getString(R.string.general_data_preferences)};
        StringBuilder output = new StringBuilder();
        for (String dir : dirs) {
            SharedPreferences sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences(dir, MODE_PRIVATE);
            Gson gson = new Gson();
            output.append(getString(R.string.separator_char)).append(gson.toJson(sharedPreferences.getAll(), Map.class));
        }
        return output.toString();
    }
    private void eraseData(){
        Objects.requireNonNull(getContext());
        save(getJsonString());
        String[] dirs = new String[]{getString(R.string.timetable_data_preferences),getString(R.string.subject_data_preferences),getString(R.string.task_data_preferences),getString(R.string.grade_data_preferences),getString(R.string.semester_data_preferences),getString(R.string.period_data_preferences),getString(R.string.break_data_preferences),getString(R.string.general_data_preferences)};
        for(String dir : dirs){
            SharedPreferences sharedPreferences = getContext().getSharedPreferences(dir, MODE_PRIVATE);
            sharedPreferences.edit().clear().apply();
        }
    }
    private boolean restoreData(String data){
        if(data.length() > 0) {
            Objects.requireNonNull(getContext());
            String[] dirs = new String[]{getString(R.string.timetable_data_preferences),getString(R.string.subject_data_preferences),getString(R.string.task_data_preferences),getString(R.string.grade_data_preferences),getString(R.string.semester_data_preferences),getString(R.string.period_data_preferences),getString(R.string.break_data_preferences),getString(R.string.general_data_preferences)};
            int firstIndex;
            int lastIndex = 0;
            for (int i = 0; i < dirs.length; i++) {
                Gson gson = new Gson();
                firstIndex = lastIndex;
                if (i != dirs.length - 1) {
                    lastIndex = data.indexOf(getString(R.string.separator_char), firstIndex + 1);
                } else {
                    lastIndex = data.length();
                }
                String json = data.substring(firstIndex + 1, lastIndex);
                Map map = gson.fromJson(json, Map.class);
                Object[] keys = map.keySet().toArray(new Object[map.size()]);
                switch (i) {
                    case 0:
                        for (int k = 0; k < map.size(); k++) {
                            DataManager.putClass(getContext(), dirs[i], (String) keys[k], gson.fromJson(map.get(keys[k]).toString(), Class.class));
                        }
                        break;
                    case 1:
                        for (int k = 0; k < map.size(); k++) {
                            DataManager.putSubject(getContext(), dirs[i], (String) keys[k], gson.fromJson(map.get(keys[k]).toString(), Subject.class));
                        }
                        break;
                    case 2:
                        for (int k = 0; k < map.size(); k++) {
                            DataManager.putTask(getContext(), dirs[i], (String) keys[k], gson.fromJson(map.get(keys[k]).toString(), Task.class));
                        }
                        break;
                    case 3:
                        for (int k = 0; k < map.size(); k++) {
                            DataManager.putGrade(getContext(), dirs[i], (String) keys[k], gson.fromJson(map.get(keys[k]).toString(), Grade.class));
                        }
                        break;
                    case 4:
                        for (int k = 0; k < map.size(); k++) {
                            DataManager.putSemester(getContext(), dirs[i], Integer.parseInt((String) keys[k]), gson.fromJson(map.get(keys[k]).toString(), Semester.class));
                        }
                        break;
                    case 5:
                        for (int k = 0; k < map.size(); k++) {
                            DataManager.putPeriod(getContext(), dirs[i], Integer.parseInt((String) keys[k]), gson.fromJson(map.get(keys[k]).toString(), Period.class));
                        }
                        break;
                    case 6:
                        for (int k = 0; k < map.size(); k++) {
                            DataManager.putBreak(getContext(), dirs[i], Integer.parseInt((String) keys[k]), gson.fromJson(map.get(keys[k]).toString(), Break.class));
                        }
                        break;
                    case 7:
                        DataManager.putIntPref(getContext(), dirs[i], getString(R.string.first_day_key), gson.fromJson(map.get(getString(R.string.first_day_key)).toString(), Integer.class));
                        DataManager.putIntPref(getContext(), dirs[i], getString(R.string.grading_key), gson.fromJson(map.get(getString(R.string.grading_key)).toString(), Integer.class));
                        DataManager.putBooleanPref(getContext(), dirs[i], getString(R.string.daily_notification_key), gson.fromJson(map.get(getString(R.string.daily_notification_key)).toString(), Boolean.class));
                        DataManager.putIntPref(getContext(), dirs[i], getString(R.string.notification_hour_key), gson.fromJson(map.get(getString(R.string.notification_hour_key)).toString(), Integer.class));
                        DataManager.putIntPref(getContext(), dirs[i], getString(R.string.notification_minute_key), gson.fromJson(map.get(getString(R.string.notification_minute_key)).toString(), Integer.class));
                        break;
                }
            }
            return true;
        }
        return false;
    }
    private void save(String string) {
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(folder+getString(R.string.slash_char)+FILE_NAME);
            fos.write(string.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public String load() {
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(folder+getString(R.string.slash_char)+FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }

            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }
}
