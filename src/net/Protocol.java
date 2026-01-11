package net;

public class Protocol {
    public enum ClientCommand {
        LOGIN,
        RESUME,
        LIST_ROOMS,
        JOIN_ROOM,
        LEAVE_ROOM,
        ROLL,
        HOLD,
        QUIT,
        EXIT,
        GAME_STATE_REQUEST,
        PING;

        public static ClientCommand fromString(String commandText) {
            if (commandText == null) {
                return null;
            }

            for (ClientCommand cmd : ClientCommand.values()) {
                if (cmd.name().equalsIgnoreCase(commandText)) {
                    return cmd;
                }
            }
            return null;
        }
    }

    public enum ServerCommand {
        OK,
        ERROR,
        WELCOME,
        GAME_PAUSED,
        ROOM_INFO,
        GAME_START,
        GAME_STATE,
        GAME_WIN,
        GAME_LOSE,
        OPPONENT_DISCONNECTED,
        OPPONENT_RECONNECTED
    }

    public enum ServerError {
        E_INVALID_COMMAND,
        E_INVALID_NICKNAME,
        E_SERVER_FULL,
        E_ROOM_FULL,
        E_GAME_IN_PROGRESS,
        E_CANNOT_JOIN,
        E_OPPONENT_QUIT,
        E_OPPONENT_TIMEOUT,
        E_NICKNAME_IN_USE
    }

    public static final String K_CMD = "cmd";
    public static final String K_MSG = "msg";
    public static final String K_NICK = "nick";
    public static final String K_ROOM = "room";
    public static final String K_STATE = "state";
    public static final String K_OPP_NICK = "opp_nick";
    public static final String K_YOUR_TURN = "your_turn";
    public static final String K_MY_SCORE = "my_score";
    public static final String K_OPP_SCORE = "opp_score";
    public static final String K_TURN_SCORE = "turn_score";
    public static final String K_CURRENT = "current";
    public static final String K_COUNT = "count";
    public static final String K_ROLL = "roll";
    public static final String K_PLAYERS = "players";
    public static final String K_ROOMS = "rooms";
}
