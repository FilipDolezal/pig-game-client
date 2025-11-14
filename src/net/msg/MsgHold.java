package net.msg;

import net.Protocol;

public class MsgHold extends ClientMessageGame {
    public MsgHold() {
        super(Protocol.ClientCommand.HOLD);
    }
}
