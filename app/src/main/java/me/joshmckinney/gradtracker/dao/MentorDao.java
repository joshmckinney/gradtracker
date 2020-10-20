package me.joshmckinney.gradtracker.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import me.joshmckinney.gradtracker.model.Mentor;

@Dao
public interface MentorDao {
    @Insert
    void insert(Mentor mentor);
    @Update
    void update(Mentor mentor);
    @Delete
    void delete(Mentor mentor);
    @Query("DELETE FROM mentor_table")
    void deleteAllMentors();
    @Query("SELECT * FROM mentor_table WHERE courseId = :courseId")
    LiveData<List<Mentor>> getCourseMentors(int courseId);
    @Query("SELECT * FROM mentor_table ORDER BY mentorName DESC")
    LiveData<List<Mentor>> getAllMentors();
}
