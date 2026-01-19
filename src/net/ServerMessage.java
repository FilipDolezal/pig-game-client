package net;

import net.Protocol.ServerCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class ServerMessage {

    public final ServerCommand cmd;
    public final Map<String, String> args;

    private ServerMessage(ServerCommand command, Map<String, String> args) {
        this.cmd = command;
        this.args = args;
    }

    public static ServerMessage parse(String message) {
        if (message == null || message.trim().isEmpty()) {
            return null;
        }

        String[] parts = message.split("\\|");
        String commandStr = parts[0];

        try {
            Protocol.ServerCommand command = Protocol.ServerCommand.valueOf(commandStr.toUpperCase());
            Map<String, String> args = new HashMap<>();
            for (int i = 1; i < parts.length; i++) {
                String[] argParts = parts[i].split(":", 2);

                if (argParts.length == 2) {
                    args.put(argParts[0], argParts[1]);
                }
            }
            return new ServerMessage(command, args);
        } catch (IllegalArgumentException e) {
            // Handle unknown commands
            return null;
        }
    }

    @Override
    public String toString() {
        if (args.isEmpty())
        {
            return cmd.toString();
        }

        return String.format(
            "%s|%s",
            cmd.toString(),
            args
                .entrySet()
                .stream()
                .map(e -> String.format("%s:%s", e.getKey(), e.getValue()))
                .collect(Collectors.joining("|"))
        );
    }
}
