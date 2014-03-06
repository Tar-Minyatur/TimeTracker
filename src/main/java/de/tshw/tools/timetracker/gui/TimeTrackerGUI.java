package de.tshw.tools.timetracker.gui;

import de.tshw.tools.timetracker.controller.GuiController;
import de.tshw.tools.timetracker.util.DateUtil;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ContainerAdapter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeTrackerGUI implements ListSelectionListener {
    private JButton addProjectButton;
    private JButton toggleTimerButton;
    private JLabel totalTimeLabel;
    private JTable projectsTable;
    private JPanel timeTrackerPanel;
    private JScrollPane projectTableScrollPane;

    private ProjectsTableBean projectsTableData;
    private GuiController controller;

    public TimeTrackerGUI() {
        toggleTimerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controller.isTimerRunning()) {
                    controller.stopTimer();
                } else {
                    controller.startTimer();
                }
                updateUI();
            }
        });
        addProjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String projectName = JOptionPane.showInputDialog("Project Name:");
                if ((projectName != null) && (projectName.length() > 0)) {
                    controller.addProject(projectName);
                }
            }
        });
    }

    public void setProjectsTableData(ProjectsTableBean projectsTableData) {
        this.projectsTable.getSelectionModel().removeListSelectionListener(this);
        this.projectsTableData = projectsTableData;
        this.projectsTable.setModel(this.projectsTableData);
        this.projectsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.projectsTable.getColumnModel().getColumn(ProjectsTableBean.TIME_COL).setPreferredWidth(20);
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
        this.projectsTable.getColumnModel().getColumn(ProjectsTableBean.TIME_COL).setCellRenderer(rightRenderer);
        if (projectsTableData.getRowCount() > 0) {
            this.projectsTable.setRowSelectionInterval(0,0);
        } else {
            this.toggleTimerButton.setEnabled(false);
        }
        this.projectsTable.getSelectionModel().addListSelectionListener(this);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() && (e.getSource() == projectsTable.getSelectionModel())) {
            ListSelectionModel model = (ListSelectionModel) e.getSource();
            int index = model.getMinSelectionIndex();
            controller.projectChanged(projectsTableData.getProject(index));
        }
    }

    public void setController(GuiController controller) {
        this.controller = controller;
    }

    public JPanel getTimeTrackerPanel() {
        return timeTrackerPanel;
    }

    public void updateUI() {
        this.projectsTable.updateUI();
        boolean timerCanBeStarted = ((this.projectsTableData.getRowCount() > 0) || this.controller.isTimerRunning());
        this.toggleTimerButton.setEnabled(timerCanBeStarted);
        this.updateTotalTime();
        if (this.controller.isTimerRunning()) {
            this.toggleTimerButton.setText("Pause Timer");
        } else {
            this.toggleTimerButton.setText("Start Timer");
        }
    }

    private void updateTotalTime() {
        String time = new DateUtil().formatMillis(this.controller.getTotalMillisPassed());
        this.totalTimeLabel.setText(time);
    }

    public void windowClosed() {
        this.controller.windowClosed();
    }
}
