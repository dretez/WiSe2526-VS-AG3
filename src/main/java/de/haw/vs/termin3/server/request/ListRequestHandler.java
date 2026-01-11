package de.haw.vs.termin3.server.request;

import de.haw.vs.termin3.common.json.JSONBuilder;
import de.haw.vs.termin3.common.json.JSONReader;
import de.haw.vs.termin3.common.network.CommunicationInterface;
import de.haw.vs.termin3.server.registry.EntryType;
import de.haw.vs.termin3.server.registry.Registry;
import de.haw.vs.termin3.server.registry.RegistryEntry;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

final class ListRequestHandler extends RequestHandler{
    ListRequestHandler(Registry registry) {
        super(registry);
    }

    @Override
    protected void handle(JSONReader reader, Socket client) {
        EntryType type = EntryType.fromString(reader.get("type").toString());
        List<String> list = registry.list(type).stream().map(RegistryEntry::toJSON).toList();
        JSONBuilder builder = new JSONBuilder();
        builder.putString("request", "listReply");
        builder.putArray("list", list);
        try {
            CommunicationInterface.sendRequest(client, builder.toString());
        } catch (IOException e) {
            System.err.println("Unable to send list to client: " + e.getMessage());
        }
    }
}
