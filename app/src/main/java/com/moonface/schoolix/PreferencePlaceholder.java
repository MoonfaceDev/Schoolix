package com.moonface.schoolix;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class PreferencePlaceholder extends AppCompatActivity {
    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_preference);
        android.support.v7.widget.Toolbar _toolbar = findViewById(R.id._toolbar);
        setSupportActionBar(_toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle(getIntent().getStringExtra(getString(R.string.label_key)));
        }
        _toolbar.setNavigationOnClickListener(_v -> onBackPressed());
        Fragment[] fragments = new Fragment[]{new GeneralPrefFragment(), new SemestersPrefFragment(), new PeriodsPrefFragment(), new NotificationPrefFragment(), new BackupAndRestorePrefFragment()};
        setFragment(getSupportFragmentManager(), fragments[getIntent().getIntExtra(getString(R.string.fragment_key), 0)]);
    }
    private void setFragment(FragmentManager fm, Fragment fragment) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_placeholder, fragment);
        ft.commit();
    }

}
