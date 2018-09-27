package com.moonface.schoolix;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

public class AppRater {

    private final static int DAYS_UNTIL_PROMPT = 2;
    private final static int LAUNCHES_UNTIL_PROMPT = 5;

    public static void appLaunched(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.app_rater_preferences), 0);
        if (prefs.getBoolean(context.getString(R.string.dont_show_again_key), false)) {
            return;
        }
        long launch_count = prefs.getLong(context.getString(R.string.launch_count_key), 0) + 1;
        prefs.edit().putLong(context.getString(R.string.launch_count_key), launch_count).apply();

        Long date_firstLaunch = prefs.getLong(context.getString(R.string.date_first_launch_key), 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            prefs.edit().putLong(context.getString(R.string.date_first_launch_key), date_firstLaunch).apply();
        }

        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(context.getString(R.string.rate_label)+context.getString(R.string.space_char)+context.getString(R.string.app_name));
                builder.setMessage(R.string.rate_message);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setPositiveButton(R.string.rate_now_label, (dialog, which) -> {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(context.getString(R.string.play_store_link)));
                    context.startActivity(intent);
                    prefs.edit().putBoolean(context.getString(R.string.dont_show_again_key), true).apply();
                });
                builder.setNegativeButton(R.string.no_thanks_label, (dialog, which) -> prefs.edit().putBoolean(context.getString(R.string.dont_show_again_key), true).apply());
                builder.setNeutralButton(R.string.later_label, ((dialog, which) -> prefs.edit().putLong(context.getString(R.string.date_first_launch_key), System.currentTimeMillis()).apply()));
                builder.show();
            }
        }
    }
}

