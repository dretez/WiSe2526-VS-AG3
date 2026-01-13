package de.haw.vs.termin3.client.command;

import de.haw.vs.termin3.client.Terminal;

import java.util.List;

non-sealed class HelpCmd extends ClientCommand {
    HelpCmd() {}

    @Override
    protected void handle(List<String> args, Terminal terminal) {
        System.out.println("\nCommands: list | quit");
    }
}
