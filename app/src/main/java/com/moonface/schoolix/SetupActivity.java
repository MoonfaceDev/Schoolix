package com.moonface.schoolix;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class SetupActivity extends AppCompatActivity {

    private SetupPagerAdapter setupPagerAdapter;
    private ViewPager viewPager;
    private ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        WelcomeFragment welcomeFragment = new WelcomeFragment();
        ImportDataFragment importDataFragment = new ImportDataFragment();
        SemestersFragment semestersFragment = new SemestersFragment();
        PeriodsFragment periodsFragment = new PeriodsFragment();
        FirstDayFragment firstdayFragment = new FirstDayFragment();
        GradingFragment gradingFragment = new GradingFragment();
        NotificationsFragment notificationsFragment = new NotificationsFragment();
        ArrayList<Fragment> fragmentsList = new ArrayList<>();
        fragmentsList.add(welcomeFragment);
        fragmentsList.add(importDataFragment);
        fragmentsList.add(semestersFragment);
        fragmentsList.add(periodsFragment);
        fragmentsList.add(firstdayFragment);
        fragmentsList.add(gradingFragment);
        fragmentsList.add(notificationsFragment);


        Fragment[] fragments = new Fragment[fragmentsList.size()];
        for (int i = 0; i < fragmentsList.size(); i++) {
            fragments[i] = fragmentsList.get(i);
        }
        setupPagerAdapter = new SetupPagerAdapter(getSupportFragmentManager(), fragments);

        FloatingActionButton forward = findViewById(R.id.forward);
        FloatingActionButton back = findViewById(R.id.back);
        viewPager = findViewById(R.id.container);
        viewPager.setAdapter(setupPagerAdapter);
        progress_bar = findViewById(R.id.progress_bar);
        progress_bar.setMax(setupPagerAdapter.getCount() - 1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                progress_bar.setProgress(position);
                if (viewPager.getCurrentItem() == 0) {
                    back.setVisibility(View.INVISIBLE);
                    progress_bar.setVisibility(View.INVISIBLE);
                } else {
                    back.setVisibility(View.VISIBLE);
                    progress_bar.setVisibility(View.VISIBLE);
                }
                if (viewPager.getCurrentItem() == setupPagerAdapter.getCount() - 1) {
                    forward.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_black_24dp));
                } else {
                    forward.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_forward_black_24dp));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        forward.setOnClickListener(view -> {
            if (viewPager.getCurrentItem() != setupPagerAdapter.getCount() - 1) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            } else {
                boolean canFinish = true;
                if (semestersFragment.getData().size() > 0) {
                    for (Semester semester : semestersFragment.getData()) {
                        DataManager.putSemester(this, getString(R.string.semester_data_preferences), semester.getNumber(), semester);
                    }
                } else {
                    viewPager.setCurrentItem(1);
                    Toast.makeText(this, R.string.have_at_least_one_semester_toast, Toast.LENGTH_SHORT).show();
                    canFinish = false;
                }
                int periodsCount = 0;
                for (Period period : periodsFragment.getData()) {
                    if (period.getTag().equals(Period.TAG)) {
                        periodsCount++;
                    }
                }
                if (periodsCount >= 2 && canFinish) {
                    for (Period period : periodsFragment.getData()) {
                        if (period.getTag().equals(Period.TAG)) {
                            DataManager.putPeriod(this, getString(R.string.period_data_preferences), period.getNumber(), period);
                        }
                        if (period.getTag().equals(Break.TAG)) {
                            DataManager.putBreak(this, getString(R.string.break_data_preferences), period.getNumber(), (Break) period);
                        }
                    }
                } else {
                    viewPager.setCurrentItem(2);
                    Toast.makeText(this, R.string.have_at_least_two_periods_toast, Toast.LENGTH_SHORT).show();
                    canFinish = false;
                }
                DataManager.putIntPref(this, getString(R.string.general_data_preferences), getString(R.string.first_day_key), firstdayFragment.getSelection());
                DataManager.putIntPref(this, getString(R.string.general_data_preferences), getString(R.string.grading_key), gradingFragment.getSelection());
                if (notificationsFragment.isDailyChecked()) {
                    DataManager.putBooleanPref(this, getString(R.string.general_data_preferences), getString(R.string.daily_notification_key), true);
                    DataManager.putIntPref(this, getString(R.string.general_data_preferences), getString(R.string.notification_hour_key), notificationsFragment.getDaily_hour());
                    DataManager.putIntPref(this, getString(R.string.general_data_preferences), getString(R.string.notification_minute_key), notificationsFragment.getDaily_minute());
                    NotificationManager.startDailyAlarm(this, notificationsFragment.getDaily_hour(), notificationsFragment.getDaily_minute());
                }
                if(notificationsFragment.isTimetableChecked()){
                    DataManager.putBooleanPref(this, getString(R.string.general_data_preferences), getString(R.string.timetable_notification_key), true);
                }
                if (canFinish) {
                    Intent intent = new Intent();
                    intent.setClass(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        back.setOnClickListener(view -> viewPager.setCurrentItem(viewPager.getCurrentItem() - 1));
        back.setVisibility(View.INVISIBLE);
        progress_bar.setVisibility(View.INVISIBLE);
    }

    public class SetupPagerAdapter extends FragmentStatePagerAdapter {
        private Fragment[] fragments;
        SetupPagerAdapter(FragmentManager fm, Fragment[] fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }
}
