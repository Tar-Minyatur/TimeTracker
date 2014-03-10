package de.tshw.tools.timetracker.gui;

import de.tshw.tools.timetracker.controller.GuiController;
import de.tshw.tools.timetracker.util.DateUtil;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimeTrackerGUI implements ListSelectionListener {
    private JButton addProjectButton;
    private JButton toggleTimerButton;
    private JLabel totalTimeLabel;
    private JTable projectsTable;
    private JPanel timeTrackerPanel;
    private JScrollPane projectTableScrollPane;
    private JToolBar toolbar;
    private JButton editCommentButton;

    private ProjectsTableBean projectsTableData;
    private GuiController controller;

    private ImageIcon playButton;
    private ImageIcon pauseButton;

    public TimeTrackerGUI() {
        this.playButton = new ImageIcon(ClassLoader.getSystemResource("icons/clock_play.png"), "Start timer");
        this.pauseButton = new ImageIcon(ClassLoader.getSystemResource("icons/clock_pause.png"), "Pause timer");

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
        editCommentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: Implement comment editing
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
            this.toggleTimerButton.setIcon(this.pauseButton);
            this.toggleTimerButton.setToolTipText("Pause timer");
        } else {
            this.toggleTimerButton.setIcon(this.playButton);
            this.toggleTimerButton.setToolTipText("Start timer");
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
