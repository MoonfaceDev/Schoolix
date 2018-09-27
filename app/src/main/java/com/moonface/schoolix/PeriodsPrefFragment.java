package com.moonface.schoolix;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;


public class PeriodsPrefFragment extends Fragment {
    private RecyclerView.Adapter<PeriodAdapter.ViewHolder> adapter;
    private ArrayList<Period> data;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_periods_pref, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle){
        LinearLayout add_period = view.findViewById(R.id.add_period);
        LinearLayout add_break = view.findViewById(R.id.add_break);
        data = DataManager.getPeriodsArray(Objects.requireNonNull(getContext()), getString(R.string.period_data_preferences));
        data.addAll(DataManager.getBreaksArray(Objects.requireNonNull(getContext()), getString(R.string.break_data_preferences)));
        Collections.sort(data, (o1, o2) -> {
            Calendar c1 = Calendar.getInstance();
            c1.set(Calendar.HOUR_OF_DAY, o1.getStartingHour());
            c1.set(Calendar.MINUTE, o1.getStartingMinute());
            Calendar c2 = Calendar.getInstance();
            c2.set(Calendar.HOUR_OF_DAY, o2.getStartingHour());
            c2.set(Calendar.MINUTE, o2.getStartingMinute());
            return c1.compareTo(c2);
        });
        RecyclerView period_list = view.findViewById(R.id.period_list);
        adapter = new PeriodAdapter(data, item -> {
            if(item.getTag().equals(Period.TAG)) {
                PeriodCreatorDialog periodCreatorDialog = new PeriodCreatorDialog(getActivity(), item, data, false, period1 -> {
                    DataManager.putPeriod(getContext(), getString(R.string.period_data_preferences), period1.getNumber(), period1);
                    ArrayList<Class> classes = DataManager.getClassesArray(getContext(), getString(R.string.timetable_data_preferences));
                    for(Class lesson : classes){
                        if(lesson.getHour() == period1.getNumber()){
                            NotificationManager.startClassAlarm(getContext(), lesson, period1);
                        }
                    }

                    for (int i = 0; i < data.size(); i++) {
                        if (period1.getNumber() == data.get(i).getNumber() && data.get(i).getTag().equals(Period.TAG)) {
                            data.remove(i);
                        }
                    }
                    data.add(period1);
                    Collections.sort(data, (o1, o2) -> {
                        Calendar c1 = Calendar.getInstance();
                        c1.set(Calendar.HOUR_OF_DAY, o1.getStartingHour());
                        c1.set(Calendar.MINUTE, o1.getStartingMinute());
                        Calendar c2 = Calendar.getInstance();
                        c2.set(Calendar.HOUR_OF_DAY, o2.getStartingHour());
                        c2.set(Calendar.MINUTE, o2.getStartingMinute());
                        return c1.compareTo(c2);
                    });
                    adapter.notifyDataSetChanged();
                });
                periodCreatorDialog.show();
            }
            if(item.getTag().equals(Break.TAG)) {
                BreakCreatorDialog breakCreatorDialog = new BreakCreatorDialog(getActivity(), (Break) item, data, false, break1 -> {
                    DataManager.putBreak(getContext(), getString(R.string.break_data_preferences), break1.getNumber(), break1);
                    for (int i = 0; i < data.size(); i++) {
                        if (break1.getNumber() == data.get(i).getNumber() && data.get(i).getTag().equals(Break.TAG)) {
                            data.remove(i);
                        }
                    }
                    data.add(break1);
                    Collections.sort(data, (o1, o2) -> {
                        Calendar c1 = Calendar.getInstance();
                        c1.set(Calendar.HOUR_OF_DAY, o1.getStartingHour());
                        c1.set(Calendar.MINUTE, o1.getStartingMinute());
                        Calendar c2 = Calendar.getInstance();
                        c2.set(Calendar.HOUR_OF_DAY, o2.getStartingHour());
                        c2.set(Calendar.MINUTE, o2.getStartingMinute());
                        return c1.compareTo(c2);
                    });
                    adapter.notifyDataSetChanged();
                });
                breakCreatorDialog.show();
            }
        });
        period_list.setLayoutManager(new LinearLayoutManager(getContext()));
        period_list.setAdapter(adapter);
        period_list.setLayoutManager(new LinearLayoutManager(getContext()));
        period_list.setAdapter(adapter);
        add_period.setOnClickListener(v -> {
            PeriodCreatorDialog periodCreatorDialog = new PeriodCreatorDialog(getActivity(), getLastPeriod(data), data, true, period1 -> {
                DataManager.putPeriod(getContext(), getString(R.string.period_data_preferences), period1.getNumber(), period1);
                ArrayList<Class> classes = DataManager.getClassesArray(getContext(), getString(R.string.timetable_data_preferences));
                for(Class lesson : classes){
                    if(lesson.getHour() == period1.getNumber()){
                        NotificationManager.startClassAlarm(getContext(), lesson, period1);
                    }
                }
                for (int i = 0; i < data.size(); i++) {
                    if (period1.getNumber() == data.get(i).getNumber() && data.get(i).getTag().equals(Period.TAG)) {
                        data.remove(i);
                    }
                }
                data.add(period1);
                Collections.sort(data, (o1, o2) -> {
                    Calendar c1 = Calendar.getInstance();
                    c1.set(Calendar.HOUR_OF_DAY, o1.getStartingHour());
                    c1.set(Calendar.MINUTE, o1.getStartingMinute());
                    Calendar c2 = Calendar.getInstance();
                    c2.set(Calendar.HOUR_OF_DAY, o2.getStartingHour());
                    c2.set(Calendar.MINUTE, o2.getStartingMinute());
                    return c1.compareTo(c2);
                });
                adapter.notifyDataSetChanged();
            });
            periodCreatorDialog.show();
        });
        add_break.setOnClickListener(v -> {
            BreakCreatorDialog breakCreatorDialog = new BreakCreatorDialog(getActivity(), getLastBreak(data), data, true, break1 -> {
                DataManager.putBreak(getContext(), getString(R.string.break_data_preferences), break1.getNumber(), break1);
                for (int i = 0; i < data.size(); i++) {
                    if (break1.getNumber() == data.get(i).getNumber() && data.get(i).getTag().equals(Break.TAG)) {
                        data.remove(i);
                    }
                }
                data.add(break1);
                Collections.sort(data, (o1, o2) -> {
                    Calendar c1 = Calendar.getInstance();
                    c1.set(Calendar.HOUR_OF_DAY, o1.getStartingHour());
                    c1.set(Calendar.MINUTE, o1.getStartingMinute());
                    Calendar c2 = Calendar.getInstance();
                    c2.set(Calendar.HOUR_OF_DAY, o2.getStartingHour());
                    c2.set(Calendar.MINUTE, o2.getStartingMinute());
                    return c1.compareTo(c2);
                });
                adapter.notifyDataSetChanged();
            });
            breakCreatorDialog.show();
        });
    }
    class PeriodAdapter extends RecyclerView.Adapter<PeriodAdapter.ViewHolder> {
        private ArrayList<Period> data;
        private OnItemClickListener listener;

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView title_view, starting_hour_view, ending_hour_view;
            View block, delete_view;

            ViewHolder(View v) {
                super(v);
                title_view = v.findViewById(R.id.title_view);
                starting_hour_view = v.findViewById(R.id.starting_hour_view);
                ending_hour_view = v.findViewById(R.id.ending_hour_view);
                block = v.findViewById(R.id.block);
                delete_view = v.findViewById(R.id.delete_view);
            }

            void bind(final Period item, final OnItemClickListener listener) {
                block.setOnClickListener(v -> listener.onItemClick(item));
            }
        }

        PeriodAdapter(ArrayList<Period> data, OnItemClickListener listener) {
            this.data = data;
            this.listener = listener;
        }

        @NonNull
        @Override
        public PeriodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v;
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.period_block, parent, false);
            return new PeriodAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull PeriodAdapter.ViewHolder holder, int position) {
            holder.bind(data.get(position), listener);
            if (data.get(position).getNumber() != 0) {
                if(data.get(position).getTag().equals(Period.TAG)) {
                    holder.title_view.setText(getOrdinalNumber(data.get(position).getNumber()).concat(getString(R.string.space_char)).concat(getString(R.string.period_label)));
                    if(Locale.getDefault().getLanguage().equals(getString(R.string.hebrew_code))) {
                        holder.title_view.setText(getString(R.string.period_label).concat(getString(R.string.space_char)).concat(getOrdinalNumber(data.get(position).getNumber())));
                    }
                }
                if(data.get(position).getTag().equals(Break.TAG)) {
                    holder.title_view.setText(getOrdinalNumber(data.get(position).getNumber()).concat(getString(R.string.space_char)).concat(getString(R.string.break_label)));
                    if(Locale.getDefault().getLanguage().equals(getString(R.string.hebrew_code))) {
                        holder.title_view.setText(getString(R.string.break_label).concat(getString(R.string.space_char)).concat(getOrdinalNumber(data.get(position).getNumber())));
                    }
                }
                holder.starting_hour_view.setText(Period.simpleHourFormat.format(data.get(position).getStartingHour(), data.get(position).getStartingMinute()));
                holder.ending_hour_view.setText(Period.simpleHourFormat.format(data.get(position).getEndingHour(), data.get(position).getEndingMinute()));
                holder.delete_view.setOnClickListener(v -> deleteItem(position, data.get(position)));
            }
            int periodCount = 0;
            for(int i=0;i<data.size();i++){
                if (data.get(i).getTag().equals(Period.TAG)) {
                    periodCount++;
                }
            }
            if(periodCount <= 2){
                if(data.get(position).getTag().equals(Period.TAG)) {
                    holder.delete_view.setVisibility(View.INVISIBLE);
                } else {
                    holder.delete_view.setVisibility(View.VISIBLE);
                }
            } else {
                holder.delete_view.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

    }
    public interface OnItemClickListener {
        void onItemClick(Period item);
    }
    private String getOrdinalNumber(int number){
        String[] suffixes = getResources().getStringArray(R.array.suffixes);
        switch (number % 100) {
            case 11:
            case 12:
            case 13:
                return number + suffixes[0];
            default:
                return number + suffixes[number % 10];

        }
    }
    private void deleteItem(int position, Period item){
        if(item.getTag().equals(Period.TAG)){
            data.remove(position);
            DataManager.clearObject(Objects.requireNonNull(getContext()), getString(R.string.period_data_preferences), String.valueOf(item.getNumber()));
            ArrayList<Class> classes = DataManager.getClassesArray(getContext(), getString(R.string.timetable_data_preferences));
            int last_period=0;
            for (Period period : data){
                if(period.getNumber() > last_period){
                    last_period = period.getNumber();
                }
            }
            for(Class lesson : classes){
                if(lesson.getHour() == last_period){
                    DataManager.clearObject(getContext(), getString(R.string.timetable_data_preferences), getString(R.string.d)+lesson.getDay()+getString(R.string.h)+lesson.getHour());
                    NotificationManager.cancelClassAlarm(getContext(), lesson);
                }
            }
            for(int i=0;i<data.size();i++){
                if(data.get(i).getTag().equals(Period.TAG)) {
                    if (data.get(i).getNumber() > item.getNumber()) {
                        DataManager.clearObject(Objects.requireNonNull(getContext()), getString(R.string.period_data_preferences), String.valueOf(data.get(i).getNumber()));
                        data.get(i).setNumber(data.get(i).getNumber()-1);
                        DataManager.putPeriod(Objects.requireNonNull(getContext()), getString(R.string.period_data_preferences), data.get(i).getNumber(), data.get(i));
                        for(Class lesson : classes){
                            if(lesson.getHour() == data.get(i).getNumber()){
                                NotificationManager.startClassAlarm(getContext(), lesson, data.get(i));
                            }
                        }
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }
        if(item.getTag().equals(Break.TAG)){
            data.remove(position);
            DataManager.clearObject(Objects.requireNonNull(getContext()), getString(R.string.break_data_preferences), String.valueOf(item.getNumber()));
            for(int i=0;i<data.size();i++){
                if(data.get(i).getTag().equals(Break.TAG)) {
                    if (data.get(i).getNumber() > item.getNumber()) {
                        DataManager.clearObject(Objects.requireNonNull(getContext()), getString(R.string.break_data_preferences), String.valueOf(data.get(i).getNumber()));
                        data.get(i).setNumber(data.get(i).getNumber()-1);
                        DataManager.putBreak(Objects.requireNonNull(getContext()), getString(R.string.break_data_preferences), data.get(i).getNumber(), (Break) data.get(i));
                        DataManager.putBreak(Objects.requireNonNull(getContext()), getString(R.string.break_data_preferences), data.get(i).getNumber(), (Break) data.get(i));
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }
    }
    private Period getLastPeriod(ArrayList<Period> data) {
        int lastPeriod = 0;
        for(int i=0;i<data.size();i++){
            if(data.get(i).getTag().equals(Period.TAG)){
                if(data.get(i).getNumber() > lastPeriod){
                    lastPeriod = data.get(i).getNumber();
                }
            }
        }
        Collections.sort(data, (o1, o2) -> {
            Calendar c1 = Calendar.getInstance();
            c1.set(Calendar.HOUR_OF_DAY, o1.getStartingHour());
            c1.set(Calendar.MINUTE, o1.getStartingMinute());
            Calendar c2 = Calendar.getInstance();
            c2.set(Calendar.HOUR_OF_DAY, o2.getStartingHour());
            c2.set(Calendar.MINUTE, o2.getStartingMinute());
            return c2.compareTo(c1);
        });
        if (data.size() > 0) {
            Period period = new Period(lastPeriod, Period.TAG);
            period.setStartingHour(data.get(0).getStartingHour());
            period.setStartingMinute(data.get(0).getStartingMinute());
            period.setEndingHour(data.get(0).getEndingHour());
            period.setEndingMinute(data.get(0).getEndingMinute());
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, period.getEndingHour());
            calendar.set(Calendar.MINUTE, period.getEndingMinute());
            calendar.add(Calendar.MINUTE, 45);
            period.setNumber(period.getNumber() + 1);
            period.setStartingHour(period.getEndingHour());
            period.setStartingMinute(period.getEndingMinute());
            period.setEndingHour(calendar.get(Calendar.HOUR_OF_DAY));
            period.setEndingMinute(calendar.get(Calendar.MINUTE));
            return period;
        }
        Period period = new Period(1, Period.TAG);
        period.setStartingHour(8);
        period.setStartingMinute(0);
        period.setEndingHour(8);
        period.setEndingMinute(45);
        return period;
    }
    private Break getLastBreak(ArrayList<Period> data) {
        int lastBreak = 0;
        for(int i=0;i<data.size();i++){
            if(data.get(i).getTag().equals(Break.TAG)){
                if(data.get(i).getNumber() > lastBreak){
                    lastBreak = data.get(i).getNumber();
                }
            }
        }
        Collections.sort(data, (o1, o2) -> {
            Calendar c1 = Calendar.getInstance();
            c1.set(Calendar.HOUR_OF_DAY, o1.getStartingHour());
            c1.set(Calendar.MINUTE, o1.getStartingMinute());
            Calendar c2 = Calendar.getInstance();
            c2.set(Calendar.HOUR_OF_DAY, o2.getStartingHour());
            c2.set(Calendar.MINUTE, o2.getStartingMinute());
            return c2.compareTo(c1);
        });
        if (data.size() > 0) {
            Period period = new Break(lastBreak, Break.TAG);
            period.setStartingHour(data.get(0).getStartingHour());
            period.setStartingMinute(data.get(0).getStartingMinute());
            period.setEndingHour(data.get(0).getEndingHour());
            period.setEndingMinute(data.get(0).getEndingMinute());
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, period.getEndingHour());
            calendar.set(Calendar.MINUTE, period.getEndingMinute());
            calendar.add(Calendar.MINUTE, 15);
            period.setNumber(period.getNumber() + 1);
            period.setStartingHour(period.getEndingHour());
            period.setStartingMinute(period.getEndingMinute());
            period.setEndingHour(calendar.get(Calendar.HOUR_OF_DAY));
            period.setEndingMinute(calendar.get(Calendar.MINUTE));
            return (Break) period;
        }
        Break period = new Break(1, Break.TAG);
        period.setStartingHour(8);
        period.setStartingMinute(0);
        period.setEndingHour(8);
        period.setEndingMinute(15);
        return period;
    }
}
