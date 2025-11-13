package net.ClientMessage;

import net.Protocol;

public class MsgExit extends ClientMessageLobby {
    public MsgExit() {
        super(Protocol.ClientCommand.EXIT);
    }
}
