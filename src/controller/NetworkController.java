package controller;

import net.Client;
import view.MainFrame;

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
            client.startConnection(ip, port);
            client.sendMessage("JOIN " + nickname);
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
            while ((fromServer = client.receiveMessage()) != null) {
                // TODO: Process the message from the server and update the view
                System.out.println("Server: " + fromServer);
            }
        } catch (IOException e) {
            // TODO: Handle disconnection
            e.printStackTrace();
        }
    }
}
