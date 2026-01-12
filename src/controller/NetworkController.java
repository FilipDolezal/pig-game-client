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
	private static final int RESPONSE_TIMEOUT_MS = 1500;

	private final Client client;
	private final NetworkToViewInterface view;
	private int maxPlayers;
	private int maxRooms;

	private Timer responseTimer;
	private ClientCommand pendingCommand;

	public NetworkController(ViewController viewController) {
		this.client = new Client();
		this.view = viewController;
		this.client.setOnPingSent(() -> SwingUtilities.invokeLater(() -> startResponseTimer(ClientCommand.PING, false)));
	}

	private void startResponseTimer(ClientCommand command, boolean clear) {
		if (clear)
		{
			cancelResponseTimer();
		}

		pendingCommand = command;
		responseTimer = new Timer(RESPONSE_TIMEOUT_MS, e -> {
			SwingUtilities.invokeLater(() -> view.showNoResponseWarning(command.name()));
		});
		responseTimer.setRepeats(false);
		responseTimer.start();
	}

	private void cancelResponseTimer() {
		if (responseTimer != null) {
			responseTimer.stop();
			responseTimer = null;
		}
		pendingCommand = null;
		SwingUtilities.invokeLater(view::hideNoResponseWarning);
	}

	public void connect(String ip, int port, String nickname) {
		try {
			client.connect(ip, port);
			client.sendMessage(new MsgLogin(nickname));
			startResponseTimer(ClientCommand.LOGIN, true);
			// Start a new thread to listen for messages from the server
			new Thread(this::listenForMessages).start();
		} catch (IOException e) {
			SwingUtilities.invokeLater(() ->
				view.showErrorMessage("Connection Error", "Connection failed: " + e.getMessage())
			);
		}
	}

	public void sendListRooms() {
		client.sendMessage(new MsgListRooms());
		startResponseTimer(ClientCommand.LIST_ROOMS, true);
	}

	@Override
	public void sendJoinRoom(int roomId) {
		client.sendMessage(new MsgJoinRoom(roomId));
		startResponseTimer(ClientCommand.JOIN_ROOM, true);
	}

	@Override
	public void sendQuitGame() {
		client.sendMessage(new MsgQuit());
		startResponseTimer(ClientCommand.QUIT, true);
	}

	@Override
	public void sendLeaveRoom() {
		client.sendMessage(new MsgLeaveRoom());
		startResponseTimer(ClientCommand.LEAVE_ROOM, true);
	}

	@Override
	public void sendRoll() {
		client.sendMessage(new MsgRoll());
		startResponseTimer(ClientCommand.ROLL, true);
	}

	@Override
	public void sendHold() {
		client.sendMessage(new MsgHold());
		startResponseTimer(ClientCommand.HOLD, true);
	}

	@Override
	public void sendExit() {
		cancelResponseTimer();
		client.sendMessage(new MsgExit());
		// EXIT doesn't expect a response
	}

	@Override
	public void sendResume() {
		client.sendMessage(new net.msg.MsgResume());
		startResponseTimer(ClientCommand.RESUME, true);
	}

	@Override
	public void sendGameStateRequest() {
		client.sendMessage(new net.msg.MsgGameStateRequest());
		startResponseTimer(ClientCommand.GAME_STATE_REQUEST, true);
	}

	private void listenForMessages() {
		try {
			String fromServer;
			while ((fromServer = client.readMessage()) != null) {

				ServerMessage parsedMessage = ServerMessage.parse(fromServer);
				if (parsedMessage != null) {
					SwingUtilities.invokeLater(() -> handleServerMessage(parsedMessage));
				} else {
					// Server sent unparseable garbage
					String garbage = fromServer;
					System.err.println("Invalid message from server: " + garbage);
					SwingUtilities.invokeLater(() ->
						view.showNoResponseWarning("INVALID SERVER MESSAGE: " +
							(garbage.length() > 30 ? garbage.substring(0, 30) + "..." : garbage))
					);
				}

			}
		} catch (IOException e) {
			SwingUtilities.invokeLater(() ->
				view.showErrorMessage("Disconnection", e.getMessage())
			);
		}
	}

	private void handleServerMessage(ServerMessage message) {
		System.out.println(message);
		ClientCommand clientArgCmd = ClientCommand.fromString(message.args.getOrDefault(Protocol.K_CMD, null));
		String clientArgMsg = message.args.getOrDefault(Protocol.K_MSG, null);

		// Cancel response timer based on which response we received
		if (pendingCommand != null) {
			boolean shouldCancel = switch (message.cmd) {
				case OK, ERROR -> clientArgCmd == pendingCommand;
				case ROOM_INFO -> pendingCommand == ClientCommand.LIST_ROOMS;
				case GAME_STATE -> pendingCommand == ClientCommand.ROLL ||
								   pendingCommand == ClientCommand.HOLD ||
								   pendingCommand == ClientCommand.GAME_STATE_REQUEST;
				case WELCOME, GAME_START, GAME_WIN, GAME_LOSE, GAME_PAUSED,
					 OPPONENT_DISCONNECTED, OPPONENT_RECONNECTED, DISCONNECTED -> true;
			};
			if (shouldCancel) {
				cancelResponseTimer();
			}
		}

		switch (message.cmd) {
			case WELCOME:
				maxPlayers = Integer.parseInt(message.args.get(Protocol.K_PLAYERS));
				maxRooms = Integer.parseInt(message.args.get(Protocol.K_ROOMS));
				client.startHeartbeat();
				view.initializeGameRooms(maxRooms);
				break;

			case OK:
				switch (clientArgCmd)
				{
					case LOGIN -> view.login(message.args.get(Protocol.K_NICK));
					case JOIN_ROOM -> view.joinGameRoom(Integer.parseInt(message.args.get(Protocol.K_ROOM)));
					case LEAVE_ROOM -> view.returnToLobby();
					case QUIT -> view.quitGameRoom();
					case RESUME -> view.resumeGame();
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

			case GAME_PAUSED:
				view.showGamePausedDialog();
				break;

			case OPPONENT_DISCONNECTED:
				view.showOpponentDisconnected();
				break;

			case DISCONNECTED:
					client.stopHeartbeat();
					view.disconnect();
					break;

			case OPPONENT_RECONNECTED:
				view.hideOpponentDisconnected();
				break;
		}
	}
}
