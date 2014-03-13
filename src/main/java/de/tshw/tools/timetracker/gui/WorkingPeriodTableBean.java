package de.tshw.tools.timetracker.gui;

import de.tshw.tools.timetracker.model.WorkingPeriod;
import de.tshw.tools.timetracker.util.DateUtil;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class WorkingPeriodTableBean extends AbstractTableModel {

    public final static int PROJECT_COL = 0;
    public final static int STARTTIME_COL = 1;
    public final static int ENDTIME_COL = 2;
    public final static int DURATION_COL = 3;
    public final static int COMMENT_COL = 4;

    private final static String[] columnNames = {"Project", "Start time", "End time", "Duration", "Comment"};
    private final static boolean[] columnEditable = {false, false, false, false, false};

    private List<WorkingPeriod> workingPeriods;

    public WorkingPeriodTableBean() {
        this.workingPeriods = new ArrayList<WorkingPeriod>();
    }

    @Override
    public int getRowCount() {
        return workingPeriods.size();
    }

    public void clear() {
        this.workingPeriods.clear();
    }

    public void addWorkingPeriod(WorkingPeriod workingPeriod) {
        this.workingPeriods.add(workingPeriod);
    }

    @Override
    public int getColumnCount() {
        return WorkingPeriodTableBean.columnNames.length;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return WorkingPeriodTableBean.columnEditable[columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return WorkingPeriodTableBean.columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        WorkingPeriod period = this.workingPeriods.get(rowIndex);
        DateUtil dateUtil = new DateUtil();
        switch (columnIndex) {
            case WorkingPeriodTableBean.PROJECT_COL:
                return period.getProject().getName();
            case WorkingPeriodTableBean.STARTTIME_COL:
                return dateUtil.formatMillis(period.getStartTime().getTimeInMillis());
            case WorkingPeriodTableBean.ENDTIME_COL:
                return dateUtil.formatMillis(period.getEndTime().getTimeInMillis());
            case WorkingPeriodTableBean.DURATION_COL:
                return dateUtil.formatMillis(period.getDurationInMillis());
            case WorkingPeriodTableBean.COMMENT_COL:
                return period.getComment();
        }
        return null;
    }
}