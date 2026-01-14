package de.haw.vs.termin3.server.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.haw.vs.termin3.common.json.JSON;
import de.haw.vs.termin3.common.network.CommunicationInterface;
import de.haw.vs.termin3.server.ClientInterface;
import de.haw.vs.termin3.server.registry.EntryType;
import de.haw.vs.termin3.server.registry.Registry;
import de.haw.vs.termin3.server.registry.RegistryEntry;
import de.haw.vs.termin3.server.registry.RegistryException;

import java.io.IOException;
import java.net.Socket;

final class RegisterRequestHandler extends RequestHandler {
    @Override
    protected void handle(JsonNode json, ClientInterface client, Registry registry) {
        String name = json.get("name").asText();
        String ip = json.get("ip").asText();
        int port = Integer.parseInt(json.get("port").asText());
        EntryType type = EntryType.fromString(json.get("type").asText());

        try {
            if (client.entry() != null)
                throw new RegistryException("ILLEGAL_REGISTRATION", "This socket has already been registered");
            RegistryEntry entry = registry.register(name, ip, port, type);
            client.setEntry(entry);
            sendOk(client.socket(), entry);
        } catch (RegistryException e) {
            sendError(client.socket(), e.getCode(), e.getMessage());
        }
    }

    private void sendError(Socket client, String code, String message){
        ObjectNode builder = JSON.getEmptyObject();
        builder.put("request", "registerReply");
        builder.put("status", "error");
        builder.put("error", code);
        builder.put("message", message);
        try {
            CommunicationInterface.sendRequest(client, JSON.toString(builder));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void sendOk(Socket client, RegistryEntry entry) {
        ObjectNode builder = JSON.getEmptyObject();
        builder.put("request", "registerReply");
        builder.put("id", entry.getId());
        builder.put("status", "ok");
        try {
            CommunicationInterface.sendRequest(client, JSON.toString(builder));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}

