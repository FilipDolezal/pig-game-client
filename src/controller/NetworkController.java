package controller;

import model.room.GameRoomStatus;
import net.Client;
import net.msg.MsgListRooms;
import net.msg.MsgLogin;
import net.msg.MsgJoinRoom;
import net.msg.MsgLeaveRoom;
import net.msg.MsgQuit;
import net.msg.MsgRoll;
import net.msg.MsgHold;
import net.msg.MsgExit;
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

	@Override
	public void sendJoinRoom(int roomId) {
		client.sendMessage(new MsgJoinRoom(roomId));
	}

	@Override
	public void sendQuitGame() {
		client.sendMessage(new MsgQuit());
	}

	@Override
	public void sendLeaveRoom() {
		client.sendMessage(new MsgLeaveRoom());
	}

	@Override
	public void sendRoll() {
		client.sendMessage(new MsgRoll());
	}

	@Override
	public void sendHold() {
		client.sendMessage(new MsgHold());
	}

	@Override
	public void sendExit() {
		client.sendMessage(new MsgExit());
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
		System.out.println(message);
		switch (message.cmd) {
			case WELCOME:
				maxPlayers = Integer.parseInt(message.args.get(Protocol.K_PLAYERS));
				maxRooms = Integer.parseInt(message.args.get(Protocol.K_ROOMS));
				view.initializeGameRooms(maxRooms);
				break;

			case OK:
				// Handle OK message
				String cmd = message.args.get(Protocol.K_CMD);
				String msg = message.args.get(Protocol.K_MSG);
				System.out.println("OK:" + cmd + " " + msg);
				break;

			case ERROR:
				// Handle ERROR message
				String ecmd = message.args.get(Protocol.K_CMD);
				String emsg = message.args.get(Protocol.K_MSG);
				System.out.println("ERR:" + ecmd + " " + emsg);

				view.showErrorMessage("Error", "Error: " + message.args.get(Protocol.K_MSG));
				break;

			case ROOM_INFO:
				view.syncGameRoom(
					Integer.parseInt(message.args.get(Protocol.K_ROOM)),
					Integer.parseInt(message.args.get(Protocol.K_COUNT)),
					GameRoomStatus.valueOf(message.args.get(Protocol.K_STATE))
				);
				break;

			case JOIN_OK:
				int room = Integer.parseInt(message.args.get(Protocol.K_ROOM));
				view.joinGameRoom(room);
				break;

			case GAME_START:
				view.startGame(
					message.args.get(Protocol.K_OPP_NICK),
					Integer.parseInt(message.args.get(Protocol.K_YOUR_TURN)) == 1
				);
				break;

			case GAME_STATE:
				System.out.println("REC: " + message);
				view.gameState(
					Integer.parseInt(message.args.get(Protocol.K_MY_SCORE)),
					Integer.parseInt(message.args.get(Protocol.K_OPP_SCORE)),
					Integer.parseInt(message.args.get(Protocol.K_TURN_SCORE)),
					Integer.parseInt(message.args.get(Protocol.K_ROLL)),
					Integer.parseInt(message.args.get(Protocol.K_YOUR_TURN)) == 1
				);
				break;

			case GAME_WIN:
				view.showGameWon(message.args.get(Protocol.K_MSG));
				break;

			case GAME_LOSE:
				view.showGameLost(message.args.get(Protocol.K_MSG));
				break;
		}
	}
}
