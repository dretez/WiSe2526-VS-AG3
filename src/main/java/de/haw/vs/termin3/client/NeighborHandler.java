package de.haw.vs.termin3.client;

import com.fasterxml.jackson.databind.JsonNode;
import de.haw.vs.termin3.common.json.JSON;
import de.haw.vs.termin3.common.network.CommunicationInterface;

import java.io.IOException;
import java.net.Socket;

public class NeighborHandler extends Thread {
    private final Socket neighbor;
    private final Client client;

    public NeighborHandler(Socket neighbor, Client client) {
        this.neighbor = neighbor;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            String request = CommunicationInterface.awaitReply(neighbor);
            JsonNode json = JSON.parse(request);
            if ("token".equals(json.path("request").asText())) {
                client.giveToken();
                System.out.println("Token received");
            }
        } catch (IOException e) {
            System.err.println("Failed to receive request from neighbor (" + neighbor + "): " + e.getMessage());
        } finally {
            try {
                neighbor.close();
            } catch (IOException _) {
            }
        }
    }
}
