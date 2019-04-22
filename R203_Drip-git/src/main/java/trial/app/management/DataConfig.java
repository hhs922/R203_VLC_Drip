package trial.app.management;

import javax.swing.*;
import java.util.HashMap;

public class DataConfig {
    public static final String layoutImageResourcePath = "/images/ITRI_B78_R203.png";

    public static final HashMap<Integer, DeviceDashboardModel.Anchor> activatorToAnchor = new HashMap<Integer, DeviceDashboardModel.Anchor>() {
        {

            put(1, new DeviceDashboardModel.Anchor(DeviceDashboardModel.AnchorType.Activator, 124, 86));
            put(2, new DeviceDashboardModel.Anchor(DeviceDashboardModel.AnchorType.Activator, 256, 86));
            put(3, new DeviceDashboardModel.Anchor(DeviceDashboardModel.AnchorType.Activator, 388, 86));
            put(4, new DeviceDashboardModel.Anchor(DeviceDashboardModel.AnchorType.Activator, 124, 221));
            put(5, new DeviceDashboardModel.Anchor(DeviceDashboardModel.AnchorType.Activator, 256, 221));
            put(6, new DeviceDashboardModel.Anchor(DeviceDashboardModel.AnchorType.Activator, 388, 221));
            put(7, new DeviceDashboardModel.Anchor(DeviceDashboardModel.AnchorType.Activator, 124, 356));
            put(8, new DeviceDashboardModel.Anchor(DeviceDashboardModel.AnchorType.Activator, 256, 356));
            put(9, new DeviceDashboardModel.Anchor(DeviceDashboardModel.AnchorType.Activator, 388, 356));

            put(10, new DeviceDashboardModel.Anchor(DeviceDashboardModel.AnchorType.ReaderAndActivator, 190, 86));
            put(11, new DeviceDashboardModel.Anchor(DeviceDashboardModel.AnchorType.ReaderAndActivator, 322, 86));
            put(12, new DeviceDashboardModel.Anchor(DeviceDashboardModel.AnchorType.ReaderAndActivator, 124, 154));
            put(13, new DeviceDashboardModel.Anchor(DeviceDashboardModel.AnchorType.ReaderAndActivator, 190, 154));
            put(14, new DeviceDashboardModel.Anchor(DeviceDashboardModel.AnchorType.ReaderAndActivator, 256, 154));
            put(15, new DeviceDashboardModel.Anchor(DeviceDashboardModel.AnchorType.ReaderAndActivator, 322, 154));
            put(16, new DeviceDashboardModel.Anchor(DeviceDashboardModel.AnchorType.ReaderAndActivator, 388, 154));
            put(17, new DeviceDashboardModel.Anchor(DeviceDashboardModel.AnchorType.ReaderAndActivator, 190, 221));
            put(18, new DeviceDashboardModel.Anchor(DeviceDashboardModel.AnchorType.ReaderAndActivator, 322, 221));
            put(19, new DeviceDashboardModel.Anchor(DeviceDashboardModel.AnchorType.ReaderAndActivator, 124, 288));
            put(20, new DeviceDashboardModel.Anchor(DeviceDashboardModel.AnchorType.ReaderAndActivator, 190, 288));
            put(21, new DeviceDashboardModel.Anchor(DeviceDashboardModel.AnchorType.ReaderAndActivator, 256, 288));
            put(22, new DeviceDashboardModel.Anchor(DeviceDashboardModel.AnchorType.ReaderAndActivator, 322, 288));
            put(23, new DeviceDashboardModel.Anchor(DeviceDashboardModel.AnchorType.ReaderAndActivator, 388, 288));
            put(24, new DeviceDashboardModel.Anchor(DeviceDashboardModel.AnchorType.ReaderAndActivator, 190, 356));
            put(25, new DeviceDashboardModel.Anchor(DeviceDashboardModel.AnchorType.ReaderAndActivator, 322, 356));

        }
    };

    public static final DeviceTableModel.DataColumn[] dataColumns = new DeviceTableModel.DataColumn[] {
            new DeviceTableModel.DataColumn<String>("Timestamp", String.class) {
                @Override
                String getValue(DataPacket dataPacket) {
                    return dataPacket.timestamp.toString();
                }
            },

            new DeviceTableModel.DataColumn<Integer>("Tag Id", Integer.class) {
                @Override
                Integer getValue(DataPacket dataPacket) { return dataPacket.tagId; }
            },

            new DeviceTableModel.DataColumn<Integer>("Activator Id", Integer.class) {
                @Override
                Integer getValue(DataPacket dataPacket) { return dataPacket.activatorId; }
            },

            new DeviceTableModel.DataColumn<String>("剩餘重量(毫升)", String.class) {
                @Override
                String getValue(DataPacket dataPacket) { return dataPacket.remainderWeight; }
            },

            new DeviceTableModel.DataColumn<String>("滴速(秒/drop)", String.class) {
                @Override
                String getValue(DataPacket dataPacket) { return dataPacket.dripSpeed; }
            },

//            new DeviceTableModel.DataColumn<Float>("滴速(秒/drop)", Float.class) {
//                @Override
//                Float getValue(DataPacket dataPacket) { return dataPacket.dripSpeed; }
//            },

            new DeviceTableModel.DataColumn<String>("剩餘時間(分鐘)", String.class) {
                @Override
                String getValue(DataPacket dataPacket) { return dataPacket.remainderTime; }
            },

            new DeviceTableModel.DataColumn<String>("電池電量(%)", String.class) {
                @Override
                String getValue(DataPacket dataPacket) { return dataPacket.batteryPower; }
            },

//            new DeviceTableModel.DataColumn<String>("Status", String.class) {
//                @Override
//                String getValue(DataPacket dataPacket) {
//                    return dataPacket.status;
//                }
//            },

    };

    public static final RowSorter.SortKey[] dataRowSorting = new RowSorter.SortKey[] {
            new RowSorter.SortKey(1, SortOrder.ASCENDING),
    };
}
