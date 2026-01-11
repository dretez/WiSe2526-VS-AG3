package de.haw.vs.termin3.server.request;

import de.haw.vs.termin3.common.json.JSONReader;
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
    protected void handle(JSONReader reader, Socket client) {
        String name = reader.get("name").toString();
        String ip = reader.get("ip").toString();
        int port = Integer.parseInt(reader.get("port").toString());
        EntryType type = EntryType.fromString(reader.get("type").toString());

        try {
            registry.register(name, ip, port,type);
            sendOk(client);
        } catch (RegistryException e) {
            sendError(client,e.getCode(), e.getMessage());
        }
    }
    @Override
    protected void sendError(Socket client, String code, String message){
        String request =
                "{"
                        + "\"request\":\"registerReply\","
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
                        + "\"request\":\"registerReply\","
                        + "\"status\":\"ok\""
                        + "}";
        try {
            CommunicationInterface.sendRequest(client, response);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}

