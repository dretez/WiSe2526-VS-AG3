package de.haw.vs.termin3.robot;

import de.haw.vs.termin3.common.json.JSONBuilder;
import de.haw.vs.termin3.common.json.JSONReader;
import de.haw.vs.termin3.common.network.CommunicationInterface;
import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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
        this.clients = new ArrayList<>();
        int port = this.server.getLocalPort();
        this.registry = new Socket(registryIP, registryPort);
        JSONBuilder builder = new JSONBuilder();
        builder.putString("request", "register");
        builder.putString("name", name);
        builder.putString("type", "client");
        builder.putString("ip", registry.getLocalAddress().getHostAddress());
        builder.putNumber("port", port);
        JSONReader reader = new JSONReader(CommunicationInterface.sendAndAwait(this.registry, builder.toString()));
        if ("error".equals(reader.get("status")))
            throw new IOException(reader.get("message").toString());
    }

    public void start() {
        while (true) {
            try {
                Socket client = server.accept();
                clients.add(client);
                new ClientHandler(client, arm).start();
            } catch (IOException e) {
                stop();
                return;
            }
        }
    }

    private void stop() {
        JSONBuilder builder = new JSONBuilder();
        builder.putString("request", "unregister");
        builder.putString("name", name);
        try {
            CommunicationInterface.sendRequest(registry, builder.toString());
        } catch (IOException _) {
        }
    }
}
