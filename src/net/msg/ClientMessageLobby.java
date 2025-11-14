package net.msg;

import net.Protocol;

abstract class ClientMessageLobby extends ClientMessage {
    public ClientMessageLobby(Protocol.ClientCommand cmd) {
        super(cmd);
    }
}
