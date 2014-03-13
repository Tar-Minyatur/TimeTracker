package de.tshw.tools.timetracker.gui;

import com.toedter.calendar.JDateChooser;
import de.tshw.tools.timetracker.model.WorkingPeriod;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.DateFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WorkingPeriodExplorer {
    private JPanel periodExplorer;
    private JTable periodsTable;
    private JScrollPane periods;
    private JButton workingDayBack;
    private JTextField workingDay;
    private JButton workingDayForward;

    private Session dbSession;
    private SimpleDateFormat dateFormatter;

    public WorkingPeriodExplorer(Session dbSession) {
        this.dbSession = dbSession;
        this.periodsTable.setModel(new WorkingPeriodTableBean());

        this.periodsTable.setColumnSelectionAllowed(false);
        this.periodsTable.getColumnModel().getColumn(WorkingPeriodTableBean.PROJECT_COL).setMinWidth(130);
        this.periodsTable.getColumnModel().getColumn(WorkingPeriodTableBean.STARTTIME_COL).setWidth(50);
        this.periodsTable.getColumnModel().getColumn(WorkingPeriodTableBean.ENDTIME_COL).setWidth(50);
        this.periodsTable.getColumnModel().getColumn(WorkingPeriodTableBean.DURATION_COL).setWidth(50);
        this.periodsTable.getColumnModel().getColumn(WorkingPeriodTableBean.COMMENT_COL).setMinWidth(200);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
        this.periodsTable.getColumnModel().getColumn(WorkingPeriodTableBean.STARTTIME_COL).setCellRenderer(rightRenderer);
        this.periodsTable.getColumnModel().getColumn(WorkingPeriodTableBean.ENDTIME_COL).setCellRenderer(rightRenderer);
        this.periodsTable.getColumnModel().getColumn(WorkingPeriodTableBean.DURATION_COL).setCellRenderer(rightRenderer);

        workingDayBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    changeWorkingDay(false);
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
        });

        workingDayForward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    changeWorkingDay(true);
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
        });

        this.dateFormatter = new SimpleDateFormat("dd.MM.yyyy");

        this.workingDay.setText(this.dateFormatter.format(Calendar.getInstance().getTime()));

        this.updateTableData();
    }

    private void changeWorkingDay(boolean forward) throws ParseException {
        Date date = dateFormatter.parse(workingDay.getText());
        Calendar time = Calendar.getInstance();
        time.setTime(date);
        time.add(Calendar.DATE, forward ? 1 : -1);
        workingDay.setText(dateFormatter.format(time.getTime()));
        updateTableData();
    }

    private void updateTableData() {
        try {
            WorkingPeriodTableBean model = (WorkingPeriodTableBean) this.periodsTable.getModel();
            model.clear();
            Calendar earliestTime = Calendar.getInstance();
            earliestTime.setTime(this.dateFormatter.parse(this.workingDay.getText()));
            earliestTime.set(Calendar.HOUR_OF_DAY, 0);
            earliestTime.set(Calendar.MINUTE, 0);
            earliestTime.set(Calendar.SECOND, 0);
            Calendar latestTime = Calendar.getInstance();
            latestTime.setTime(this.dateFormatter.parse(this.workingDay.getText()));
            latestTime.set(Calendar.HOUR_OF_DAY, 23);
            latestTime.set(Calendar.MINUTE, 59);
            latestTime.set(Calendar.SECOND, 59);
            Query query = this.dbSession.createQuery("from WorkingPeriod where startTime between :earliest and :latest order by startTime asc");
            query.setParameter("earliest", earliestTime);
            query.setParameter("latest", latestTime);
            List<WorkingPeriod> workingPeriods = query.list();
            for (WorkingPeriod p: workingPeriods) {
                model.addWorkingPeriod(p);
            }
        } catch (ParseException e) {
            // TODO Add error handling for wrong date format
            e.printStackTrace();
        }
        this.periods.updateUI();
        this.periodsTable.updateUI();
        this.periodExplorer.updateUI();
    }

    public JPanel getPeriodExplorer() {
        return periodExplorer;
    }
}
