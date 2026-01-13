package de.haw.vs.termin3.client.command;

import de.haw.vs.termin3.client.Terminal;

import java.util.List;

final class HelpCmd extends ClientCommand {
    @Override
    protected void handle(List<String> args, Terminal terminal) {
        System.out.println("Commands:");
        System.out.println("help - Prints this list");
        System.out.println("list - Shows all available robots, as well as currently selected robots");
        System.out.println("use <name> - Connects to a robot. Use list to find robot names");
        System.out.println("release <name> - Releases control of a robot. Use list to find what robots are being used");
        System.out.println("lr <percentage> - Rotates all selected robots left or right to the specified percentage");
        System.out.println("home - Sends all selected robots to their default position");
        System.out.println("quit - closes this terminal");
    }
}
