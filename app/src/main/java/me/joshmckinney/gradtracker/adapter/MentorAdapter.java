package me.joshmckinney.gradtracker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import me.joshmckinney.gradtracker.R;
import me.joshmckinney.gradtracker.model.Mentor;

public class MentorAdapter extends ListAdapter<Mentor, MentorAdapter.MentorHolder> {
    private onItemClickListener listener;

    public MentorAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Mentor> DIFF_CALLBACK = new DiffUtil.ItemCallback<Mentor>() {
        @Override
        public boolean areItemsTheSame(@NonNull Mentor oldItem, @NonNull Mentor newItem) {
            return oldItem.getMentorId() == newItem.getMentorId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Mentor oldItem, @NonNull Mentor newItem) {
            return oldItem.getMentorName().equals(newItem.getMentorName()) &&
                    oldItem.getMentorPhone().equals(newItem.getMentorPhone()) &&
                    oldItem.getMentorEmail().equals(newItem.getMentorEmail());
        }
    };

    @NonNull
    @Override
    public MentorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mentor_item, parent, false);
        return new MentorHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MentorHolder holder, int position) {
        Mentor currentMentor = getItem(position);
        holder.textViewName.setText(currentMentor.getMentorName());
        holder.textViewPhone.setText(currentMentor.getMentorPhone());
        holder.textViewEmail.setText(currentMentor.getMentorEmail());
    }

    public Mentor getMentorAt(int position) {
        return getItem(position);
    }

    class MentorHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewPhone;
        private TextView textViewEmail;

        public MentorHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewPhone = itemView.findViewById(R.id.text_view_phone);
            textViewEmail = itemView.findViewById(R.id.text_view_email);
            itemView.setOnClickListener((v)-> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(Mentor mentor);
    }

    public void setOnLongClickListener(onItemClickListener onItemClickListener) {
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }
}
