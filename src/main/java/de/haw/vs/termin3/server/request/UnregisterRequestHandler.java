package de.haw.vs.termin3.server.request;

import de.haw.vs.termin3.common.json.JSONReader;
import de.haw.vs.termin3.server.registry.Registry;
import de.haw.vs.termin3.server.registry.RegistryException;

import java.net.Socket;

public final class UnregisterRequestHandler extends RequestHandler {
    String request;

    UnregisterRequestHandler(Registry registry) {
        super(registry);
    }

    @Override
    protected void handle(JSONReader reader, Socket client) {
        String name = reader.get("name").toString();
        try {
            registry.unregister(name);
        } catch (RegistryException e) {
            throw new RuntimeException(e);
        }
    }
}
