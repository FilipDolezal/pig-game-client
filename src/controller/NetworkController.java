package controller;

import model.GameRoom.GameRoom;
import model.GameRoom.GameRoomStatus;
import model.GameRoom.GameRooms;
import net.Client;
import net.ClientMessage.MsgListRooms;
import net.ClientMessage.MsgLogin;
import net.ServerMessage;
import net.Protocol;
import view.MainFrame;

import javax.swing.*;
import java.io.IOException;

public class NetworkController {
    private Client client;
    private MainFrame mainFrame;
    private ViewController viewController;
    private GameRooms gameRooms;
    private int maxPlayers;
    private int maxRooms;

    public NetworkController(MainFrame mainFrame, ViewController viewController) {
        this.client = new Client();
        this.mainFrame = mainFrame;
        this.viewController = viewController;
    }

    public boolean connect(String ip, int port, String nickname) {
        try {
            client.connect(ip, port);
            client.sendMessage(new MsgLogin(nickname));
            // Start a new thread to listen for messages from the server
            new Thread(this::listenForMessages).start();
            return true;
        } catch (IOException e) {
            // TODO: Handle connection error
            e.printStackTrace();
            SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(mainFrame, "Connection failed: " + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE)
            );
            return false;
        }
    }

    public void sendListRooms() {
        client.sendMessage(new MsgListRooms());
    }

    private void listenForMessages() {
        try {
            String fromServer;
            while ((fromServer = client.readMessage()) != null) {

                ServerMessage parsedMessage = ServerMessage.parse(fromServer);
                if (parsedMessage != null) {
                    SwingUtilities.invokeLater(() -> handleServerMessage(parsedMessage));
                }

            }
        } catch (IOException e) {
            // TODO: Handle disconnection
            e.printStackTrace();
        }
    }

    private void handleServerMessage(ServerMessage message) {
        switch (message.cmd) {
            case WELCOME:
                maxPlayers = Integer.parseInt(message.args.get(Protocol.K_PLAYERS));
                maxRooms = Integer.parseInt(message.args.get(Protocol.K_ROOMS));

                gameRooms = new GameRooms(maxRooms);
                mainFrame.getLobbyView().setGameRoomsModel(gameRooms);

                break;
            case OK:
                // Handle OK message
                break;
            case ERROR:
                // Handle ERROR message
                JOptionPane.showMessageDialog(mainFrame, "Error: " + message.args.get(Protocol.K_MSG), "Error", JOptionPane.ERROR_MESSAGE);
                break;
            case ROOM_INFO:
                gameRooms.sync(
                    Integer.parseInt(message.args.get(Protocol.K_ROOM)),
                    Integer.parseInt(message.args.get(Protocol.K_COUNT)),
                    GameRoomStatus.valueOf(message.args.get(Protocol.K_STATE))
                );
                break;
        }
    }
}
