package de.tshw.tools.timetracker.controller;

import de.tshw.tools.timetracker.gui.ProjectsTableBean;
import de.tshw.tools.timetracker.gui.TimeTrackerGUI;
import de.tshw.tools.timetracker.model.Project;
import de.tshw.tools.timetracker.model.ProjectList;
import de.tshw.tools.timetracker.model.WorkingPeriod;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.List;

public class GuiController implements ActionListener {

    private TimeTrackerGUI gui;
    private Session dbSession;
    private ProjectList projects;

    private Timer timer = null;

    private Project currentProject;
    private WorkingPeriod currentWorkingPeriod;

    public GuiController (TimeTrackerGUI gui, Session dbSession) {
        this.gui = gui;
        this.dbSession = dbSession;
        this.timer = new Timer(1000, this);

        this.gui.setController(this);

        this.projects = new ProjectList();
        initializeProjectList();
        this.gui.updateUI();
    }

    private void initializeProjectList() {
        Query query = this.dbSession.createQuery("from Project");
        List<Project> projects = query.list();
        for (Project p: projects) {
            this.projects.addProject(p);
        }
        ProjectsTableBean projectsTableBean = new ProjectsTableBean(this.projects);
        this.gui.setProjectsTableData(projectsTableBean);
        if (this.projects.getProjects().size() > 0) {
            this.projectChanged(this.projects.getProjects().get(0));
        }
    }

    public void addProject(String projectName) {
        Transaction transaction = this.dbSession.beginTransaction();
        Project project = new Project();
        project.setName(projectName);
        this.dbSession.persist(project);
        transaction.commit();
        this.projects.addProject(project);
        gui.updateUI();
    }

    public boolean isTimerRunning() {
        return this.timer.isRunning();
    }

    public void startTimer() {
        if (isTimerRunning()) {
            return;
        }

        this.startNewWorkingPeriod();
        this.timer.start();
    }

    public void stopTimer() {
        if (!isTimerRunning()) {
            return;
        }

        this.timer.stop();
        this.endCurrentWorkingPeriod();
    }

    public void tick() {
        this.gui.updateUI();

        if (this.currentWorkingPeriod == null) {
            startNewWorkingPeriod();
        } else {
            if (this.currentWorkingPeriod.getProject() != this.currentProject) {
                endCurrentWorkingPeriod();
                startNewWorkingPeriod();
            }
        }
    }

    private void endCurrentWorkingPeriod() {
        this.currentWorkingPeriod.setEndTime(Calendar.getInstance());
        Transaction transaction = this.dbSession.beginTransaction();
        this.dbSession.persist(this.currentWorkingPeriod);
        transaction.commit();
    }

    private void startNewWorkingPeriod() {
        this.currentWorkingPeriod = new WorkingPeriod();
        this.currentWorkingPeriod.setStartTime(Calendar.getInstance());
        this.currentWorkingPeriod.setProject(this.currentProject);
    }

    public long getTotalMillisPassed() {
        return this.projects.getTotalMillisToday();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Timer) {
            this.tick();
        }
    }

    public void projectChanged(Project project) {
        this.currentProject = project;
    }

    public void windowClosed() {
        if (isTimerRunning()) {
            this.stopTimer();
        }
    }
}
