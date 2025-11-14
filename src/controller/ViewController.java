package controller;

import model.GameRoom.GameRooms;
import model.GameRoom.GameRoomStatus;
import view.MainFrame;

import javax.swing.*;

public class ViewController implements NetworkToViewInterface {
    private final MainFrame mainFrame;
    private ViewToNetworkInterface networkController;
    private final Timer lobbyUpdateTimer;
    private GameRooms gameRooms;

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

    public void setNetworkController(ViewToNetworkInterface networkController) {
        this.networkController = networkController;
    }

    public void initializeGameRooms(int maxRooms) {
        this.gameRooms = new GameRooms(maxRooms);
        mainFrame.getLobbyView().setGameRoomsModel(gameRooms);
    }

    public void syncGameRoom(int id, int count, GameRoomStatus status) {
        if (gameRooms != null) {
            gameRooms.sync(id, count, status);
        }
    }

    public void showErrorMessage(String title, String message) {
        JOptionPane.showMessageDialog(mainFrame, message, title, JOptionPane.ERROR_MESSAGE);
    }

    private void addListeners() {
        mainFrame.getLoginView().getLoginButton().addActionListener(e -> handleLoginAction());

        mainFrame.getLobbyView().getJoinRoomButton().addActionListener(e -> handleJoinRoomAction());

        mainFrame.getGameView().getRollButton().addActionListener(e -> {
            // TODO: Add logic to roll the dice
        });

        mainFrame.getGameView().getHoldButton().addActionListener(e -> {
            // TODO: Add logic to hold
        });
    }

    private void handleJoinRoomAction() {
        int selectedRoomId = mainFrame.getLobbyView().getSelectedRoomId();
        if (selectedRoomId == -1) {
            showErrorMessage("Join Room Error", "Please select a room to join.");
            return;
        }
        networkController.sendJoinRoom(selectedRoomId);
        // Further logic to handle server response (e.g., switch to game view) will be handled by NetworkController
        // and delegated back to ViewController via NetworkToViewInterface methods.
    }

    private void handleLoginAction() {
        String ip = mainFrame.getLoginView().getIp();
        String portStr = mainFrame.getLoginView().getPort();
        String nickname = mainFrame.getLoginView().getNickname();

        if (ip.isEmpty() || portStr.isEmpty() || nickname.isEmpty()) {
            showErrorMessage("Error", "IP, Port, and Nickname cannot be empty.");
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
            showErrorMessage("Error", "Invalid port number.");
        }
    }
}
