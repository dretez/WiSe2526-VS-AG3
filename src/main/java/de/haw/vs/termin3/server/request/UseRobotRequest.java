package de.haw.vs.termin3.server.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.haw.vs.termin3.common.json.JSON;
import de.haw.vs.termin3.common.network.CommunicationInterface;
import de.haw.vs.termin3.server.registry.EntryType;
import de.haw.vs.termin3.server.registry.Registry;
import de.haw.vs.termin3.server.registry.RegistryEntry;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Objects;

final class UseRobotRequest extends RequestHandler {
    UseRobotRequest(Registry registry) {
        super(registry);
    }

    @Override
    protected void handle(JsonNode json, Socket client) {
        if (!json.path("name").isTextual())
            error(client, "No valid name provided");
        String name = json.get("name").asText();
        RegistryEntry entry = registry.list(EntryType.ROBOT).stream().filter(
                e -> Objects.equals(e.getName(), name)
        ).toList().getFirst();
        ObjectNode builder = JSON.getEmptyObject();
        builder.put("request", "useReply");
        builder.put("ip", entry.getIp());
        builder.put("port", entry.getPort());
        try {
            CommunicationInterface.sendRequest(client, JSON.toString(builder));
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
