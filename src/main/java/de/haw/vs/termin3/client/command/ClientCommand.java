package de.haw.vs.termin3.client.command;

import de.haw.vs.termin3.client.Terminal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract sealed class ClientCommand permits HelpCmd, ListCmd, QuitCmd {
    public static void handle(String[] args, Terminal terminal) {
        List<String> argList = new ArrayList<>(List.of(args));
        try {
            ClientCommand handler = switch (argList.getFirst()) {
                case "help" -> new HelpCmd();
                case "list" -> new ListCmd();
                case "quit" -> new QuitCmd();
                default -> throw new IllegalArgumentException();
            };
            handler.handle(argList, terminal);
        } catch (IllegalArgumentException _) {
            invalidCmd(argList.getFirst());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    protected abstract void handle(List<String> args, Terminal terminal) throws IOException;

    private static void invalidCmd(String cmd) {
        System.out.println("Unknown command: " + cmd);
    }
}
