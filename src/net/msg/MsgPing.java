package net.msg;

import net.Protocol;

public class MsgPing extends ClientMessage {
    public MsgPing() {
        super(Protocol.ClientCommand.PING);
    }
}
