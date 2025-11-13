package net.ClientMessage;

import net.Protocol;

abstract class ClientMessageLobby extends ClientMessage {
    public ClientMessageLobby(Protocol.ClientCommand cmd) {
        super(cmd);
    }
}
