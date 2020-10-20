package me.joshmckinney.gradtracker.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "term_table")
public class Term implements Serializable {

    @PrimaryKey (autoGenerate = true)
    private int termId;
    private String termName;
    private Date startDate;
    private Date endDate;

    public Term(String termName, Date startDate, Date endDate) {
        this.termName = termName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public int getTermId() {
        return termId;
    }

    public String getTermName() {
        return termName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

}
