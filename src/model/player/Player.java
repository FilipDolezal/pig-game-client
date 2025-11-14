package model.player;

public class Player {
    private int currentRoomId;
    private final String nick;
    private PlayerState state;

    public Player(String nick) {
        this.nick = nick;
        this.currentRoomId = -1; // Default to no room
        this.state = PlayerState.LOBBY; // Default to lobby
    }

    public int getCurrentRoomId() {
        return currentRoomId;
    }

    public void setCurrentRoomId(int currentRoomId) {
        this.currentRoomId = currentRoomId;
    }

    public String getNick() {
        return nick;
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }
}
