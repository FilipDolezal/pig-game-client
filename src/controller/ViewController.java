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
            String nickname = mainFrame.getLoginView().getNickname();
            if (!nickname.isEmpty()) {
                networkController.connect("172.31.232.122", 12345, nickname);
                mainFrame.showView("lobby");
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
