package view;

import model.GameRoom.GameRooms;

import javax.swing.*;
import java.awt.*;

public class LobbyView extends JPanel {
    private final JTable roomTable;
    private final JButton joinRoomButton;

    public LobbyView() {
        setLayout(new BorderLayout());

        roomTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(roomTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        joinRoomButton = new JButton("Join Room");
        buttonPanel.add(joinRoomButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setGameRoomsModel(GameRooms model) {
        roomTable.setModel(model);
    }

    public JButton getJoinRoomButton() {
        return joinRoomButton;
    }

    public JTable getRoomTable() {
        return roomTable;
    }

    public int getSelectedRoomId() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow != -1) {
            // Assuming Room ID is in the first column (index 0)
            Object roomId = roomTable.getModel().getValueAt(selectedRow, 0);
            if (roomId instanceof Integer) {
                return (Integer) roomId;
            }
        }
        return -1; // No room selected or invalid ID
    }
}
