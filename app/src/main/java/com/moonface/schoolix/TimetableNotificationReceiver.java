package com.moonface.schoolix;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.TaskStackBuilder;
import com.google.gson.Gson;
import java.util.Calendar;

public class TimetableNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Gson gson = new Gson();
        Class lesson = gson.fromJson(intent.getStringExtra(context.getString(R.string.class_key)), Class.class);
        Period period = DataManager.getPeriod(context, context.getString(R.string.period_data_preferences), lesson.getHour());


        Intent notificationIntent = new Intent(context, ClassViewActivity.class);
        notificationIntent.putExtra(context.getString(R.string.name_key), context.getString(R.string.d)+lesson.getDay()+context.getString(R.string.h)+lesson.getHour());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ClassViewActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);

        Calendar startingTime = Calendar.getInstance();
        startingTime.set(Calendar.HOUR_OF_DAY, period.getStartingHour());
        startingTime.set(Calendar.MINUTE, period.getStartingMinute());


        builder.setAutoCancel(true);
        builder.setContentTitle(lesson.getSubject().getTitle()+context.getString(R.string.space_char)+context.getString(R.string.starting_at_label)+context.getString(R.string.space_char)+Period.simpleHourFormat.format(startingTime.get(Calendar.HOUR_OF_DAY), startingTime.get(Calendar.MINUTE)));
        builder.setContentText(context.getString(R.string.hurry_up_to_room_label)+context.getString(R.string.space_char)+lesson.getSubject().getRoom());
        builder.setTicker(context.getString(R.string.upcoming_class_alert_label));
        builder.setSmallIcon(R.drawable.app_icon);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setColor(context.getResources().getColor(R.color.colorPrimary));
        }
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(context.getString(R.string.timetable_channel_id));
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    context.getString(R.string.timetable_channel_id),
                    context.getString(R.string.timetable_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        if (notificationManager != null) {
            notificationManager.notify(0, notification);
        }
    }
}
