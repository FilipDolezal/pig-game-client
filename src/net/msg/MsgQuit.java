package net.msg;

import net.Protocol;

public class MsgQuit extends ClientMessageGame {
    public MsgQuit() {
        super(Protocol.ClientCommand.QUIT);
    }
}
