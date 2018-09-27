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

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.ViewHolder> {
    private final Context context;
    private final boolean bigCells;
    private ArrayList<Grade> data;
    private OnItemClickListener listener;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title_view, date_view, type_view, subject_view, grade_view, letter;
        LinearLayout image;
        View block;
        ViewHolder(View v) {
            super(v);
            block = v.findViewById(R.id.block);
            title_view = v.findViewById(R.id.title_view);
            date_view = v.findViewById(R.id.date_view);
            grade_view = v.findViewById(R.id.grade_view);
            image = v.findViewById(R.id.image);
            if(bigCells) {
                subject_view = v.findViewById(R.id.subject_view);
                type_view = v.findViewById(R.id.type_view);
                letter = v.findViewById(R.id.letter);
            }
        }
        void bind(final Grade item, final OnItemClickListener listener) {
            block.setOnClickListener(v -> listener.onItemClick(item));
        }
    }

    GradeAdapter(boolean bigCells, ArrayList<Grade> data, Context context, OnItemClickListener listener) {
        this.data = data;
        this.context = context;
        this.listener = listener;
        this.bigCells = bigCells;
    }

    @NonNull
    @Override
    public GradeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if(bigCells) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grade_card, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grade_block, parent, false);
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
            holder.date_view.setText(Grade.dateFormat.format(data.get(position).getDate()));
            holder.grade_view.setText(Grade.format(data.get(position).getGrading(), data.get(position).getGrade()));

            if (DataManager.getSubject(context, context.getString(R.string.subject_data_preferences), data.get(position).getSubject().getTitle()) != null) {
                Drawable bg = context.getResources().getDrawable(Subject.ids[data.get(position).getSubject().getDrawableIndex()]);
                holder.image.setBackground(bg);
            }
            if (bigCells) {
                holder.type_view.setText(data.get(position).getType());
                holder.subject_view.setText(data.get(position).getSubject().getTitle());
                holder.letter.setText(String.valueOf(data.get(position).getSubject().getTitle().charAt(0)));
            } else {
                holder.title_view.setText(data.get(position).getTitle().concat(context.getString(R.string.space_char)).concat(context.getString(R.string.opening_bracket)).concat(Grade.format(Grade.Grading.ONE_TO_HUNDRED, data.get(position).getWeight())).concat(context.getString(R.string.percents_char)).concat(context.getString(R.string.closing_bracket)));
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public interface OnItemClickListener {
        void onItemClick(Grade item);
    }
}
