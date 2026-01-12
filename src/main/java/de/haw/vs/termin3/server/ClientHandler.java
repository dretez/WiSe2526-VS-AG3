package de.haw.vs.termin3.server;

import de.haw.vs.termin3.common.network.CommunicationInterface;
import de.haw.vs.termin3.server.registry.Registry;
import de.haw.vs.termin3.server.request.RequestHandler;

import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {
    private final Socket client;
    private final Registry registry;

    public ClientHandler(Socket client, Registry registry) {
        this.client = client;
        this.registry = registry;
    }

    @Override
    public void run() {
        while (true) {
            try {
                handleNextRequest();
            } catch (IOException _) {
                return;
            }
        }
    }

    private void handleNextRequest() throws IOException {
        String request = CommunicationInterface.awaitReply(client);
        if (request == null) throw new IOException("Connection closed");
        RequestHandler.handle(request, client,registry);
    }
}
