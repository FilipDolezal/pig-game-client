package net.ClientMessage;

import net.Protocol;

public class MsgListRooms extends ClientMessageLobby {
    public MsgListRooms() {
        super(Protocol.ClientCommand.LIST_ROOMS);
    }
}
