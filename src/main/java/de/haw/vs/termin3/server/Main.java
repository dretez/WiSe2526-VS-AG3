package de.haw.vs.termin3.server;

import de.haw.vs.termin3.common.network.Port;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Server server = new Server(Port.DEFAULT.port());
        server.start();
    }
}
