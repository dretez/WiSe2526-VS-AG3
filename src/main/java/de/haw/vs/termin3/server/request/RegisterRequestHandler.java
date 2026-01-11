package de.haw.vs.termin3.server.request;

import de.haw.vs.termin3.common.json.JSONReader;
import de.haw.vs.termin3.server.registry.Registry;
import de.haw.vs.termin3.server.registry.RegistryException;

import java.net.Socket;

public final class RegisterRequestHandler extends RequestHandler {
    String request;

    RegisterRequestHandler(Registry registry) {
        super(registry);
    }

    @Override
    protected void handle(JSONReader reader, Socket client) {
        String name = reader.get("name").toString();
        String ip = reader.get("ip").toString();
        int port = Integer.parseInt(reader.get("port").toString());
        String type = reader.get("type").toString();

        try {
            registry.register(name, ip, port,type);
        } catch (RegistryException e) {
            throw new RuntimeException(e);
        }
    }
}

