package net.msg;

import net.Protocol;

public final class MsgResume extends ClientMessage {
    public MsgResume() {
        super(Protocol.ClientCommand.RESUME);
    }
}