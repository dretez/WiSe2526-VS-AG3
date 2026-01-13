package de.haw.vs.termin3.client.command;

import de.haw.vs.termin3.client.Terminal;

import java.io.IOException;
import java.util.List;

final class LeftRightCmd extends ClientCommand {
    @Override
    protected void handle(List<String> args, Terminal terminal) throws IOException {
        if (args.size() < 2)
            throw new IllegalArgumentException("Expected use: " + args.getFirst() + " <percentage>");
        if (args.size() > 2)
            throw new IllegalArgumentException("Too many arguments, expected 2, received: " + args.size());
        terminal.client().rotateRobotsLR(Integer.parseInt(args.get(1)));
    }
}
