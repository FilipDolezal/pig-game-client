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

    @Override
    public void startGame(String oppNick, boolean turn) {
        mainFrame.getGameView().gameStart(oppNick, turn);
    }

    @Override
    public void gameState(int myScore, int oppScore, int turnScore, int roll, boolean turn) {
        mainFrame.getGameView().gameState(myScore, oppScore, turnScore, roll, turn);
    }

    @Override
    public void showGameWon(String message) {
        JOptionPane.showMessageDialog(mainFrame, message, "You Won!", JOptionPane.INFORMATION_MESSAGE);
        returnToLobby();
    }

    @Override
    public void showGameLost(String message) {
        JOptionPane.showMessageDialog(mainFrame, message, "You Lost", JOptionPane.WARNING_MESSAGE);
        returnToLobby();
    }

    private void returnToLobby() {
        if (player != null) {
            player.setState(PlayerState.LOBBY);
            player.setCurrentRoomId(-1);
        }
        mainFrame.getGameView().resetView();
        mainFrame.showView("lobby");
        if (networkController != null) {
            networkController.sendListRooms();
        }
    }

    public void showErrorMessage(String title, String message) {
        JOptionPane.showMessageDialog(mainFrame, message, title, JOptionPane.ERROR_MESSAGE);
    }

    private void addListeners() {
        mainFrame.getLoginView().getLoginButton().addActionListener(e -> handleLoginAction());
        mainFrame.getLobbyView().getJoinRoomButton().addActionListener(e -> handleJoinRoomAction());

        mainFrame.getGameView().getRollButton().addActionListener(e -> {
            if (networkController != null) {
                networkController.sendRoll();
            }
        });

        mainFrame.getGameView().getHoldButton().addActionListener(e -> {
            if (networkController != null) {
                networkController.sendHold();
            }
        });
        mainFrame.getGameView().getQuitButton().addActionListener(e -> handleQuitGameAction());
    }

    private void handleQuitGameAction() {
        int confirm = JOptionPane.showConfirmDialog(
            mainFrame,
            "Are you sure you want to quit? Your opponent will win, and this action cannot be undone.",
            "Confirm Quit",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            networkController.sendQuitGame();
            mainFrame.showView("lobby");
            player.setState(PlayerState.LOBBY);
            player.setCurrentRoomId(-1);
        }
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
