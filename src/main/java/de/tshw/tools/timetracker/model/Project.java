package de.tshw.tools.timetracker.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private List<WorkingPeriod> workingPeriods = new ArrayList<WorkingPeriod>();

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WorkingPeriod> getWorkingPeriods() {
        return Collections.unmodifiableList(workingPeriods);
    }

    public void addWorkingPeriods(WorkingPeriod workingPeriod) {
        this.workingPeriods.add(workingPeriod);
    }

    public long getTotalMillisToday() {
        long totalMillis = 0;
        Calendar now = Calendar.getInstance();
        for (WorkingPeriod p: this.workingPeriods) {
            if (p.isToday()) {
                totalMillis += p.getDurationInMillis();
            }
        }
        return totalMillis;
    }
}
