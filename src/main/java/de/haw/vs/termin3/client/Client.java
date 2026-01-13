package de.haw.vs.termin3.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.haw.vs.termin3.common.json.JSON;
import de.haw.vs.termin3.common.network.CommunicationInterface;
import de.haw.vs.termin3.common.network.Port;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private static final String DEFAULT_HOST = "127.0.0.1";

    private final Socket clientSocket;
    private final int port;
    private final String ip;
    private final String name;

    public Client(String name, String host, int port) throws IOException {
        this.clientSocket = new Socket(host, port);
        this.ip = host;
        this.port = port;
        this.name = name;
    }

    public Client(String name, String host) throws IOException {
        this(name, host, Port.DEFAULT.port());
    }

    public Client(String name) throws IOException {
        this(name, DEFAULT_HOST);
    }

    public void stop() throws IOException {
        clientSocket.close();
    }

    public void register() throws IOException, LoginFailureException {
        ObjectNode builder = JSON.getEmptyObject();
        builder.put("request", "register");
        builder.put("name", name);
        builder.put("type", "client");
        builder.put("ip", ip);
        builder.put("port", port);
        String response = CommunicationInterface.sendAndAwait(clientSocket, JSON.toString(builder));

        JsonNode json = JSON.parse(response);
        if ("error".equals(json.get("status").asText()))
            throw new LoginFailureException(json.get("message").asText());
    }

    public void unregister() throws IOException {
        ObjectNode builder = JSON.getEmptyObject();
        builder.put("request", "unregister");
        builder.put("name", name);
        String response = CommunicationInterface.sendAndAwait(clientSocket, JSON.toString(builder));

        JsonNode json = JSON.parse(response);
        if ("error".equals(json.get("status").asText()))
            System.out.println("Unregistration failed: " + json.get("message").asText());
    }

    public List<JsonNode> list() throws IOException {
        ObjectNode builder = JSON.getEmptyObject();
        builder.put("request", "list");
        builder.put("type", "robot");
        String response = CommunicationInterface.sendAndAwait(clientSocket, JSON.toString(builder));

        JsonNode json = JSON.parse(response);
        if (!json.path("list").isArray())
            throw new IOException("Error reading list, raw json: " + response);
        List<JsonNode> list = new ArrayList<>();
        json.get("list").forEach(list::add);
        return list;
    }
}

