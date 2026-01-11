package de.haw.vs.termin3.server.request;

import de.haw.vs.termin3.common.json.JSONBuilder;
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

    private void sendError(Socket client, String code, String message){
        JSONBuilder builder = new JSONBuilder();
        builder.putString("request", "registerReply");
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
        builder.putString("request", "registerReply");
        builder.putString("status", "ok");
        try {
            CommunicationInterface.sendRequest(client, builder.toString());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}

