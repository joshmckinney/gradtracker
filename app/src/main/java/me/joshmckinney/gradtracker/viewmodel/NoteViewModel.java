package me.joshmckinney.gradtracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import me.joshmckinney.gradtracker.database.TrackerRepository;
import me.joshmckinney.gradtracker.model.Note;

public class NoteViewModel extends AndroidViewModel {
    private static TrackerRepository repository;
    private LiveData<List<Note>> allNotes;
    private LiveData<List<Note>> courseNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new TrackerRepository(application);
        allNotes = repository.getAllNotes();
        courseNotes = repository.getCourseNotes();
    }

    public static void insert(Note note) {
        repository.insert(note);
    }
    public static void update(Note note) {
        repository.update(note);
    }
    public void delete(Note note) {
        repository.delete(note);
    }
    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }

    public LiveData<List<Note>> getCourseNotes(int courseId) {
        return courseNotes;
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }
}
