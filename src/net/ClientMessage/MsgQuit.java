package net.ClientMessage;

import net.Protocol;

public class MsgQuit extends ClientMessageGame {
    public MsgQuit() {
        super(Protocol.ClientCommand.QUIT);
    }
}
