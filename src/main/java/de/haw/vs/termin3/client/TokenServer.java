package de.haw.vs.termin3.client;

import java.io.IOException;
import java.net.ServerSocket;

public class TokenServer extends Thread {
    private final ServerSocket server;
    private final Client client;

    public TokenServer(ServerSocket server, Client client) {
        this.server = server;
        this.client = client;
    }

    @Override
    public void run() {
        while (!server.isClosed()) {
            try {
                new NeighborHandler(server.accept(), client).start();
            } catch (IOException _) {
            }
        }
    }
}
