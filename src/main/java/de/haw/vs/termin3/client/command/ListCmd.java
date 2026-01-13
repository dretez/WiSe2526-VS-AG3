package de.haw.vs.termin3.client.command;

import de.haw.vs.termin3.client.Terminal;

import java.io.IOException;
import java.util.List;

final class ListCmd extends ClientCommand {
    ListCmd() {}

    @Override
    protected void handle(List<String> args, Terminal terminal) throws IOException {
        terminal.client().list();
    }
}
