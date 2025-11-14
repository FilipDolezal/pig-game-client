package controller;

import view.MainFrame;

import javax.swing.*;

public class ViewController {
    private final MainFrame mainFrame;
    private NetworkController networkController;
    private final Timer lobbyUpdateTimer;

    public ViewController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        addListeners();
        lobbyUpdateTimer = new Timer(5000, e -> {
            if (networkController != null) {
                networkController.sendListRooms();
            }
        });
        lobbyUpdateTimer.setRepeats(true);
    }

    public void setNetworkController(NetworkController networkController) {
        this.networkController = networkController;
    }

    private void addListeners() {
        mainFrame.getLoginView().getLoginButton().addActionListener(e -> {
            String ip = mainFrame.getLoginView().getIp();
            String portStr = mainFrame.getLoginView().getPort();
            String nickname = mainFrame.getLoginView().getNickname();

            if (ip.isEmpty() || portStr.isEmpty() || nickname.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, "IP, Port, and Nickname cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int port = Integer.parseInt(portStr);
                if (networkController.connect(ip, port, nickname)) {
                    mainFrame.showView("lobby");
                    lobbyUpdateTimer.start();
                    networkController.sendListRooms();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainFrame, "Invalid port number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        mainFrame.getLobbyView().getJoinRoomButton().addActionListener(e -> {
            lobbyUpdateTimer.stop();
            // TODO: Add logic to join a room
        });

        mainFrame.getGameView().getRollButton().addActionListener(e -> {
            // TODO: Add logic to roll the dice
        });

        mainFrame.getGameView().getHoldButton().addActionListener(e -> {
            // TODO: Add logic to hold
        });
    }
}
