package com.moonface.schoolix;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

public class DashboardFragment extends Fragment {
    RecyclerView upcoming_list;
    RecyclerView schedule_today_list;
    TextView greeting_view;
    ImageView ic_day;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        upcoming_list = view.findViewById(R.id.upcoming_list);
        schedule_today_list = view.findViewById(R.id.schedule_today_list);
        greeting_view = view.findViewById(R.id.greeting_view);
        ic_day = view.findViewById(R.id.ic_day);
        if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 0 && Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 5) {
            greeting_view.setText(R.string.good_evening_label);
            ic_day.setImageDrawable(getResources().getDrawable(R.drawable.moon));
        } else if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 5 && Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 12) {
            greeting_view.setText(R.string.good_morning_label);
            ic_day.setImageDrawable(getResources().getDrawable(R.drawable.sun));
        } else if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 12 && Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 17) {
            greeting_view.setText(R.string.good_afternoon_label);
            ic_day.setImageDrawable(getResources().getDrawable(R.drawable.sun));
        } else if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 17 && Calendar.getInstance().get(Calendar.HOUR_OF_DAY) <= 24) {
            greeting_view.setText(R.string.good_evening_label);
            ic_day.setImageDrawable(getResources().getDrawable(R.drawable.moon));
        }
        initializeScheduleList();
        initializeTasksList();
    }
    private void initializeTasksList() {
        ArrayList<Task> data = DataManager.getTasksArray(Objects.requireNonNull(getContext()), getString(R.string.task_data_preferences));
        ArrayList<Task> dateFiltered = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 1);
            if (data.get(i).getDate().after(calendar.getTime())) {
                dateFiltered.add(data.get(i));
            }
        }
        Collections.sort(dateFiltered, (o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        ArrayList<Task> listFiltered = new ArrayList<>();
        if (dateFiltered.size() > 3) {
            for (int i = 0; i < dateFiltered.size(); i++) {
                Date event_day = dateFiltered.get(i).getDate();
                if (DateUtils.isToday(event_day.getTime() - DateUtils.DAY_IN_MILLIS)) {
                    listFiltered.add(data.get(i));
                }
            }
            if (listFiltered.size() <= 1) {
                for (int i = 0; i < dateFiltered.size(); i++) {
                    Date event_day = dateFiltered.get(i).getDate();
                    if (DateUtils.isToday(event_day.getTime() - 2 * DateUtils.DAY_IN_MILLIS)) {
                        listFiltered.add(data.get(i));
                    }
                }
            }
            if (listFiltered.size() <= 1) {
                listFiltered = dateFiltered;
            }
        } else {
            listFiltered = dateFiltered;
        }
        RecyclerView.Adapter<TaskAdapter.ViewHolder> adapter = new TaskAdapter(true, listFiltered, getContext(), item -> {
            if (getContext() != null) {
                Intent intent = new Intent();
                intent.setClass(getContext(), TaskViewActivity.class);
                if (item.getSubject() != null) {
                    intent.putExtra(getString(R.string.name_key), item.getSubject().getTitle() + getString(R.string.underscore_char) + item.getTitle());
                    startActivity(intent);
                }
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        upcoming_list.setLayoutManager(layoutManager);
        upcoming_list.setItemAnimator(new DefaultItemAnimator());
        upcoming_list.setAdapter(adapter);
    }
    private void initializeScheduleList() {
        ArrayList<Class> data = DataManager.getClassesArray(Objects.requireNonNull(getContext()), getString(R.string.timetable_data_preferences));
        ArrayList<Class> dayFiltered = new ArrayList<>();
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        day -= 2;
        if(day == -1){
            day = 6;
        }
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getDay() == day) {
                dayFiltered.add(data.get(i));
            }
        }
        Collections.sort(dayFiltered, (o1, o2) -> o1.getHour().compareTo(o2.getHour()));
        RecyclerView.Adapter<ScheduleAdapter.ViewHolder> adapter = new ScheduleAdapter(dayFiltered, getContext(), item -> {
            if(getContext() != null) {
                Intent intent = new Intent();
                intent.setClass(getContext(), ClassViewActivity.class);
                if(item.getSubject() != null) {
                    intent.putExtra(getString(R.string.name_key), getString(R.string.d)+String.valueOf(item.getDay())+getString(R.string.h)+String.valueOf(item.getHour()));
                    startActivity(intent);
                }
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        schedule_today_list.setLayoutManager(layoutManager);
        schedule_today_list.setItemAnimator(new DefaultItemAnimator());
        schedule_today_list.setAdapter(adapter);
    }
    public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
        private final Context context;
        private final ArrayList<Class> data;
        private OnItemClickListener listener;

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView title, letter, time_view;
            LinearLayout image;
            View block;
            ViewHolder(View v) {
                super(v);
                block = v.findViewById(R.id.block);
                title = v.findViewById(R.id.title);
                image = v.findViewById(R.id.image);
                letter = v.findViewById(R.id.letter);
                time_view = v.findViewById(R.id.time_view);
            }
            void bind(final Class item, final OnItemClickListener listener) {
                block.setOnClickListener(v -> listener.onItemClick(item));
            }
        }

        ScheduleAdapter(ArrayList<Class> data, Context context, OnItemClickListener listener) {
            this.data = data;
            this.context = context;
            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_block, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ScheduleAdapter.ViewHolder holder, int position) {
            holder.bind(data.get(position), listener);
            holder.block.setAlpha(0);
            ObjectAnimator animator = new ObjectAnimator();
            animator.setTarget(holder.block);
            animator.setFloatValues(0f,1f);
            animator.setDuration(500);
            animator.setProperty(View.ALPHA);
            final Handler handler = new Handler();
            handler.postDelayed(animator::start, 25*position);
            if(data.get(position).getSubject() != null) {
                holder.title.setText(data.get(position).getSubject().getTitle());
                Period period = DataManager.getPeriod(context, getString(R.string.period_data_preferences), data.get(position).getHour());
                if(period != null){
                    holder.time_view.setText(Period.simpleHourFormat.format(period.getStartingHour(), period.getStartingMinute()));
                }
                if (DataManager.getSubject(context, getString(R.string.subject_data_preferences), data.get(position).getSubject().getTitle()) != null) {
                    Drawable bg = context.getResources().getDrawable(Subject.ids[data.get(position).getSubject().getDrawableIndex()]);
                    holder.image.setBackground(bg);
                    holder.letter.setText(String.valueOf(data.get(position).getSubject().getTitle().charAt(0)));
                }
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
    public interface OnItemClickListener {
        void onItemClick(Class item);
    }
    @Override
    public void onStart(){
        super.onStart();
        if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 0 && Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 5) {
            greeting_view.setText(getString(R.string.good_evening_label));
            ic_day.setImageDrawable(getResources().getDrawable(R.drawable.moon));
        } else if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 5 && Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 12) {
            greeting_view.setText(getString(R.string.good_morning_label));
            ic_day.setImageDrawable(getResources().getDrawable(R.drawable.sun));
        } else if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 12 && Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 17) {
            greeting_view.setText(getString(R.string.good_afternoon_label));
            ic_day.setImageDrawable(getResources().getDrawable(R.drawable.sun));
        } else if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 17 && Calendar.getInstance().get(Calendar.HOUR_OF_DAY) <= 24) {
            greeting_view.setText(getString(R.string.good_evening_label));
            ic_day.setImageDrawable(getResources().getDrawable(R.drawable.moon));
        }
        initializeScheduleList();
        initializeTasksList();
    }
}
