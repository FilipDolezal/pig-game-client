package controller;

import model.room.GameRooms;
import model.room.GameRoomStatus;
import model.player.Player;
import model.player.PlayerState;
import view.MainFrame;

import javax.swing.*;

public class ViewController implements NetworkToViewInterface {
    private final MainFrame mainFrame;
    private ViewToNetworkInterface networkController;
    private GameRooms gameRooms;
    private Player player;

    public ViewController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        addListeners();
    }

    public void setNetworkController(ViewToNetworkInterface networkController) {
        this.networkController = networkController;
    }

    @Override
    public void initializeGameRooms(int maxRooms) {
        this.gameRooms = new GameRooms(maxRooms);
        mainFrame.getLobbyView().setGameRoomsModel(gameRooms);
    }

    @Override
    public void syncGameRoom(int id, int count, GameRoomStatus status) {
        if (gameRooms != null) {
            gameRooms.sync(id, count, status);
        }
    }

    @Override
    public void joinGameRoom(int id) {
        if (player != null) {
            player.setCurrentRoomId(id);
            player.setState(PlayerState.IN_GAME);
            mainFrame.showView("game");
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
                this.player = new Player(nickname); // Initialize player here
                mainFrame.showView("lobby");
            }
        } catch (NumberFormatException ex) {
            showErrorMessage("Error", "Invalid port number.");
        }
    }
}
