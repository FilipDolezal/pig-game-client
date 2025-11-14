package controller;

import model.GameRoom.GameRoomStatus;
import net.Client;
import net.ClientMessage.MsgListRooms;
import net.ClientMessage.MsgLogin;
import net.ServerMessage;
import net.Protocol;

import javax.swing.*;
import java.io.IOException;

public class NetworkController implements ViewToNetworkInterface {
    private final Client client;
    private final NetworkToViewInterface view;
    private int maxPlayers;
    private int maxRooms;

    public NetworkController(ViewController viewController) {
        this.client = new Client();
        this.view = viewController;
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
                    view.showErrorMessage("Connection Error", "Connection failed: " + e.getMessage())
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
                view.initializeGameRooms(maxRooms);

                break;
            case OK:
                // Handle OK message
                break;
            case ERROR:
                // Handle ERROR message
                view.showErrorMessage("Error", "Error: " + message.args.get(Protocol.K_MSG));
                break;
            case ROOM_INFO:
                view.syncGameRoom(
                    Integer.parseInt(message.args.get(Protocol.K_ROOM)),
                    Integer.parseInt(message.args.get(Protocol.K_COUNT)),
                    GameRoomStatus.valueOf(message.args.get(Protocol.K_STATE))
                );
                break;
        }
    }
}
