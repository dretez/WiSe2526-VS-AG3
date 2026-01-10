package de.haw.vs.termin3.server;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerMain {
    private ServerSocket serverSocket;
    private final ServerDataStore dataStore;

    private ServerMain() {
        this.dataStore = new ServerDataStore();
    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true)
            new ServerStub(serverSocket.accept(), dataStore).start();
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    public static void main(String[] args) {
        ServerMain server = new ServerMain();
        try {
            server.start(3000);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        try {
            server.stop();
        } catch (IOException _) {
        }
    }
}
