package model.room;

import javax.swing.table.AbstractTableModel;
import java.util.HashMap;

public class GameRooms extends AbstractTableModel {
    private final HashMap<Integer, GameRoom> rooms;
    private final String[] columnNames = {"Room ID", "Players", "Status"};

    public GameRooms(int maxRooms) {
        rooms = new HashMap<>(maxRooms);

        for(int i = 0; i < maxRooms; i++) {
            rooms.put(i, new GameRoom(i, 0, GameRoomStatus.WAITING));
        }
    }

    public void sync(int id, int count, GameRoomStatus status) {
        rooms.get(id).update(count, status);
    }

    @Override
    public int getRowCount() {
        return rooms.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        GameRoom room = rooms.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> room.getId();
            case 1 -> room.getCount();
            case 2 -> room.getStatus();
            default -> null;
        };
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> Integer.class;
            case 1 -> String.class;
            case 2 -> GameRoomStatus.class;
            default -> Object.class;
        };
    }
}
