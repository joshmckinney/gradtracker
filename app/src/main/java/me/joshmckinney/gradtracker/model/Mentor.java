package me.joshmckinney.gradtracker.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "mentor_table")
public class  Mentor implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int mentorId;
    private int courseId;
    private String mentorName;
    private String mentorPhone;
    private String mentorEmail;

    public Mentor(String mentorName, String mentorPhone, String mentorEmail, int courseId) {
        this.mentorName = mentorName;
        this.mentorPhone = mentorPhone;
        this.mentorEmail = mentorEmail;
        this.courseId = courseId;
    }

    public void setMentorId(int mentorId) {
        this.mentorId = mentorId;
    }

    public int getMentorId() {
        return mentorId;
    }

    public int getCourseId() { return courseId; }

    public String getMentorName() {
        return mentorName;
    }

    public String getMentorPhone() {
        return mentorPhone;
    }

    public String getMentorEmail() {
        return mentorEmail;
    }

}
