package controller;

import view.MainFrame;

public class ViewController {
    private MainFrame mainFrame;
    private NetworkController networkController;

    public ViewController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        addListeners();
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
                javax.swing.JOptionPane.showMessageDialog(mainFrame, "IP, Port, and Nickname cannot be empty.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int port = Integer.parseInt(portStr);
                if (networkController.connect(ip, port, nickname)) {
                    mainFrame.showView("lobby");
                }
            } catch (NumberFormatException ex) {
                javax.swing.JOptionPane.showMessageDialog(mainFrame, "Invalid port number.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        });

        mainFrame.getLobbyView().getCreateRoomButton().addActionListener(e -> {
            // TODO: Add logic to create a room
        });

        mainFrame.getLobbyView().getJoinRoomButton().addActionListener(e -> {
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
