package com.moonface.schoolix;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import static android.content.Intent.EXTRA_TEXT;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar _toolbar = findViewById(R.id._toolbar);
        setSupportActionBar(_toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        _toolbar.setNavigationOnClickListener(_v -> onBackPressed());
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, _toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    menuItem.setChecked(true);
                    drawerLayout.closeDrawers();
                    switch (menuItem.getItemId()) {
                        case R.id.nav_overview:
                            changeFragments(getSupportFragmentManager(), new OverviewFragment(), getString(R.string.overview_tag));
                            break;
                        case R.id.nav_subjects:
                            changeFragments(getSupportFragmentManager(), new SubjectsFragment(), getString(R.string.subjects_tag));
                            break;
                        case R.id.nav_grades:
                            changeFragments(getSupportFragmentManager(), new GradesFragment(), getString(R.string.grades_tag));
                            break;
                        case R.id.nav_settings:
                            Intent settings = new Intent();
                            settings.setClass(this, SettingsActivity.class);
                            startActivity(settings);
                            menuItem.setChecked(false);
                            break;
                        case R.id.nav_share:
                            menuItem.setChecked(false);
                            Intent iShare = new Intent(android.content.Intent.ACTION_SEND);
                            iShare.setType(getString(R.string.text_plain_type));
                            iShare.putExtra(EXTRA_TEXT, getString(R.string.play_store_link));
                            startActivity(Intent.createChooser(iShare,getString(R.string.share_using_label)));
                            break;
                        case R.id.nav_about:
                            menuItem.setChecked(false);
                            Intent about = new Intent();
                            about.setClass(this, AboutActivity.class);
                            startActivity(about);
                            break;
                    }
                    return true;
                });
        changeFragments(getSupportFragmentManager(), new OverviewFragment(), getString(R.string.overview_tag));
        navigationView.setCheckedItem(R.id.nav_overview);
        if(DataManager.getSemestersArray(this,getString(R.string.semester_data_preferences)).size() == 0 || DataManager.getPeriodsArray(this, getString(R.string.period_data_preferences)).size() == 0 || DataManager.getIntPref(this, getString(R.string.general_data_preferences), getString(R.string.first_day_key)) == -1 || DataManager.getIntPref(this, getString(R.string.general_data_preferences), getString(R.string.grading_key)) == -1) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), SetupActivity.class);
            startActivity(intent);
            finish();
        }
        AppRater.appLaunched(this);
    }

    public static void changeFragments(FragmentManager fm, Fragment fragment, String tag) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_placeholder, fragment, tag);
        ft.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onStart(){
        super.onStart();
    }
    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(navigationView)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            OverviewFragment currentFragment = (OverviewFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.overview_tag));
            if(currentFragment == null || !currentFragment.isVisible()) {
                changeFragments(getSupportFragmentManager(), new OverviewFragment(), getString(R.string.overview_tag));
                navigationView.setCheckedItem(R.id.nav_overview);
            } else {
                super.onBackPressed();
            }
        }
    }
}
