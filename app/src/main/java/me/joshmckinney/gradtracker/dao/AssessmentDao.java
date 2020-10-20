package me.joshmckinney.gradtracker.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import me.joshmckinney.gradtracker.model.Assessment;
import me.joshmckinney.gradtracker.model.Note;

@Dao
public interface AssessmentDao {
    @Insert
    void insert(Assessment assessment);
    @Update
    void update(Assessment assessment);
    @Delete
    void delete(Assessment assessment);
    @Query("DELETE FROM assessment_table")
    void deleteAllAssessments();

    @Query("SELECT * FROM assessment_table WHERE courseId = :courseId")
    LiveData<List<Assessment>> getCourseAssessments(int courseId);
    @Query("SELECT * FROM assessment_table ORDER BY assessmentName DESC")
    LiveData<List<Assessment>> getAllAssessments();
}
