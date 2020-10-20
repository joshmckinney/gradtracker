package me.joshmckinney.gradtracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import me.joshmckinney.gradtracker.database.TrackerRepository;
import me.joshmckinney.gradtracker.model.Term;

public class TermViewModel extends AndroidViewModel {
    private static TrackerRepository repository;
    private LiveData<List<Term>> allTerms;

    public TermViewModel(@NonNull Application application) {
        super(application);
        repository = new TrackerRepository(application);
        allTerms = repository.getAllTerms();
    }

    public static void insert(Term term) {
        repository.insert(term);
    }
    public void update(Term term) {
        repository.update(term);
    }
    public void delete(Term term) {
        repository.delete(term);
    }
    public void deleteAllTerms() {
        repository.deleteAllTerms();
    }

    public LiveData<List<Term>> getAllTerms() {
        return allTerms;
    }
}
