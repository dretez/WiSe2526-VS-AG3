package de.haw.vs.termin3.server.request;

import de.haw.vs.termin3.common.json.JSONReader;
import de.haw.vs.termin3.server.Registry;
import de.haw.vs.termin3.server.RegistryException;

import java.net.Socket;

public final class ListRequestHandler extends RequestHandler{
    String request;
    Registry registry;

    ListRequestHandler(Registry registry) {
        this.registry = registry;
    }

    @Override
    protected void handle(JSONReader reader, Socket client) {
        String type = reader.get("type").toString();
        try {
            registry.list(type);
        } catch (RegistryException e) {
            throw new RuntimeException(e);
        }
    }
}
