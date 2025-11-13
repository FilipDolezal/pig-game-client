package controller;

import net.Client;
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

    public NetworkController(MainFrame mainFrame, ViewController viewController) {
        this.client = new Client();
        this.mainFrame = mainFrame;
        this.viewController = viewController;
    }

    public void connect(String ip, int port, String nickname) {
        try {
            client.connect(ip, port);
            client.sendMessage(new MsgLogin(nickname));
            // Start a new thread to listen for messages from the server
            new Thread(this::listenForMessages).start();
        } catch (IOException e) {
            // TODO: Handle connection error
            e.printStackTrace();
        }
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
            case Protocol.ServerCommand.OK:
                // Handle OK message
                break;
            case Protocol.ServerCommand.ERROR:
                // Handle ERROR message
                JOptionPane.showMessageDialog(mainFrame, "Error: " + String.join(" ", message.args()), "Error", JOptionPane.ERROR_MESSAGE);
                break;
            case Protocol.ServerCommand.ROOM_LIST:
                // Handle ROOM_LIST message
                DefaultListModel<String> model = (DefaultListModel<String>) mainFrame.getLobbyView().getRoomList().getModel();
                model.clear();
                for (String room : message.args) {
                    model.addElement(room);
                }
                break;
            // TODO: Add cases for other server commands
        }
    }
}
