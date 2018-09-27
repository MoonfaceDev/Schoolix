package com.moonface.schoolix;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NotificationService extends Service {
    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationManager.startDailyAlarm(this, DataManager.getIntPref(this, getString(R.string.general_data_preferences), getString(R.string.notification_hour_key)),DataManager.getIntPref(this, getString(R.string.general_data_preferences), getString(R.string.notification_minute_key)));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }
}