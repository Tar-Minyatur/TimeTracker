package de.tshw.tools.timetracker.model;

import javax.persistence.*;
import java.util.Calendar;

@Entity
public class WorkingPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private Calendar startTime;

    private Calendar endTime;

    @ManyToOne
    private Project project;

    private String comment;

    public int getId() {
        return id;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
        if (!this.project.getWorkingPeriods().contains(this)) {
            this.project.addWorkingPeriods(this);
        }
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getDurationInMillis() {
        if (this.endTime == null) {
            Calendar now = Calendar.getInstance();
            return (now.getTimeInMillis() - this.startTime.getTimeInMillis());
        } else {
            return (this.endTime.getTimeInMillis() - this.startTime.getTimeInMillis());
        }
    }

    public boolean isToday() {
        Calendar now = Calendar.getInstance();
        return (now.get(Calendar.YEAR) == this.startTime.get(Calendar.YEAR) &&
                (now.get(Calendar.DAY_OF_YEAR) == this.startTime.get(Calendar.DAY_OF_YEAR)));
    }
}
