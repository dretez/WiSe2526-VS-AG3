package de.haw.vs.termin3.server.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.haw.vs.termin3.common.json.JSON;
import de.haw.vs.termin3.common.network.CommunicationInterface;
import de.haw.vs.termin3.server.ClientInterface;
import de.haw.vs.termin3.server.registry.EntryType;
import de.haw.vs.termin3.server.registry.Registry;
import de.haw.vs.termin3.server.registry.RegistryEntry;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Objects;

final class UseRobotRequest extends RequestHandler {
    @Override
    protected void handle(JsonNode json, ClientInterface client, Registry registry) {
        if (!json.path("name").isTextual()) {
            error(client.socket(), "No valid name provided");
            return;
        }
        String name = json.get("name").asText();
        List<RegistryEntry> list = registry.list(EntryType.ROBOT).stream().filter(
                e -> Objects.equals(e.getName(), name)
        ).toList();
        if (list.isEmpty()) {
            error(client.socket(), "No such robot available");
            return;
        }
        RegistryEntry entry = list.getFirst();
        ObjectNode builder = JSON.getEmptyObject();
        builder.put("request", "useReply");
        builder.put("ip", entry.getIp());
        builder.put("port", entry.getPort());
        try {
            CommunicationInterface.sendRequest(client.socket(), JSON.toString(builder));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void error(Socket client, String message) {
        ObjectNode builder = JSON.getEmptyObject();
        builder.put("request", "useReply");
        builder.put("status", "error");
        builder.put("message", message);
        try {
            CommunicationInterface.sendRequest(client, JSON.toString(builder));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
