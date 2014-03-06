package de.tshw.tools.timetracker.gui;

import de.tshw.tools.timetracker.model.Project;
import de.tshw.tools.timetracker.model.ProjectList;
import de.tshw.tools.timetracker.util.DateUtil;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ProjectsTableBean extends AbstractTableModel {
    private ProjectList projectList;

    public final static int PROJECT_COL = 0;
    public final static int TIME_COL = 1;

    private final static String[] columnNames = {"Project", "Time today"};
    private final static boolean[] columnEditable = {true, false};

    public ProjectsTableBean(ProjectList projectList) {
        this.projectList = projectList;
    }

    @Override
    public int getRowCount() {
        return this.projectList.getProjects().size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int column) {
        return ProjectsTableBean.columnNames[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return ProjectsTableBean.columnEditable[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object value = null;
        switch (columnIndex) {
            case ProjectsTableBean.PROJECT_COL:
                value = this.projectList.getProjects().get(rowIndex).getName();
                break;
            case ProjectsTableBean.TIME_COL:
                value = getProjectTime(this.projectList.getProjects().get(rowIndex));
                break;
        }
        return value;
    }

    private String getProjectTime(Project project) {
        long totalMillis = project.getTotalMillisToday();
        return new DateUtil().formatMillis(totalMillis);
    }

    public Project getProject(int rowIndex) {
        return this.projectList.getProjects().get(rowIndex);
    }
}