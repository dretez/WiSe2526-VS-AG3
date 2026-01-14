package de.haw.vs.termin3.client.command;

import com.fasterxml.jackson.databind.JsonNode;
import de.haw.vs.termin3.client.Terminal;

import java.io.IOException;
import java.util.List;

final class ListCmd extends ClientCommand {
    @Override
    protected void handle(List<String> args, Terminal terminal) throws IOException {
        if (!terminal.client().robots().isEmpty())
            System.out.println("Currently selected robots:");
        terminal.client().robots().forEach(System.out::println);
        List<JsonNode> list = terminal.client().list();
        if (!list.isEmpty()) {
            System.out.println("Available robots:");
            list.forEach(i -> System.out.printf(" - %d | %s | %s:%d%n",
                    i.get("id").asInt(), i.get("name").asText(), i.get("ip").asText(), i.get("port").asInt()
            ));
        }
    }
}
