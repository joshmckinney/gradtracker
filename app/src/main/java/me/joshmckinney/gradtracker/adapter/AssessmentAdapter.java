package me.joshmckinney.gradtracker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Dao;

import me.joshmckinney.gradtracker.R;
import me.joshmckinney.gradtracker.model.Assessment;
import me.joshmckinney.gradtracker.util.DateFormatter;

@Dao
public class AssessmentAdapter extends ListAdapter<Assessment, AssessmentAdapter.AssessmentHolder> {
    private onItemClickListener clickListener;
    private onLongClickListener longClickListener;

    public AssessmentAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Assessment> DIFF_CALLBACK = new DiffUtil.ItemCallback<Assessment>() {
        @Override
        public boolean areItemsTheSame(@NonNull Assessment oldItem, @NonNull Assessment newItem) {
            return oldItem.getAssessmentId() == newItem.getAssessmentId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Assessment oldItem, @NonNull Assessment newItem) {
            return oldItem.getAssessmentName().equals(newItem.getAssessmentName());
        }
    };

    @NonNull
    @Override
    public AssessmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assessment_item, parent, false);
        return new AssessmentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentHolder holder, int position) {
        Assessment currentAssessment = getItem(position);
        holder.textViewName.setText(currentAssessment.getAssessmentName());
        if(currentAssessment.getAssessmentType() == 0) {
            holder.textViewType.setText("[OA]");
        } else {
            holder.textViewType.setText("[PA]");
        }
        holder.textViewGoal.setText(DateFormatter.toDate(currentAssessment.getGoalDate()));

    }

    public Assessment getAssessmentAt(int position) {
        return getItem(position);
    }

    class AssessmentHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewType;
        private TextView textViewGoal;

        public AssessmentHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewType = itemView.findViewById(R.id.text_view_type);
            textViewGoal = itemView.findViewById(R.id.text_view_goal);
            itemView.setOnClickListener((v) -> {
                int position = getAdapterPosition();
                if (clickListener != null && position != RecyclerView.NO_POSITION) {
                    clickListener.onItemClick(getItem(position));
                }
            });
            itemView.setOnLongClickListener((v) -> {
                int position = getAdapterPosition();
                if (longClickListener != null && position != RecyclerView.NO_POSITION) {
                    longClickListener.onItemLongClick(getItem(position));
                }
                return true;
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(Assessment assessment);
    }

    public interface onLongClickListener {
        void onItemLongClick(Assessment assessment);
    }

    public void setOnItemClickListener(AssessmentAdapter.onItemClickListener listener) {
        this.clickListener = listener;
    }

    public void setOnLongClickListener(onLongClickListener listener) {
        this.longClickListener = listener;
    }
}