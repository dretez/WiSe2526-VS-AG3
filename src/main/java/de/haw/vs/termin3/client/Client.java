package de.haw.vs.termin3.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.haw.vs.termin3.client.exceptions.LoginFailureException;
import de.haw.vs.termin3.client.exceptions.RobotUnavailableException;
import de.haw.vs.termin3.common.json.JSON;
import de.haw.vs.termin3.common.network.CommunicationInterface;
import de.haw.vs.termin3.common.network.Port;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Client {
    private static final String DEFAULT_HOST = "127.0.0.1";

    private final ServerSocket tokenServerSocket;
    private final Socket clientSocket;
    private final String name;
    private final Map<String, RobotInterface> robots;
    private Integer id = null;
    private boolean hasToken;
    private NeighborClientInfo successor = null;

    public Client(String name, String host, int port) throws IOException {
        this.tokenServerSocket = new ServerSocket(0);
        this.clientSocket = new Socket(host, port);
        this.name = name;
        this.robots = new HashMap<>();
        this.hasToken = false;
        new TokenServer(tokenServerSocket, this).start();
    }

    public Client(String name, String host) throws IOException {
        this(name, host, Port.DEFAULT.port());
    }

    public Client(String name) throws IOException {
        this(name, DEFAULT_HOST);
    }

    private void setId(int id) {
        if (this.id != null)
            return;
        this.id = id;
    }

    public String name() {
        return name;
    }

    public synchronized void giveToken() {
        hasToken = true;
    }

    public void stop() throws IOException {
        clientSocket.close();
        passToken();
        tokenServerSocket.close();
    }

    public void register() throws IOException, LoginFailureException {
        ObjectNode builder = JSON.getEmptyObject();
        builder.put("request", "register");
        builder.put("name", name);
        builder.put("type", "client");
        builder.put("ip", clientSocket.getLocalAddress().getHostAddress());
        builder.put("port", tokenServerSocket.getLocalPort());
        String response = CommunicationInterface.sendAndAwait(clientSocket, JSON.toString(builder));

        JsonNode json = JSON.parse(response);
        if ("error".equals(json.get("status").asText()))
            throw new LoginFailureException(json.get("message").asText());
        setId(json.get("id").asInt());
        hasToken = queryNeighbours().getFirst().get("id").asInt() == this.id;
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
        if (!validateToken()) return;
        ObjectNode builder = JSON.getEmptyObject();
        builder.put("request", "use");
        builder.put("name", name);
        String response = CommunicationInterface.sendAndAwait(clientSocket, JSON.toString(builder));
        JsonNode json = JSON.parse(response);
        if ("error".equals(json.path("status").asText()))
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
        passToken();
    }

    public void rotateRobotsLR(int i) throws IOException {
        if (!validateToken()) return;
        for (var robot : robots.entrySet())
            robot.getValue().leftRight(i);
    }

    public void resetRobotPositions() throws IOException {
        if (!validateToken()) return;
        for (var robot : robots.entrySet())
            robot.getValue().goHome();
    }

    private List<JsonNode> queryNeighbours() throws IOException {
        ObjectNode builder = JSON.getEmptyObject();
        builder.put("request", "list");
        builder.put("type", "client");
        String response = CommunicationInterface.sendAndAwait(clientSocket, JSON.toString(builder));

        JsonNode json = JSON.parse(response);
        if (!json.path("list").isArray())
            throw new IOException("Invalid list field");
        List<JsonNode> list = new ArrayList<>();
        json.get("list").forEach(list::add);
        list.sort(Comparator.comparingInt(i -> i.get("id").asInt()));
        int index = list.indexOf(list.stream().filter(i -> i.get("id").asInt() == this.id).toList().getFirst());
        if (list.size() > 1) {
            JsonNode successor = list.get((index + 1) % list.size());
            this.successor = new NeighborClientInfo(successor.get("id").asInt(), successor.get("ip").asText(), successor.get("port").asInt());
        } else {
            this.successor = null;
        }
        return list;
    }

    private void passToken() {
        if (!hasToken || successor == null) return;

        try (Socket s = new Socket(successor.ip(), successor.port())) {
            ObjectNode token = JSON.getEmptyObject();
            token.put("request", "token");
            CommunicationInterface.sendRequest(s, JSON.toString(token));
            hasToken = false;
            System.out.println("Passed token to client " + successor.id());
        } catch (IOException e) {
            try {
                queryNeighbours();
            } catch (IOException _) {
            }
        }
    }

    private boolean validateToken() {
        if (!hasToken)
            System.out.println("You do not hold the token.");
        return hasToken;
    }
}

