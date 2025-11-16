package controller;

import model.room.GameRoomStatus;
import net.Client;
import net.Protocol.ClientCommand;
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

	public void connect(String ip, int port, String nickname) {
		try {
			client.connect(ip, port);
			client.sendMessage(new MsgLogin(nickname));
			// Start a new thread to listen for messages from the server
			new Thread(this::listenForMessages).start();
		} catch (IOException e) {
			// TODO: Handle connection error
			e.printStackTrace();
			SwingUtilities.invokeLater(() ->
				view.showErrorMessage("Connection Error", "Connection failed: " + e.getMessage())
			);
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
		ClientCommand clientArgCmd = ClientCommand.fromString(message.args.getOrDefault(Protocol.K_CMD, null));
		String clientArgMsg = message.args.getOrDefault(Protocol.K_MSG, null);

		switch (message.cmd) {
			case WELCOME:
				maxPlayers = Integer.parseInt(message.args.get(Protocol.K_PLAYERS));
				maxRooms = Integer.parseInt(message.args.get(Protocol.K_ROOMS));
				view.initializeGameRooms(maxRooms);
				break;

			case OK:
				switch (clientArgCmd)
				{
					case LOGIN -> view.login(message.args.get(Protocol.K_NICK));
					case JOIN_ROOM -> view.joinGameRoom(Integer.parseInt(message.args.get(Protocol.K_ROOM)));
					case LEAVE_ROOM -> view.returnToLobby();
					case QUIT -> view.quitGameRoom();
					/* TODO
					OK|command:RESUME
					 */
				}
				break;

			case ERROR:
				// Handle ERROR message
				if (clientArgCmd == null) {
					view.showErrorMessage("Error", "Error: " + clientArgMsg);
					break;
				}

				switch (clientArgCmd)
				{
					case LEAVE_ROOM -> view.showErrorMessage("Cannot Leave Room", "The game is already underway, you cannot leave.");
					case QUIT -> view.showErrorMessage("Cannot Quit Game", "You cannot quit the game at this moment.");
					default -> view.showErrorMessage("Error", "Error: " + clientArgMsg);
				}
				break;

			case ROOM_INFO:
				view.syncGameRoom(
					Integer.parseInt(message.args.get(Protocol.K_ROOM)),
					Integer.parseInt(message.args.get(Protocol.K_COUNT)),
					GameRoomStatus.valueOf(message.args.get(Protocol.K_STATE))
				);
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
