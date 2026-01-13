package de.haw.vs.termin3.server.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.haw.vs.termin3.common.json.JSON;
import de.haw.vs.termin3.common.network.CommunicationInterface;
import de.haw.vs.termin3.server.ClientInterface;
import de.haw.vs.termin3.server.registry.EntryType;
import de.haw.vs.termin3.server.registry.Registry;

import java.io.IOException;

final class ListRequestHandler extends RequestHandler{
    @Override
    protected void handle(JsonNode json, ClientInterface client, Registry registry) {
        EntryType type = EntryType.fromString(json.get("type").asText());
        ObjectNode builder = JSON.getEmptyObject();
        builder.put("request", "listReply");
        ArrayNode list = builder.putArray("list");
        registry.list(type).forEach(list::addPOJO);
        try {
            CommunicationInterface.sendRequest(client.socket(), builder.toString());
        } catch (IOException e) {
            System.err.println("Unable to send list to client: " + e.getMessage());
        }
    }
}
