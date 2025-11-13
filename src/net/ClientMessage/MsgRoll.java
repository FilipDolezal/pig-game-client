package net.ClientMessage;

import net.Protocol;

public class MsgRoll extends ClientMessageGame {
    public MsgRoll() {
        super(Protocol.ClientCommand.ROLL);
    }
}
