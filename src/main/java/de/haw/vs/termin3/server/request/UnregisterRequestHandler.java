package de.haw.vs.termin3.server.request;

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

    @Override
    protected void sendError(Socket client, String code, String message) {
        String request =
                "{"
                        + "\"request\":\"UnregistReply\","
                        + "\"status\":\"error\","
                        + "\"error\":\"" + code + "\","
                        + "\"message\":\"" + message + "\""
                        + "}";
        try {
            CommunicationInterface.sendRequest(client, request);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void sendOk(Socket client) {
        String response =
                "{"
                        + "\"request\":\"uregisterReply\","
                        + "\"status\":\"ok\""
                        + "}";
        try {
            CommunicationInterface.sendRequest(client, response);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
