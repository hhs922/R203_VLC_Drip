package trial.app.management;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;

import static trial.app.management.DataConfig.dataRowSorting;

public class MainForm {

    public MainForm() {
        aidCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                deviceDashboard.setAidVisible(e.getStateChange() == ItemEvent.SELECTED);
            }
        });

        deviceDataTable.setModel(new DeviceTableModel());
        deviceDataTable.getRowSorter().setSortKeys(Arrays.asList(dataRowSorting));

        deviceDashboard.setModel(new DeviceDashboardModel());
        deviceDashboard.setAnimationEnabled(500);
        deviceDashboard.setDesignMode(false);

//        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
//        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
//        deviceDashboard.setDefaultRenderer(Object.class, centerRenderer);

        lightingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LightingFrame lightingFrame = new LightingFrame();
                lightingFrame.setVisible(true);
            }
        });
    }

    public DeviceDashboardModel getDeviceDashboardModel() {
        return deviceDashboard.getModel();
    }

    public DeviceTableModel getDeviceTableModel() {
        return (DeviceTableModel) deviceDataTable.getModel();
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private JPanel rootPanel;
    private DeviceDashboard deviceDashboard;
    private JTable deviceDataTable;
    private JCheckBox aidCheckBox;
    private JButton lightingButton;


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
