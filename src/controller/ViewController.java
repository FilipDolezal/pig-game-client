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
        mainFrame.lobbyView.setGameRoomsModel(gameRooms);
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
            mainFrame.gameView.showLeaveButton(true);
            mainFrame.gameView.setWaitingForOpponent(true);
            mainFrame.showView("game");
        }
    }

    @Override
    public void quitGameRoom() {
        if (player != null) {
            player.setState(PlayerState.LOBBY);
            player.setCurrentRoomId(-1);
        }
        returnToLobby();
    }

    @Override
    public void startGame(String oppNick, boolean turn) {
        mainFrame.gameView.showLeaveButton(false);
        mainFrame.gameView.gameStart(oppNick, turn);
    }

    @Override
    public void gameState(int myScore, int oppScore, int turnScore, int roll, boolean turn) {
        mainFrame.gameView.gameState(myScore, oppScore, turnScore, roll, turn);
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

    @Override
    public void login(String username) {
        player = new Player(username);
        mainFrame.showView("lobby");
    }

    @Override
    public void returnToLobby() {
        if (player != null) {
            player.setState(PlayerState.LOBBY);
            player.setCurrentRoomId(-1);
        }
        mainFrame.gameView.resetView();
        mainFrame.showView("lobby");
        if (networkController != null) {
            networkController.sendListRooms();
        }
    }

    @Override
    public void resumeGame() {
        mainFrame.gameView.resumeGame();
        if (networkController != null) {
            networkController.sendGameStateRequest();
        }
    }

    public void showErrorMessage(String title, String message) {
        JOptionPane.showMessageDialog(mainFrame, message, title, JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void showGamePausedDialog() {
        int dialogResult = JOptionPane.showConfirmDialog(mainFrame,
                "Your opponent has paused the game. Do you want to resume?",
                "Game Paused", JOptionPane.YES_NO_OPTION);

        if (dialogResult == JOptionPane.YES_OPTION) {
            if (networkController != null) {
                networkController.sendResume();
                mainFrame.showView("game");
            }
        }
    }

    @Override
    public void showOpponentDisconnected() {
        mainFrame.gameView.showOpponentDisconnected(true);
    }

    @Override
    public void hideOpponentDisconnected() {
        mainFrame.gameView.showOpponentDisconnected(false);
    }

    @Override
    public void showNoResponseWarning(String command) {
        mainFrame.showWarning("No response from server for: " + command);
    }

    @Override
    public void hideNoResponseWarning() {
        mainFrame.hideWarning();
    }

    private void addListeners() {
        mainFrame.loginView.getLoginButton().addActionListener(e -> handleLoginAction());
        mainFrame.lobbyView.getJoinRoomButton().addActionListener(e -> handleJoinRoomAction());
        mainFrame.lobbyView.getExitButton().addActionListener(e -> handleExitAction());
        mainFrame.gameView.getRollButton().addActionListener(e -> handleRollAction());
        mainFrame.gameView.getHoldButton().addActionListener(e -> handleHoldAction());
        mainFrame.gameView.getQuitButton().addActionListener(e -> handleQuitGameAction());
        mainFrame.gameView.getLeaveButton().addActionListener(e -> handleLeaveRoomAction());
    }

    private void handleHoldAction() {
        if (networkController != null) {
            networkController.sendHold();
        }
    }

    private void handleRollAction() {
        if (networkController != null) {
            networkController.sendRoll();
        }
    }

    private void handleLeaveRoomAction() {
        if (networkController != null) {
            networkController.sendLeaveRoom();
        }
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
        }
    }

    private void handleJoinRoomAction() {
        int selectedRoomId = mainFrame.lobbyView.getSelectedRoomId();
        if (selectedRoomId == -1) {
            showErrorMessage("Join Room Error", "Please select a room to join.");
            return;
        }
        networkController.sendJoinRoom(selectedRoomId);
    }

    private void handleLoginAction() {
        String ip = mainFrame.loginView.getIp();
        String portStr = mainFrame.loginView.getPort();
        String nickname = mainFrame.loginView.getNickname();

        if (ip.isEmpty() || portStr.isEmpty() || nickname.isEmpty()) {
            showErrorMessage("Error", "IP, Port, and Nickname cannot be empty.");
            return;
        }

        try {
			networkController.connect(ip, Integer.parseInt(portStr), nickname);
        } catch (NumberFormatException ex) {
            showErrorMessage("Error", "Invalid port number.");
        }
    }

    public void handleExitAction() {
        if (networkController != null) {
            networkController.sendExit();
        }
        this.player = null;
        this.gameRooms = null;
        mainFrame.showView("login");
    }

    @Override
    public void disconnect() {
        this.player = null;
        this.gameRooms = null;
        mainFrame.showView("login");
        JOptionPane.showMessageDialog(mainFrame, "You have been disconnected from the server.", "Disconnected", JOptionPane.WARNING_MESSAGE);
    }
}
