package de.haw.vs.termin3.server.request;

import de.haw.vs.termin3.common.json.JSONReader;
import de.haw.vs.termin3.server.registry.Registry;
import de.haw.vs.termin3.server.registry.RegistryException;

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
        } catch (RegistryException e) {
            System.err.println("Unable to unregister \"" + name + "\": " + e.getMessage());
        }
    }
}
