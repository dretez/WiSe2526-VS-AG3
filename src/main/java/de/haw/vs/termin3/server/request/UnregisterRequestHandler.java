package de.haw.vs.termin3.server.request;

import de.haw.vs.termin3.common.json.JSONBuilder;
import de.haw.vs.termin3.common.json.JSONReader;
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
    protected void handle(JSONReader reader, Socket client) {
        String name = reader.get("name").toString();
        try {
            registry.unregister(name);
            sendOk(client);
        } catch (RegistryException e) {
            sendError(client, e.getCode(), e.getMessage());
            System.err.println("Unable to unregister \"" + name + "\": " + e.getMessage());
        }
    }

    private void sendError(Socket client, String code, String message) {
        JSONBuilder builder = new JSONBuilder();
        builder.putString("request", "unregisterReply");
        builder.putString("status", "error");
        builder.putString("error", code);
        builder.putString("message", message);
        try {
            CommunicationInterface.sendRequest(client, builder.toString());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void sendOk(Socket client) {
        JSONBuilder builder = new JSONBuilder();
        builder.putString("request", "unregisterReply");
        builder.putString("status", "ok");
        try {
            CommunicationInterface.sendRequest(client, builder.toString());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
