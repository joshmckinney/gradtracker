package me.joshmckinney.gradtracker.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import me.joshmckinney.gradtracker.model.Course;

@Dao
public interface CourseDao {
    @Insert
    void insert(Course course);
    @Update
    void update(Course course);
    @Delete
    void delete(Course course);
    @Query("DELETE FROM course_table")
    void deleteAllCourses();
    @Query("SELECT * FROM course_table WHERE termId = :termId")
    LiveData<List<Course>> getTermCourses(int termId);
    @Query("SELECT * FROM course_table ORDER BY courseName DESC")
    LiveData<List<Course>> getAllCourses();
}
