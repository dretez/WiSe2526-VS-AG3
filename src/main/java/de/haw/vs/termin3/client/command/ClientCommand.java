package de.haw.vs.termin3.client.command;

import de.haw.vs.termin3.client.Terminal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract sealed class ClientCommand permits GoHomeCmd, HelpCmd, LeftRightCmd, ListCmd, QuitCmd, ReleaseRobotCmd, UseRobotCmd {
    public static void handle(String[] args, Terminal terminal) {
        List<String> argList = new ArrayList<>(List.of(args));
        try {
            ClientCommand handler = switch (argList.getFirst()) {
                case "help" -> new HelpCmd();
                case "list" -> new ListCmd();
                case "use" -> new UseRobotCmd();
                case "release" -> new ReleaseRobotCmd();
                case "lr" -> new LeftRightCmd();
                case "home" -> new GoHomeCmd();
                case "quit" -> new QuitCmd();
                default -> throw new IllegalArgumentException("Unknown command: " + argList.getFirst());
            };
            handler.handle(argList, terminal);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    protected abstract void handle(List<String> args, Terminal terminal) throws IOException;
}
