package de.haw.vs.termin3.client.command;

import de.haw.vs.termin3.client.Terminal;
import de.haw.vs.termin3.client.exceptions.RobotUnavailableException;

import java.io.IOException;
import java.util.List;

final class ReleaseRobotCmd extends ClientCommand {
    @Override
    protected void handle(List<String> args, Terminal terminal) throws IOException {
        if (args.size() < 2)
            throw new IllegalArgumentException("Expected use: " + args.getFirst() + " <name>");
        if (args.size() > 2)
            throw new IllegalArgumentException("Too many arguments, expected 2, received: " + args.size());
        try {
            terminal.client().releaseRobot(args.get(1));
        } catch (RobotUnavailableException e) {
            System.err.println(e.getMessage());
        }
    }
}
