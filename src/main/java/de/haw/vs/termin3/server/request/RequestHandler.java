package de.haw.vs.termin3.server.request;

import de.haw.vs.termin3.common.json.JSONReader;

import java.net.Socket;

public abstract sealed class RequestHandler permits ExampleRequestHandler {
    public static void handle(String request, Socket client) {
        JSONReader reader = new JSONReader(request);
        RequestHandler handler = switch ((String) reader.get("request")) {
            case "example" -> new ExampleRequestHandler();
            // ...
            default -> throw new IllegalStateException("Unexpected value: " + reader.get("request"));
        };
        handler.handle(reader, client);
    }

    protected abstract void handle(JSONReader reader, Socket client);
}
