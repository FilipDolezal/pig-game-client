package view;

import model.GameRoom.GameRooms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LobbyView extends JPanel {
    private JTable roomTable;
    private JButton joinRoomButton;

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
}
