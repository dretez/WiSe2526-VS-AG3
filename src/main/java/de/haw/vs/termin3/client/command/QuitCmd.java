package de.haw.vs.termin3.client.command;

import de.haw.vs.termin3.client.Terminal;

import java.io.IOException;
import java.util.List;

public non-sealed class QuitCmd extends ClientCommand {
    QuitCmd() {}

    @Override
    protected void handle(List<String> args, Terminal terminal) throws IOException {
        terminal.client().unregister();
        terminal.client().stop();
        terminal.stop();
    }
}
