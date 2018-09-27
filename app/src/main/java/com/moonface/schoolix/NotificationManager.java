package com.moonface.schoolix;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.google.gson.Gson;
import java.util.Calendar;
import java.util.Objects;

import static android.content.Context.ALARM_SERVICE;

public class NotificationManager {
    static void startDailyAlarm(Context context, int hour, int minute) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, TaskNotificationReceiver.class);
        alarmIntent.setAction(context.getString(R.string.daily_notification_tag));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmIntent.setData((Uri.parse(context.getString(R.string.custom_uri_prefix)+System.currentTimeMillis())));
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        Calendar alarmStartTime = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        alarmStartTime.set(Calendar.HOUR_OF_DAY, hour);
        alarmStartTime.set(Calendar.MINUTE, minute);
        alarmStartTime.set(Calendar.SECOND, 0);
        if (now.after(alarmStartTime)) {
            alarmStartTime.add(Calendar.DATE, 1);
        }

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    static void cancelDailyAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager) Objects.requireNonNull(context).getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, TaskNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }


    static void startClassAlarm(Context context, Class lesson, Period period) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, TimetableNotificationReceiver.class);
        alarmIntent.setAction(context.getString(R.string.d)+lesson.getDay()+context.getString(R.string.h)+lesson.getHour());
        Gson gson = new Gson();
        alarmIntent.putExtra(context.getString(R.string.class_key), gson.toJson(lesson));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmIntent.setData((Uri.parse(context.getString(R.string.custom_uri_prefix)+System.currentTimeMillis())));
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        Calendar alarmStartTime = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        int day = lesson.getDay();
        day += 2;
        if(day > 7){
            day -= 7;
        }
        if(day < 0){
            day += 7;
        }
        alarmStartTime.set(Calendar.DAY_OF_WEEK, day);
        alarmStartTime.set(Calendar.HOUR_OF_DAY, period.getStartingHour());
        alarmStartTime.set(Calendar.MINUTE, period.getStartingMinute());
        alarmStartTime.set(Calendar.SECOND, 0);
        if (now.after(alarmStartTime)) {
            alarmStartTime.add(Calendar.DATE, 7);
        }

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(), 7*AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }


    static void cancelClassAlarm(Context context, Class lesson) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, TimetableNotificationReceiver.class);
        alarmIntent.setAction(context.getString(R.string.d)+lesson.getDay()+context.getString(R.string.h)+lesson.getHour());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmIntent.setData((Uri.parse(context.getString(R.string.custom_uri_prefix)+System.currentTimeMillis())));
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
