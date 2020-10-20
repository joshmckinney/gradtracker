package me.joshmckinney.gradtracker.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int noteId;
    private int courseId;
    private String noteString;

    public Note(int courseId, String noteString) {
        this.courseId = courseId;
        this.noteString = noteString;
    }

    public int getNoteId() {
        return noteId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getNoteString() {
        return noteString;
    }

    public void setNoteString(String noteString) {
        this.noteString = noteString;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

}
