package de.haw.vs.termin3.robot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.haw.vs.termin3.common.json.JSON;
import de.haw.vs.termin3.common.network.CommunicationInterface;
import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RobotNode {
    private final ICaDSRoboticArm arm;
    private final Socket registry;
    private final ServerSocket server;
    private final List<Socket> clients;
    private final String name;

    public RobotNode(String name, String registryIP, int registryPort, ICaDSRoboticArm arm) throws IOException {
        this.name = name;
        this.arm = arm;
        this.server = new ServerSocket();
        this.clients = Collections.synchronizedList(new ArrayList<>());
        int port = this.server.getLocalPort();
        this.registry = new Socket(registryIP, registryPort);
        ObjectNode builder = JSON.getEmptyObject();
        builder.put("request", "register");
        builder.put("name", name);
        builder.put("type", "client");
        builder.put("ip", registry.getLocalAddress().getHostAddress());
        builder.put("port", port);
        JsonNode reader = JSON.parse(CommunicationInterface.sendAndAwait(this.registry, JSON.toString(builder)));
        if ("error".equals(reader.get("status").asText()))
            throw new IOException(reader.get("message").asText());
    }

    public void start() {
        while (true) {
            try {
                Socket client = server.accept();
                clients.add(client);
                new ClientHandler(clients, client, arm).start();
            } catch (IOException e) {
                stop();
                return;
            }
        }
    }

    private void stop() {
        synchronized (clients) {
            for (Socket socket : clients) {
                try {
                    socket.close();
                } catch (IOException _) {
                }
            }
        }
        ObjectNode builder = JSON.getEmptyObject();
        builder.put("request", "unregister");
        builder.put("name", name);
        try {
            CommunicationInterface.sendRequest(registry, JSON.toString(builder));
        } catch (IOException _) {
        }
        try {
            registry.close();
        } catch (IOException _) {
        }
    }
}
