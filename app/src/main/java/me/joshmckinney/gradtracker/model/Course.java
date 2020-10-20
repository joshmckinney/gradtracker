package me.joshmckinney.gradtracker.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "course_table")
public class Course implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int courseId;
    private String courseName;
    private String status;
    private Date startDate;
    private Date endDate;

    private int termId;

    public Course(String courseName, Date startDate, Date endDate, String status, int termId) {
        this.courseName = courseName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.termId = termId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }

    public int getTermId() {
        return termId;
    }

}
