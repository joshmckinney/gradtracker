package me.joshmckinney.gradtracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import me.joshmckinney.gradtracker.database.TrackerRepository;
import me.joshmckinney.gradtracker.model.Assessment;

public class AssessmentViewModel extends AndroidViewModel {
    private static TrackerRepository repository;
    private LiveData<List<Assessment>> courseAssessments;
    private LiveData<List<Assessment>> allAssessments;

    public AssessmentViewModel(@NonNull Application application) {
        super(application);
        repository = new TrackerRepository(application);
        courseAssessments = repository.getCourseAssessments();
        allAssessments = repository.getAllAssessments();
    }

    public static void insert(Assessment assessment) {
        repository.insert(assessment);
    }
    public void update(Assessment assessment) {
        repository.update(assessment);
    }
    public void delete(Assessment assessment) {
        repository.delete(assessment);
    }
    public void deleteAllAssessments() {
        repository.deleteAllAssessments();
    }

    public LiveData<List<Assessment>> getCourseAssessments(int courseId) { return courseAssessments; }
    public LiveData<List<Assessment>> getAllAssessments() { return allAssessments; }
}
