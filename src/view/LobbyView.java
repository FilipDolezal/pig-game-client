package view;

import javax.swing.*;
import java.awt.*;

public class LobbyView extends JPanel {
    private JList<String> roomList;
    private JButton createRoomButton;
    private JButton joinRoomButton;

    public LobbyView() {
        setLayout(new BorderLayout());

        DefaultListModel<String> model = new DefaultListModel<>();
        roomList = new JList<>(model);
        JScrollPane scrollPane = new JScrollPane(roomList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        createRoomButton = new JButton("Create Room");
        joinRoomButton = new JButton("Join Room");
        buttonPanel.add(createRoomButton);
        buttonPanel.add(joinRoomButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public JList<String> getRoomList() {
        return roomList;
    }

    public JButton getCreateRoomButton() {
        return createRoomButton;
    }

    public JButton getJoinRoomButton() {
        return joinRoomButton;
    }
}
