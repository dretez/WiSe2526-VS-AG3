package de.haw.vs.termin3.server.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.haw.vs.termin3.common.json.JSON;
import de.haw.vs.termin3.common.network.CommunicationInterface;
import de.haw.vs.termin3.server.registry.EntryType;
import de.haw.vs.termin3.server.registry.Registry;
import de.haw.vs.termin3.server.registry.RegistryException;

import java.io.IOException;
import java.net.Socket;

final class RegisterRequestHandler extends RequestHandler {
    RegisterRequestHandler(Registry registry) {
        super(registry);
    }

    @Override
    protected void handle(JsonNode json, Socket client) {
        String name = json.get("name").toString();
        String ip = json.get("ip").toString();
        int port = Integer.parseInt(json.get("port").toString());
        EntryType type = EntryType.fromString(json.get("type").toString());

        try {
            registry.register(name, ip, port,type);
            sendOk(client);
        } catch (RegistryException e) {
            sendError(client,e.getCode(), e.getMessage());
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

    private void sendOk(Socket client) {
        ObjectNode builder = JSON.getEmptyObject();
        builder.put("request", "registerReply");
        builder.put("status", "ok");
        try {
            CommunicationInterface.sendRequest(client, JSON.toString(builder));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}

