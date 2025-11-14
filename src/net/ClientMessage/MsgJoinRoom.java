package net.ClientMessage;

import net.Protocol;

import static net.Protocol.ClientCommand.JOIN_ROOM;

public class MsgJoinRoom extends ClientMessage {
    private final int roomId;

    public MsgJoinRoom(int roomId) {
        super(JOIN_ROOM);
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return String.format("%s|%s:%d", super.toString(), Protocol.K_ROOM, roomId);
    }
}
