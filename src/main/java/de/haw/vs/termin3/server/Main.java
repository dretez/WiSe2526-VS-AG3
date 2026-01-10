package de.haw.vs.termin3.server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Server server = new Server(3000);
        server.start();
    }
}
