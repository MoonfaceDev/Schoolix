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
import android.text.format.DateUtils;
import java.util.ArrayList;

public class TaskNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<Task> tasks = DataManager.getTasksArray(context, context.getString(R.string.task_data_preferences));
        ArrayList<Task> dayFiltered = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            if (DateUtils.isToday(tasks.get(i).getDate().getTime() - DateUtils.DAY_IN_MILLIS)) {
                dayFiltered.add(tasks.get(i));
            }
        }
        Intent notificationIntent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);

        String title;
        StringBuilder message;

        if (dayFiltered.size() == 0) {
            title = context.getString(R.string.no_tasks_for_tomorrow_label);
            message = new StringBuilder(context.getString(R.string.enjoy_your_day_label));
        } else {
            title = dayFiltered.size() + context.getString(R.string.space_char) + context.getString(R.string.tasks_for_tomorrow_label);
            message = new StringBuilder();
            for (int i = 0; i < dayFiltered.size(); i++) {
                if (i != 0) {
                    message.append(context.getString(R.string.comma_char)).append(context.getString(R.string.space_char));
                }
                message.append(dayFiltered.get(i).getTitle()).append(context.getString(R.string.space_char)).append(context.getString(R.string.opening_bracket)).append(dayFiltered.get(i).getSubject().getTitle()).append(context.getString(R.string.closing_bracket));
            }
        }

        builder.setAutoCancel(true);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setTicker(context.getString(R.string.upcoming_task_alert_label));
        builder.setSmallIcon(R.drawable.app_icon);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setColor(context.getResources().getColor(R.color.colorPrimary));
        }
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(context.getString(R.string.tasks_channel_id));
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    context.getString(R.string.tasks_channel_id),
                    context.getString(R.string.tasks_channel_name),
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
