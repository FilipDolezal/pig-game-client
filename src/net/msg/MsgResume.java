package net.msg;

import net.Protocol;

public class MsgResume extends ClientMessageLobby {
    public MsgResume() {
        super(Protocol.ClientCommand.RESUME);
    }
}
