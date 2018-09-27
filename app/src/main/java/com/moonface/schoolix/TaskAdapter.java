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

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private final Context context;
    private final boolean bigCells;
    private ArrayList<Task> data;
    private OnItemClickListener listener;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title_view, date_view, type_view, subject_view, letter;
        LinearLayout image;
        View block;
        ViewHolder(View v) {
            super(v);
            block = v.findViewById(R.id.block);
            title_view = v.findViewById(R.id.title_view);
            date_view = v.findViewById(R.id.date_view);
            type_view = v.findViewById(R.id.type_view);
            if(bigCells) {
                subject_view = v.findViewById(R.id.subject_view);
                image = v.findViewById(R.id.image);
                letter = v.findViewById(R.id.letter);
            }
        }
        void bind(final Task item, final OnItemClickListener listener) {
            block.setOnClickListener(v -> listener.onItemClick(item));
        }
    }

    TaskAdapter(boolean bigCells, ArrayList<Task> data, Context context, OnItemClickListener listener) {
        this.data = data;
        this.context = context;
        this.listener = listener;
        this.bigCells = bigCells;
    }

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if(bigCells) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_block, parent, false);
        }
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
        handler.postDelayed(animator::start, 100*position);
        if(data.get(position).getSubject() != null) {
            holder.title_view.setText(data.get(position).getTitle());
            holder.date_view.setText(Task.dateFormat.format(data.get(position).getDate()));
            holder.type_view.setText(data.get(position).getType());
            if(bigCells) {
                holder.subject_view.setText(data.get(position).getSubject().getTitle());
                if (DataManager.getSubject(context, context.getString(R.string.subject_data_preferences), data.get(position).getSubject().getTitle()) != null) {
                    Drawable bg = context.getResources().getDrawable(Subject.ids[data.get(position).getSubject().getDrawableIndex()]);
                    holder.image.setBackground(bg);
                    holder.letter.setText(String.valueOf(data.get(position).getSubject().getTitle().charAt(0)));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public interface OnItemClickListener {
        void onItemClick(Task item);
    }
}
