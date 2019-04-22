package trial.app.management;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

import static trial.app.management.DataConfig.dataColumns;

public class DeviceTableModel extends AbstractTableModel {

    ArrayList<DataPacket> dataPacketList = new ArrayList<>();

    public void receive(DataPacket dataPacket) {
        int idx = findTheSameTagId(dataPacket);
        if (idx < 0) { // new one
            if (dataPacketList.add(dataPacket)) {
                //int latest = dataPacketList.size();
                //fireTableRowsInserted(latest, latest);
                fireTableDataChanged();
            }
        } else { // update
            dataPacketList.set(idx, dataPacket);
            fireTableRowsUpdated(idx, idx);
        }
    }

    int findTheSameTagId(DataPacket newOne) {
        for (int i = 0; i < dataPacketList.size(); ++i) {
            if (dataPacketList.get(i).tagId == newOne.tagId) return i;
        }
        return -1;
    }

    @Override
    public int getRowCount() {
        return dataPacketList.size();
    }

    abstract static class DataColumn<T> {
        public DataColumn(String columnName, Class<T> dataClass) {
            this.columnName = columnName;
            this.dataClass = dataClass;
        }

        public final String columnName;
        public final Class<T> dataClass;

        abstract T getValue(DataPacket dataPacket);
    }

    @Override
    public int getColumnCount() {
        return dataColumns.length;
    }

    @Override
    public String getColumnName(int column) {
        return dataColumns[column].columnName;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return dataColumns[columnIndex].dataClass;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= dataPacketList.size()) return null;
        if (columnIndex < 0 || columnIndex >= dataColumns.length) return null;
        return dataColumns[columnIndex].getValue(dataPacketList.get(rowIndex));
    }
}
