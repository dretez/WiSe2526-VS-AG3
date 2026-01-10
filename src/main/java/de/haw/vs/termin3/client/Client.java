package de.haw.vs.termin3.client;

import java.io.IOException;
import java.net.Socket;

public class Client {
    private static final int DEFAULT_PORT = 3000;
    private static final String DEFAULT_HOST = "127.0.0.1";

    private final Socket clientSocket;

    public Client(String host, int port) throws IOException {
        this.clientSocket = new Socket(host, port);
    }
    public Client(String host) throws IOException {
        this(host, DEFAULT_PORT);
    }
    public Client() throws IOException {
        this(DEFAULT_HOST);
    }

    public void stop() throws IOException {
        clientSocket.close();
    }
}
