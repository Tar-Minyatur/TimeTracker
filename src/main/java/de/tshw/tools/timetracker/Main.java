package de.tshw.tools.timetracker;

import de.tshw.tools.timetracker.controller.GuiController;
import de.tshw.tools.timetracker.gui.TimeTrackerGUI;
import de.tshw.tools.timetracker.model.Project;
import de.tshw.tools.timetracker.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        final HibernateUtil hibernateUtil = new HibernateUtil();
        final SessionFactory sessionFactory = hibernateUtil.getSessionFactory();
        final Session session = sessionFactory.openSession();

        JFrame jFrame = new JFrame("NullTimeTracker");
        final TimeTrackerGUI timeTrackerGUI = new TimeTrackerGUI();
        jFrame.getContentPane().add(timeTrackerGUI.getTimeTrackerPanel());
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                timeTrackerGUI.windowClosed();
                session.close();
                hibernateUtil.closeSession();
                e.getWindow().dispose();
            }
        });
        jFrame.pack();
        jFrame.setAlwaysOnTop(true);
        jFrame.setVisible(true);

        GuiController controller = new GuiController(timeTrackerGUI, session);
    }

}
