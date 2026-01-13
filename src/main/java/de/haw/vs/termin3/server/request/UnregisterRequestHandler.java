package de.haw.vs.termin3.server.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.haw.vs.termin3.common.json.JSON;
import de.haw.vs.termin3.common.network.CommunicationInterface;
import de.haw.vs.termin3.server.registry.Registry;
import de.haw.vs.termin3.server.registry.RegistryException;

import java.io.IOException;
import java.net.Socket;

final class UnregisterRequestHandler extends RequestHandler {
    UnregisterRequestHandler(Registry registry) {
        super(registry);
    }

    @Override
    protected void handle(JsonNode json, Socket client) {
        String name = json.get("name").toString();
        try {
            registry.unregister(name);
            sendOk(client);
        } catch (RegistryException e) {
            sendError(client, e.getCode(), e.getMessage());
            System.err.println("Unable to unregister \"" + name + "\": " + e.getMessage());
        }
    }

    private void sendError(Socket client, String code, String message) {
        ObjectNode builder = JSON.getEmptyObject();
        builder.put("request", "unregisterReply");
        builder.put("status", "error");
        builder.put("error", code);
        builder.put("message", message);
        try {
            CommunicationInterface.sendRequest(client, JSON.toString(builder));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void sendOk(Socket client) {
        ObjectNode builder = JSON.getEmptyObject();
        builder.put("request", "unregisterReply");
        builder.put("status", "ok");
        try {
            CommunicationInterface.sendRequest(client, JSON.toString(builder));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
