package de.haw.vs.termin3.client.command;

import de.haw.vs.termin3.client.Terminal;

import java.io.IOException;
import java.util.List;

final class GoHomeCmd extends ClientCommand {
    @Override
    protected void handle(List<String> args, Terminal terminal) throws IOException {
        if (args.size() > 1)
            throw new IllegalArgumentException("Too many arguments, expected 1, received: " + args.size());
        terminal.client().resetRobotPositions();
    }
}
