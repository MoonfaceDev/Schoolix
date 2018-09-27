package com.moonface.schoolix;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        android.support.v7.widget.Toolbar _toolbar = findViewById(R.id._toolbar);
        setSupportActionBar(_toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        _toolbar.setNavigationOnClickListener(_v -> onBackPressed());
        RecyclerView general_list = findViewById(R.id.general_list);
        general_list.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<Section> generalSections = new ArrayList<>();
        Section general = new Section();
        general.setName(getString(R.string.general_section_label));
        general.setIcon(getResources().getDrawable(R.drawable.ic_settings_black_24dp));
        general.setBg(getResources().getDrawable(R.drawable.subject_icon_blue));
        generalSections.add(general);
        Section semesters = new Section();
        semesters.setName(getString(R.string.semesters_section_label));
        semesters.setIcon(getResources().getDrawable(R.drawable.ic_timetable_black_24dp));
        semesters.setBg(getResources().getDrawable(R.drawable.subject_icon_lightgreen));
        generalSections.add(semesters);
        Section periods = new Section();
        periods.setName(getString(R.string.periods_section_label));
        periods.setIcon(getResources().getDrawable(R.drawable.ic_time_black_24dp));
        periods.setBg(getResources().getDrawable(R.drawable.subject_icon_orange));
        generalSections.add(periods);
        Section notifications = new Section();
        notifications.setName(getString(R.string.notifications_section_label));
        notifications.setIcon(getResources().getDrawable(R.drawable.ic_notifications_black_24dp));
        notifications.setBg(getResources().getDrawable(R.drawable.subject_icon_pink));
        generalSections.add(notifications);
        Section backup = new Section();
        backup.setName(getString(R.string.backup_and_restore_section_label));
        backup.setIcon(getResources().getDrawable(R.drawable.ic_settings_backup_restore_black_24dp));
        backup.setBg(getResources().getDrawable(R.drawable.subject_icon_red));
        generalSections.add(backup);
        general_list.setAdapter(new SettingsAdapter(generalSections, position -> {
            Intent intent = new Intent();
            intent.setClass(this, PreferencePlaceholder.class);
            intent.putExtra(getString(R.string.fragment_key), position);
            intent.putExtra(getString(R.string.label_key), generalSections.get(position).getName());
            startActivity(intent);
        }));
        RecyclerView miscellaneous_list = findViewById(R.id.miscellaneous_list);
        miscellaneous_list.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<Section> miscellaneousSections = new ArrayList<>();
        Section review = new Section();
        review.setName(getString(R.string.rate_us_label));
        review.setIcon(getResources().getDrawable(R.drawable.ic_grade_black_24dp));
        review.setBg(getResources().getDrawable(R.drawable.subject_icon_yellow));
        miscellaneousSections.add(review);
        Section contact = new Section();
        contact.setName(getString(R.string.contact_us_label));
        contact.setIcon(getResources().getDrawable(R.drawable.ic_email_black_24dp));
        contact.setBg(getResources().getDrawable(R.drawable.subject_icon_cyan));
        miscellaneousSections.add(contact);
        Section about = new Section();
        about.setName(getString(R.string.about_label));
        about.setIcon(getResources().getDrawable(R.drawable.ic_about_black_24dp));
        about.setBg(getResources().getDrawable(R.drawable.subject_icon_gray));
        miscellaneousSections.add(about);
        miscellaneous_list.setAdapter(new SettingsAdapter(miscellaneousSections, position -> {
            Intent intent;
            switch (position){
                case 0:
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(getString(R.string.play_store_link)));
                    startActivity(intent);
                    return;
                case 1:
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(getString(R.string.mailto)+getString(R.string.email)));
                    startActivity(intent);
                    return;
                case 2:
                    intent = new Intent();
                    intent.setClass(getApplicationContext(), AboutActivity.class);
                    startActivity(intent);
            }
        }));
    }

    class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {
        private ArrayList<Section> data;
        private OnItemClickListener listener;

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView title_view;
            ImageView image;
            View block;

            ViewHolder(View v) {
                super(v);
                block = v.findViewById(R.id.block);
                title_view = v.findViewById(R.id.title_view);
                image = v.findViewById(R.id.image);
            }

            void bind(final int position, final OnItemClickListener listener) {
                block.setOnClickListener(v -> listener.onItemClick(position));
            }
        }

        SettingsAdapter(ArrayList<Section> data, OnItemClickListener listener) {
            this.data = data;
            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v;
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bind(position, listener);
            if(data.get(position) != null) {
                holder.title_view.setText(data.get(position).getName());
                holder.image.setImageDrawable(data.get(position).getIcon());
                holder.image.setBackground(data.get(position).getBg());
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
