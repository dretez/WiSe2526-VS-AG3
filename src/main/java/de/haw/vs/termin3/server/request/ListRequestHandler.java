package de.haw.vs.termin3.server.request;

import de.haw.vs.termin3.common.json.JSONReader;
import de.haw.vs.termin3.server.registry.Registry;
import de.haw.vs.termin3.server.registry.RegistryException;

import java.net.Socket;

public final class ListRequestHandler extends RequestHandler{
    String request;

    ListRequestHandler(Registry registry) {
        super(registry);
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
