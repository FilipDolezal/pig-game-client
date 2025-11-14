package net.msg;

import net.Protocol;

public class MsgLeaveRoom extends ClientMessageLobby {
    public MsgLeaveRoom() {
        super(Protocol.ClientCommand.LEAVE_ROOM);
    }
}
