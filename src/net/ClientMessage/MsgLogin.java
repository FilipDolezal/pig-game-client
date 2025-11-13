package net.ClientMessage;

import net.Protocol;

public final class MsgLogin extends ClientMessage {
    public final String nick;

    public MsgLogin(String nick)
    {
        super(Protocol.ClientCommand.LOGIN);
        this.nick = nick;
    }

    @Override
    public String toString() {
        return super.toString() + "|" +
                Protocol.K_NICK + ":" + nick;
    }
}
