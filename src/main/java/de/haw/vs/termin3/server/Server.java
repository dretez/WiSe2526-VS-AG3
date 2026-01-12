package de.haw.vs.termin3.server;

import de.haw.vs.termin3.server.registry.Registry;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    private final ServerSocket serverSocket;
    private final Registry registry;

    Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        registry = new Registry();
    }

    public void start() {
        while (true) {
            try {
                new ClientHandler(serverSocket.accept(), registry).start();
            } catch (IOException e) {
                return;
            }
        }
    }
}
