package de.haw.vs.termin3.server.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import de.haw.vs.termin3.common.json.JSON;
import de.haw.vs.termin3.server.registry.Registry;

import java.net.Socket;

public abstract sealed class RequestHandler permits ListRequestHandler, RegisterRequestHandler, UnregisterRequestHandler, UseRobotRequest {
    protected final Registry registry;

    protected RequestHandler(Registry registry) {
        this.registry = registry;
    }

    public static void handle(String request, Socket client, Registry registry) throws JsonProcessingException {
        JsonNode json = JSON.parse(request);
        RequestHandler handler = switch (json.get("request").asText()) {
            case "register" -> new RegisterRequestHandler(registry);
            case "unregister" -> new UnregisterRequestHandler(registry);
            case "list" -> new ListRequestHandler(registry);
            case "use" -> new UseRobotRequest(registry);
            default -> throw new IllegalStateException("Unexpected value: " + json.get("request"));
        };
        handler.handle(json, client);
    }

    protected abstract void handle(JsonNode json, Socket client);
}
