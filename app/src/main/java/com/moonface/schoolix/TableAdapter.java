package com.moonface.schoolix;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {
    private final Context context;
    private ArrayList<Class> data;
    private OnItemClickListener listener;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, teacher, room, letter, hour;
        LinearLayout image;
        LinearLayout block;
        ViewHolder(View v) {
            super(v);
            block = v.findViewById(R.id.block);
            title = v.findViewById(R.id.title);
            teacher = v.findViewById(R.id.teacher);
            room = v.findViewById(R.id.room);
            image = v.findViewById(R.id.image);
            letter = v.findViewById(R.id.letter);
            hour = v.findViewById(R.id.hour);

        }
        void bind(final Class item, final OnItemClickListener listener) {
            block.setOnClickListener(v -> listener.onItemClick(item));
        }
    }

    TableAdapter(ArrayList<Class> data, Context context, OnItemClickListener listener) {
        this.data = data;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TableAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hour_block, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
            holder.teacher.setText(data.get(position).getSubject().getTeacher());
            holder.room.setText(data.get(position).getSubject().getRoom());
            if (DataManager.getSubject(context, context.getString(R.string.subject_data_preferences), data.get(position).getSubject().getTitle()) != null) {
                Drawable bg = context.getResources().getDrawable(Subject.ids[data.get(position).getSubject().getDrawableIndex()]);
                holder.image.setBackground(bg);
                holder.letter.setText(String.valueOf(data.get(position).getSubject().getTitle().charAt(0)));
            }
        }
        holder.hour.setText(String.valueOf(data.get(position).getHour()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public interface OnItemClickListener {
        void onItemClick(Class item);
    }
}
