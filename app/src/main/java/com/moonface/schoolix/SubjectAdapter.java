package com.moonface.schoolix;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class SubjectAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Subject> data;

    SubjectAdapter(Context c, ArrayList<Subject> list) {
        context = c;
        data = list;
    }

    public int getCount() {
        return data.size();
    }

    public Subject getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("InflateParams")
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView = convertView;
        if (gridView == null) {
            gridView = inflater != null ? inflater.inflate(R.layout.subject_card, null) : null;
        }
        if(gridView != null){
            LinearLayout image;
            TextView letter, title_view;
            image = gridView.findViewById(R.id.image);
            letter = gridView.findViewById(R.id.letter);
            title_view = gridView.findViewById(R.id.title_view);
            if (data.get(position) != null) {
                Drawable bg = context.getResources().getDrawable(Subject.ids[data.get(position).getDrawableIndex()]);
                if (image != null) {
                    image.setBackground(bg);
                }
                if (letter != null) {
                    letter.setText(String.valueOf(data.get(position).getTitle().charAt(0)));
                }
                if (title_view != null) {
                    title_view.setText(data.get(position).getTitle());
                }
            }
        }
        return gridView;
    }
}
