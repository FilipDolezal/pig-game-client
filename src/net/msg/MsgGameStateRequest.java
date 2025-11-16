package net.msg;

import net.Protocol;

public final class MsgGameStateRequest extends ClientMessage {
    public MsgGameStateRequest() {
        super(Protocol.ClientCommand.GAME_STATE_REQUEST);
    }
}
