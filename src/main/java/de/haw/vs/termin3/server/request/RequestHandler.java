package de.haw.vs.termin3.server.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import de.haw.vs.termin3.common.json.JSON;
import de.haw.vs.termin3.server.ClientInterface;
import de.haw.vs.termin3.server.registry.Registry;

public abstract sealed class RequestHandler permits ListRequestHandler, RegisterRequestHandler, UnregisterRequestHandler, UseRobotRequest {
    public static void handle(String request, ClientInterface client, Registry registry) throws JsonProcessingException {
        JsonNode json = JSON.parse(request);
        RequestHandler handler = switch (json.get("request").asText()) {
            case "register" -> new RegisterRequestHandler();
            case "unregister" -> new UnregisterRequestHandler();
            case "list" -> new ListRequestHandler();
            case "use" -> new UseRobotRequest();
            default -> throw new IllegalStateException("Unexpected value: " + json.get("request"));
        };
        handler.handle(json, client, registry);
    }

    protected abstract void handle(JsonNode json, ClientInterface client, Registry registry);
}
