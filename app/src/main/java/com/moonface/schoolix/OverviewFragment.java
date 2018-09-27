package com.moonface.schoolix;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.duolingo.open.rtlviewpager.RtlViewPager;

public class OverviewFragment extends Fragment {

    private RtlViewPager viewPager;
    private int[] item_ids;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_timetable:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_tasks:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        item_ids = new int[]{R.id.navigation_dashboard, R.id.navigation_timetable, R.id.navigation_tasks};

        final BottomNavigationView navigation = view.findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager = view.findViewById(R.id.viewpager);
        final PagerAdapter adapter = new PagerAdapter(getFragmentManager(), 3);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                navigation.setSelectedItemId(item_ids[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    @Override
    public void onStart(){
        super.onStart();
    }
}
