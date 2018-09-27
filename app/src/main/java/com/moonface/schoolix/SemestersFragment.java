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

public class SemestersFragment extends Fragment {
    private RecyclerView.Adapter<SemesterAdapter.ViewHolder> adapter;
    private ArrayList<Semester> data;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.setup_semesters, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        LinearLayout add_semester = view.findViewById(R.id.add_semester);
        data = new ArrayList<>();
        Calendar calendar;
        Semester semester;
        semester = new Semester(1);
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        semester.setStartingDate(calendar.getTime());
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        semester.setEndingDate(calendar.getTime());
        data.add(semester);
        semester = new Semester(2);
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        semester.setStartingDate(calendar.getTime());
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
        calendar.set(Calendar.MONTH, 5);
        calendar.set(Calendar.DAY_OF_MONTH, 30);
        semester.setEndingDate(calendar.getTime());
        data.add(semester);

        RecyclerView semester_list = view.findViewById(R.id.semester_list);
        adapter = new SemesterAdapter(data, item -> {
            SemesterCreatorDialog semesterCreatorDialog = new SemesterCreatorDialog(getActivity(), item, data, false, semester1 -> {
                for(int i=0;i<data.size();i++) {
                    if(semester1.getNumber() == data.get(i).getNumber()){
                        data.remove(i);
                    }
                }
                data.add(semester1);
                Collections.sort(data, (o1, o2) -> o1.getNumber()-o2.getNumber());
                adapter.notifyDataSetChanged();
            });
            semesterCreatorDialog.show();
        });
        semester_list.setLayoutManager(new LinearLayoutManager(getContext()));
        semester_list.setAdapter(adapter);
        add_semester.setOnClickListener(v -> {
            SemesterCreatorDialog semesterCreatorDialog = new SemesterCreatorDialog(getActivity(),getLastSemester(data), data, true, (semester1) -> {
                for(int i=0;i<data.size();i++) {
                    if(semester1.getNumber() == data.get(i).getNumber()){
                        data.remove(i);
                    }
                }
                data.add(semester1);
                Collections.sort(data, (o1, o2) -> o1.getNumber()-o2.getNumber());
                adapter.notifyDataSetChanged();
            });
            semesterCreatorDialog.show();
        });
    }

    private Semester getLastSemester(ArrayList<Semester> data) {
        Collections.sort(data, (o1, o2) -> o2.getNumber()-o1.getNumber());
        Semester semester = new Semester(data.get(0).getNumber()+1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data.get(0).getEndingDate());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        semester.setStartingDate(calendar.getTime());
        calendar.setTime(data.get(0).getEndingDate());
        calendar.add(Calendar.MONTH, 10 / (data.size() + 1));
        semester.setEndingDate(calendar.getTime());
        return semester;
    }


    class SemesterAdapter extends RecyclerView.Adapter<SemesterAdapter.ViewHolder> {
        private ArrayList<Semester> data;
        private OnItemClickListener listener;

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView title_view, starting_date_view, ending_date_view;
            View block, delete_view;

            ViewHolder(View v) {
                super(v);
                title_view = v.findViewById(R.id.title_view);
                starting_date_view = v.findViewById(R.id.starting_date_view);
                ending_date_view = v.findViewById(R.id.ending_date_view);
                block = v.findViewById(R.id.block);
                delete_view = v.findViewById(R.id.delete_view);

            }

            void bind(final Semester item, final OnItemClickListener listener) {
                block.setOnClickListener(v -> listener.onItemClick(item));
            }
        }

        SemesterAdapter(ArrayList<Semester> data, OnItemClickListener listener) {
            this.data = data;
            this.listener = listener;
        }

        @NonNull
        @Override
        public SemesterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v;
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.semester_block, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bind(data.get(position), listener);
            if (data.get(position).getNumber() != 0) {
                holder.title_view.setText(getOrdinalNumber(data.get(position).getNumber()).concat(getString(R.string.space_char)).concat(getString(R.string.semester_label)));
                if (Locale.getDefault().getLanguage().equals(getString(R.string.hebrew_code))) {
                    holder.title_view.setText(getString(R.string.semester_label).concat(getString(R.string.space_char)).concat(getOrdinalNumber(data.get(position).getNumber())));
                }
                holder.starting_date_view.setText(Semester.dateFormat.format(data.get(position).getStartingDate()));
                holder.ending_date_view.setText(Semester.dateFormat.format(data.get(position).getEndingDate()));
                holder.delete_view.setOnClickListener(v -> deleteItem(position, data.get(position)));
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

    }
    public interface OnItemClickListener {
        void onItemClick(Semester item);
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

    private void deleteItem(int position, Semester item) {
        data.remove(position);
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getNumber() > item.getNumber()) {
                data.get(i).setNumber(data.get(i).getNumber() - 1);
            }

        }
        adapter.notifyDataSetChanged();
    }

    public ArrayList<Semester> getData() {
        return data;
    }
}

