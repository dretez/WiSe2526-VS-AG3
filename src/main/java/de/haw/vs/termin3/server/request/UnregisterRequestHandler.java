package de.haw.vs.termin3.server.request;

import de.haw.vs.termin3.common.json.JSONReader;
import de.haw.vs.termin3.server.Registry;
import de.haw.vs.termin3.server.RegistryException;

import java.net.Socket;

public final class UnregisterRequestHandler extends RequestHandler {
    String request;
    Registry registry;

    UnregisterRequestHandler(Registry registry) {
        this.registry = registry;
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
