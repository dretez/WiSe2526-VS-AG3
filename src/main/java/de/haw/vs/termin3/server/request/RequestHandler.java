package de.haw.vs.termin3.server.request;

import de.haw.vs.termin3.common.json.JSONReader;
import de.haw.vs.termin3.server.Registry;

import java.net.Socket;

public abstract sealed class RequestHandler permits ExampleRequestHandler, RegisterRequestHandler, UnregisterRequestHandler,ListRequestHandler {
    public static void handle(String request, Socket client, Registry registry) {
        JSONReader reader = new JSONReader(request);
        RequestHandler handler = switch ((String) reader.get("request")) {
            case "example" -> new ExampleRequestHandler();
            case "regist" -> new RegisterRequestHandler(registry);
            case "unregist" -> new UnregisterRequestHandler(registry);
            case "list" -> new ListRequestHandler(registry);
            default -> throw new IllegalStateException("Unexpected value: " + reader.get("request"));
        };
        handler.handle(reader, client);
    }

    protected abstract void handle(JSONReader reader, Socket client);
}
