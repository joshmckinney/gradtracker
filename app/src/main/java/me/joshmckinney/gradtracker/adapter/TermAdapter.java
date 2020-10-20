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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.joshmckinney.gradtracker.R;
import me.joshmckinney.gradtracker.model.Term;
import me.joshmckinney.gradtracker.util.DateFormatter;

public class TermAdapter extends ListAdapter<Term, TermAdapter.TermHolder> {
    private onItemClickListener clickListener;
    private onLongClickListener longClickListener;

    public TermAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Term> DIFF_CALLBACK = new DiffUtil.ItemCallback<Term>() {
        @Override
        public boolean areItemsTheSame(@NonNull Term oldItem, @NonNull Term newItem) {
            return oldItem.getTermId() == newItem.getTermId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Term oldItem, @NonNull Term newItem) {
            return oldItem.getTermName().equals(newItem.getTermName()) &&
                    oldItem.getStartDate().equals(newItem.getStartDate()) &&
                            oldItem.getEndDate().equals(newItem.getEndDate());
        }
    };

    @NonNull
    @Override
    public TermHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.term_item, parent, false);
        return new TermHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TermHolder holder, int position) {
        Term currentTerm = getItem(position);
        try {
            holder.textViewName.setText(currentTerm.getTermName());
            holder.textViewStart.setText(DateFormatter.toDate(currentTerm.getStartDate()));
            holder.textViewEnd.setText(DateFormatter.toDate(currentTerm.getEndDate()));
        } catch (NullPointerException e) {
            Log.println(Log.INFO, "NULL", e.getMessage());
        }
    }

    public Term getTermAt(int position) {
        return getItem(position);
    }

    class TermHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewStart;
        private TextView textViewEnd;

        public TermHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewStart = itemView.findViewById(R.id.text_view_start);
            textViewEnd = itemView.findViewById(R.id.text_view_end);
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
        void onItemClick(Term term);
    }

    public interface onLongClickListener {
        void onItemLongClick(Term term);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.clickListener = listener;
    }

    public void setOnLongClickListener(onLongClickListener listener) {
        this.longClickListener = listener;
    }
}
