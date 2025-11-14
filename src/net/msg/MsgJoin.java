package net.msg;

import net.Protocol;

public class MsgJoin extends ClientMessageLobby {
    public final String room;

    public MsgJoin(String room) {
        super(Protocol.ClientCommand.JOIN_ROOM);
        this.room = room;
    }

    @Override
    public String toString() {
        return super.toString() + "|" +
                Protocol.K_ROOM + ":" + room;
    }
}
