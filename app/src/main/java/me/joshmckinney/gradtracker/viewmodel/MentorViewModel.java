package me.joshmckinney.gradtracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import me.joshmckinney.gradtracker.database.TrackerRepository;
import me.joshmckinney.gradtracker.model.Course;
import me.joshmckinney.gradtracker.model.Mentor;

public class MentorViewModel extends AndroidViewModel {
    private static TrackerRepository repository;
    private LiveData<List<Mentor>> allMentors;
    private LiveData<List<Mentor>> courseMentors;

    public MentorViewModel(@NonNull Application application) {
        super(application);
        repository = new TrackerRepository(application);
        allMentors = repository.getAllMentors();
        courseMentors = repository.getCourseMentors();
    }

    public static void insert(Mentor mentor) {
        repository.insert(mentor);
    }
    public void update(Mentor mentor) {
        repository.update(mentor);
    }
    public void delete(Mentor mentor) {
        repository.delete(mentor);
    }
    public void deleteAllMentors() {
        repository.deleteAllMentors();
    }

    public LiveData<List<Mentor>> getCourseMentors(int courseId) {
        return courseMentors;
    }
    public LiveData<List<Mentor>> getAllMentors() {
        return allMentors;
    }
}