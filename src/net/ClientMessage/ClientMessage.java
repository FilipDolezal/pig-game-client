package net.ClientMessage;

import net.Protocol;

public abstract class ClientMessage {
    public final Protocol.ClientCommand cmd;

    public ClientMessage(Protocol.ClientCommand cmd) {
        this.cmd = cmd;
    }

    @Override
    public String toString() {
        return cmd.toString();
    }
}

