package de.tshw.tools.timetracker.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProjectList {

    private List<Project> projects;

    public ProjectList() {
        this.projects = new ArrayList<Project>();
    }

    public List<Project> getProjects() {
        return Collections.unmodifiableList(projects);
    }

    public void addProject(Project project) {
        this.projects.add(project);
    }

    public void deleteProject(Project project) {
        this.projects.remove(project);
    }

    public long getTotalMillisToday () {
        long totalMillis = 0;
        for (Project p: this.projects) {
            totalMillis += p.getTotalMillisToday();
        }
        return totalMillis;
    }
}
