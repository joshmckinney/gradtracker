package me.joshmckinney.gradtracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import me.joshmckinney.gradtracker.database.TrackerRepository;
import me.joshmckinney.gradtracker.model.Course;

public class CourseViewModel extends AndroidViewModel {
    private static TrackerRepository repository;
    private LiveData<List<Course>> allCourses;
    private LiveData<List<Course>> termCourses;

    public CourseViewModel(@NonNull Application application) {
        super(application);
        repository = new TrackerRepository(application);
        allCourses = repository.getAllCourses();
        termCourses = repository.getTermCourses();
    }

    public static void insert(Course course) {
        repository.insert(course);
    }
    public void update(Course course) {
        repository.update(course);
    }
    public void delete(Course course) {
        repository.delete(course);
    }
    public void deleteAllCourses() {
        repository.deleteAllCourses();
    }

    public LiveData<List<Course>> getTermCourses(int termId) {
        return termCourses;
    }

    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    }
}
