package me.joshmckinney.gradtracker.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.joshmckinney.gradtracker.R;
import me.joshmckinney.gradtracker.model.Course;
import me.joshmckinney.gradtracker.model.Course;
import me.joshmckinney.gradtracker.util.DateFormatter;

public class CourseAdapter extends ListAdapter<Course, CourseAdapter.CourseHolder> {
    private onItemClickListener clickListener;
    private onLongClickListener longClickListener;

    public CourseAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Course> DIFF_CALLBACK = new DiffUtil.ItemCallback<Course>() {
        @Override
        public boolean areItemsTheSame(@NonNull Course oldItem, @NonNull Course newItem) {
            return oldItem.getCourseId() == newItem.getCourseId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Course oldItem, @NonNull Course newItem) {
            return oldItem.getCourseName().equals(newItem.getCourseName()) &&
                    oldItem.getStartDate().equals(newItem.getStartDate()) &&
                    oldItem.getEndDate().equals(newItem.getEndDate());
        }
    };

    @NonNull
    @Override
    public CourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_item, parent, false);
        return new CourseHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseHolder holder, int position) {
        Course currentCourse = getItem(position);
        try {
            holder.textViewName.setText(currentCourse.getCourseName());
            holder.textViewStart.setText(DateFormatter.toDate(currentCourse.getStartDate()));
            holder.textViewEnd.setText(DateFormatter.toDate(currentCourse.getEndDate()));
            holder.textViewStatus.setText(currentCourse.getStatus());
        } catch (NullPointerException e) {
            Log.println(Log.INFO, "COURSE DATE", e.getMessage());
        }
    }

    public Course getCourseAt(int position) {
        return getItem(position);
    }

    class CourseHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewStart;
        private TextView textViewEnd;
        private TextView textViewStatus;

        public CourseHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewStart = itemView.findViewById(R.id.text_view_start);
            textViewEnd = itemView.findViewById(R.id.text_view_end);
            textViewStatus = itemView.findViewById(R.id.text_view_status);
            itemView.setOnClickListener((v)-> {
                int position = getAdapterPosition();
                if (clickListener != null && position != RecyclerView.NO_POSITION) {
                    clickListener.onItemClick(getItem(position));
                }
            });
            itemView.setOnLongClickListener((v)-> {
                int position = getAdapterPosition();
                if (longClickListener != null && position != RecyclerView.NO_POSITION) {
                    longClickListener.onItemLongClick(getItem(position));
                }
                return true;
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(Course course);
    }
    public interface onLongClickListener {
        void onItemLongClick(Course course);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.clickListener = listener;
    }
    public void setOnLongClickListener(onLongClickListener listener) {
        this.longClickListener = listener;
    }
}
