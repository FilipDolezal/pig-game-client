package net.ClientMessage;

import net.Protocol;

abstract class ClientMessageGame extends ClientMessage {
    public ClientMessageGame(Protocol.ClientCommand cmd) {
        super(cmd);
    }
}
