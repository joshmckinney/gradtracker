package me.joshmckinney.gradtracker.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "assessment_table")
public class Assessment implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int assessmentId;
    private int courseId;
    private String assessmentName;
    private int assessmentType;
    private Date goalDate;

    public Assessment(int courseId, String assessmentName, int assessmentType, Date goalDate) {
        this.courseId = courseId;
        this.assessmentName = assessmentName;
        this.assessmentType = assessmentType;
        this.goalDate = goalDate;
    }

    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    public int getAssessmentId() {
        return assessmentId;
    }
    public int getCourseId() { return courseId; }
    public String getAssessmentName() { return assessmentName; }
    public int getAssessmentType() { return assessmentType; }
    public Date getGoalDate() { return goalDate; }

}
