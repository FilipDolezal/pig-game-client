package net.msg;

import net.Protocol;

public class MsgQuit extends ClientMessage {
    public MsgQuit() {
        super(Protocol.ClientCommand.QUIT);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}