package com.moonface.schoolix;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collection;

import static android.content.Context.MODE_PRIVATE;

public class DataManager {
    static void putClass(Context context, String parent, String name, Class object){
        SharedPreferences sharedPreferences = context.getSharedPreferences(parent, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(object);
        sharedPreferences.edit().putString(name, json).apply();
    }
    static void putSubject(Context context, String parent, String name, Subject object){
        SharedPreferences sharedPreferences = context.getSharedPreferences(parent, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(object);
        sharedPreferences.edit().putString(name, json).apply();
    }
    static void putTask(Context context, String parent, String name, Task object){
        SharedPreferences sharedPreferences = context.getSharedPreferences(parent, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(object);
        sharedPreferences.edit().putString(name, json).apply();
    }
    static void putGrade(Context context, String parent, String name, Grade object){
        SharedPreferences sharedPreferences = context.getSharedPreferences(parent, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(object);
        sharedPreferences.edit().putString(name, json).apply();
    }
    static void putSemester(Context context, String parent, int number, Semester object){
        SharedPreferences sharedPreferences = context.getSharedPreferences(parent, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(object);
        sharedPreferences.edit().putString(String.valueOf(number), json).apply();
    }
    static void putPeriod(Context context, String parent, int number, Period object){
        SharedPreferences sharedPreferences = context.getSharedPreferences(parent, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(object);
        sharedPreferences.edit().putString(String.valueOf(number), json).apply();
    }
    static void putBreak(Context context, String parent, int number, Break object){
        SharedPreferences sharedPreferences = context.getSharedPreferences(parent, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(object);
        sharedPreferences.edit().putString(String.valueOf(number), json).apply();
    }

    static void putStringPref(Context context, String parent, String name, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(parent, MODE_PRIVATE);
        sharedPreferences.edit().putString(name, value).apply();
    }
    static void putIntPref(Context context, String parent, String name, int value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(parent, MODE_PRIVATE);
        sharedPreferences.edit().putInt(name, value).apply();
    }
    static void putBooleanPref(Context context, String parent, String name, boolean value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(parent, MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(name, value).apply();
    }
    static void putFloatPref(Context context, String parent, String name, float value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(parent, MODE_PRIVATE);
        sharedPreferences.edit().putFloat(name, value).apply();
    }


    static Class getClass(Context context, String parent, String name){
        SharedPreferences sharedPreferences = context.getSharedPreferences(parent, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(name, null);
        return gson.fromJson(json, Class.class);
    }
    static Subject getSubject(Context context, String parent, String name){
        SharedPreferences sharedPreferences = context.getSharedPreferences(parent, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(name, null);
        return gson.fromJson(json, Subject.class);
    }
    static Task getTask(Context context, String parent, String name){
        SharedPreferences sharedPreferences = context.getSharedPreferences(parent, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(name, null);
        return gson.fromJson(json, Task.class);
    }
    static Grade getGrade(Context context, String parent, String name){
        SharedPreferences sharedPreferences = context.getSharedPreferences(parent, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(name, null);
        return gson.fromJson(json, Grade.class);
    }
    static Semester getSemester(Context context, String parent, int number){
        SharedPreferences sharedPreferences = context.getSharedPreferences(parent, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(String.valueOf(number), null);
        return gson.fromJson(json, Semester.class);
    }
    static Period getPeriod(Context context, String parent, int number){
        SharedPreferences sharedPreferences = context.getSharedPreferences(parent, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(String.valueOf(number), null);
        return gson.fromJson(json, Period.class);
    }
    static Break getBreak(Context context, String parent, int number){
        SharedPreferences sharedPreferences = context.getSharedPreferences(parent, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(String.valueOf(number), null);
        return gson.fromJson(json, Break.class);
    }


    static String getStringPref(Context context, String parent, String name){
        SharedPreferences sharedPreferences = context.getSharedPreferences(parent, MODE_PRIVATE);
        return sharedPreferences.getString(name, null);
    }
    static int getIntPref(Context context, String parent, String name){
        SharedPreferences sharedPreferences = context.getSharedPreferences(parent, MODE_PRIVATE);
        return sharedPreferences.getInt(name, -1);
    }
    static boolean getBooleanPref(Context context, String parent, String name){
        SharedPreferences sharedPreferences = context.getSharedPreferences(parent, MODE_PRIVATE);
        return sharedPreferences.getBoolean(name, false);
    }
    static float getFloatPref(Context context, String parent, String name){
        SharedPreferences sharedPreferences = context.getSharedPreferences(parent, MODE_PRIVATE);
        return sharedPreferences.getFloat(name, 0f);
    }


    static ArrayList<Class> getClassesArray(Context context, String parent){
        SharedPreferences sharedPreferences = context.getSharedPreferences(parent, MODE_PRIVATE);
        ArrayList<String> jsons = new ArrayList<>((Collection<? extends String>) sharedPreferences.getAll().values());
        Gson gson = new Gson();
        ArrayList<Class> output = new ArrayList<>();
        for (String json : jsons){
            output.add(gson.fromJson(json, Class.class));
        }
        return output;
    }
    static ArrayList<Subject> getSubjectsArray(Context context, String parent){
        SharedPreferences sharedPreferences = context.getSharedPreferences(parent, MODE_PRIVATE);
        ArrayList<String> jsons = new ArrayList<>((Collection<? extends String>) sharedPreferences.getAll().values());
        Gson gson = new Gson();
        ArrayList<Subject> output = new ArrayList<>();
        for (String json : jsons){
            output.add(gson.fromJson(json, Subject.class));
        }
        return output;
    }
    static ArrayList<Task> getTasksArray(Context context, String parent){
        SharedPreferences sharedPreferences = context.getSharedPreferences(parent, MODE_PRIVATE);
        ArrayList<String> jsons = new ArrayList<>((Collection<? extends String>) sharedPreferences.getAll().values());
        Gson gson = new Gson();
        ArrayList<Task> output = new ArrayList<>();
        for (String json : jsons){
            output.add(gson.fromJson(json, Task.class));
        }
        return output;
    }
    static ArrayList<Grade> getGradesArray(Context context, String parent){
        SharedPreferences sharedPreferences = context.getSharedPreferences(parent, MODE_PRIVATE);
        ArrayList<String> jsons = new ArrayList<>((Collection<? extends String>) sharedPreferences.getAll().values());
        Gson gson = new Gson();
        ArrayList<Grade> output = new ArrayList<>();
        for (String json : jsons){
            output.add(gson.fromJson(json, Grade.class));
        }
        return output;
    }
    static ArrayList<Semester> getSemestersArray(Context context, String parent){
        SharedPreferences sharedPreferences = context.getSharedPreferences(parent, MODE_PRIVATE);
        ArrayList<String> jsons = new ArrayList<>((Collection<? extends String>) sharedPreferences.getAll().values());
        Gson gson = new Gson();
        ArrayList<Semester> output = new ArrayList<>();
        for (String json : jsons){
            output.add(gson.fromJson(json, Semester.class));
        }
        return output;
    }
    static ArrayList<Period> getPeriodsArray(Context context, String parent){
        SharedPreferences sharedPreferences = context.getSharedPreferences(parent, MODE_PRIVATE);
        ArrayList<String> jsons = new ArrayList<>((Collection<? extends String>) sharedPreferences.getAll().values());
        Gson gson = new Gson();
        ArrayList<Period> output = new ArrayList<>();
        for (String json : jsons){
            output.add(gson.fromJson(json, Period.class));
        }
        return output;
    }
    static ArrayList<Break> getBreaksArray(Context context, String parent){
        SharedPreferences sharedPreferences = context.getSharedPreferences(parent, MODE_PRIVATE);
        ArrayList<String> jsons = new ArrayList<>((Collection<? extends String>) sharedPreferences.getAll().values());
        Gson gson = new Gson();
        ArrayList<Break> output = new ArrayList<>();
        for (String json : jsons){
            output.add(gson.fromJson(json, Break.class));
        }
        return output;
    }


    static ArrayList<String> getObjectsNamesList(Context context, String parent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(parent, MODE_PRIVATE);
        return new ArrayList<>(sharedPreferences.getAll().keySet());
    }
    static void clearObject(Context context, String parent, String name){
        SharedPreferences sharedPreferences = context.getSharedPreferences(parent, MODE_PRIVATE);
        sharedPreferences.edit().remove(name).apply();
    }
}
