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

import me.joshmckinney.gradtracker.R;
import me.joshmckinney.gradtracker.model.Note;
import me.joshmckinney.gradtracker.util.DateFormatter;

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteHolder> {
    private onItemClickListener clickListener;
    private onLongClickListener longClickListener;

    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getNoteId() == newItem.getNoteId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getNoteId() == newItem.getNoteId() &&
                    oldItem.getCourseId() == newItem.getCourseId() &&
                            oldItem.getNoteString().equals(newItem.getNoteString());
        }
    };

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote = getItem(position);
        try {
            holder.textViewNote.setText(currentNote.getNoteString());
        } catch (NullPointerException e) {
            Log.println(Log.INFO, "NULL", e.getMessage());
        }
    }

    public Note getNoteAt(int position) {
        return getItem(position);
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView textViewNote;

        public NoteHolder(View itemView) {
            super(itemView);
            textViewNote = itemView.findViewById(R.id.text_view_note);
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
        void onItemClick(Note note);
    }

    public interface onLongClickListener {
        void onItemLongClick(Note note);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.clickListener = listener;
    }

    public void setOnLongClickListener(onLongClickListener listener) {
        this.longClickListener = listener;
    }
}
