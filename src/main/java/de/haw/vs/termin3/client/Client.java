package de.haw.vs.termin3.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.haw.vs.termin3.client.exceptions.LoginFailureException;
import de.haw.vs.termin3.client.exceptions.RobotUnavailableException;
import de.haw.vs.termin3.common.json.JSON;
import de.haw.vs.termin3.common.network.CommunicationInterface;
import de.haw.vs.termin3.common.network.Port;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class Client {
    private static final String DEFAULT_HOST = "127.0.0.1";

    private final Socket clientSocket;
    private final int port;
    private final String ip;
    private final String name;
    private final Map<String, RobotInterface> robots;

    public Client(String name, String host, int port) throws IOException {
        this.clientSocket = new Socket(host, port);
        this.ip = host;
        this.port = port;
        this.name = name;
        this.robots = new HashMap<>();
    }

    public Client(String name, String host) throws IOException {
        this(name, host, Port.DEFAULT.port());
    }

    public Client(String name) throws IOException {
        this(name, DEFAULT_HOST);
    }

    public String name() {
        return name;
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

    public List<String> robots() {
        return robots.keySet().stream().toList();
    }

    public void useRobot(String name) throws IOException, RobotUnavailableException {
        ObjectNode builder = JSON.getEmptyObject();
        builder.put("request", "use");
        builder.put("name", name);
        String response = CommunicationInterface.sendAndAwait(clientSocket, JSON.toString(builder));
        JsonNode json = JSON.parse(response);
        if ("error".equals(json.get("status").asText()))
            throw new RobotUnavailableException("Could not connect to robot \"" + name + "\": " + json.get("message").asText());
        try {
            Socket socket = new Socket(json.get("ip").asText(), json.get("port").asInt());
            robots.put(name, new RobotInterface(socket, name));
        } catch (IOException e) {
            throw new RobotUnavailableException("Could not connect to robot \"" + name + "\": ", e);
        }
    }

    public void releaseRobot(String name) throws IOException, RobotUnavailableException {
        if (!robots.containsKey(name))
            throw new RobotUnavailableException("The robot \"" + name + "\" is not selected");
        robots.get(name).release();
        robots.remove(name);
    }

    public void rotateRobotsLR(int i) throws IOException {
        for (var robot : robots.entrySet())
            robot.getValue().leftRight(i);
    }

    public void resetRobotPositions() throws IOException {
        for (var robot : robots.entrySet())
            robot.getValue().goHome();
    }
}

