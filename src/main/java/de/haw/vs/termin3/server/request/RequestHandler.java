package de.haw.vs.termin3.server.request;

import de.haw.vs.termin3.common.json.JSONReader;
import de.haw.vs.termin3.server.registry.Registry;

import java.net.Socket;

public abstract sealed class RequestHandler permits RegisterRequestHandler, UnregisterRequestHandler,ListRequestHandler {
    protected final Registry registry;

    protected RequestHandler(Registry registry) {
        this.registry = registry;
    }

    public static void handle(String request, Socket client, Registry registry) {
        JSONReader reader = new JSONReader(request);
        RequestHandler handler = switch ((String) reader.get("request")) {
            case "register" -> new RegisterRequestHandler(registry);
            case "unregister" -> new UnregisterRequestHandler(registry);
            case "list" -> new ListRequestHandler(registry);
            default -> throw new IllegalStateException("Unexpected value: " + reader.get("request"));
        };
        handler.handle(reader, client);
    }

    protected abstract void handle(JSONReader reader, Socket client);
    protected abstract void sendError(Socket client, String code, String message);
}
