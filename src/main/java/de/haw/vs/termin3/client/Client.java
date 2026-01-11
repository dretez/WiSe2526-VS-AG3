package de.haw.vs.termin3.client;

import de.haw.vs.termin3.common.json.JSONBuilder;
import de.haw.vs.termin3.common.json.JSONReader;
import de.haw.vs.termin3.common.network.CommunicationInterface;
import de.haw.vs.termin3.common.network.Port;

import java.io.IOException;
import java.net.Socket;

public class Client {
    private static final String DEFAULT_HOST = "127.0.0.1";

    private final Socket clientSocket;
    private final int port;
    private final String ip;

    public Client(String host, int port) throws IOException {
        this.clientSocket = new Socket(host, port);
        this.ip = host;
        this.port = port;
    }
    public Client(String host) throws IOException {
        this(host, Port.DEFAULT.port());
    }
    public Client() throws IOException {
        this(DEFAULT_HOST);
    }

    public void stop() throws IOException {
        clientSocket.close();
    }

    public void register(String name, String type) {
        JSONBuilder builder = new JSONBuilder();
        builder.putString("request", "register");
        builder.putString("name", name);
        builder.putString("type", type);
        builder.putString("ip", ip);
        builder.putNumber("port", port);
        try {
            String response = CommunicationInterface.sendAndAwait(clientSocket, builder.toString());

            JSONReader reader = new JSONReader(response);
            if ("error".equals(String.valueOf(reader.get("status"))))
                throw new RuntimeException("Registration failed: " + reader.get("message"));
            System.out.println("Registration successful");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void unregister(String name) {
        JSONBuilder builder = new JSONBuilder();
        builder.putString("request", "unregister");
        builder.putString("name", name);
        try {
            String response = CommunicationInterface.sendAndAwait(clientSocket, builder.toString()); // receive

            JSONReader reader = new JSONReader(response);
            if ("error".equals(String.valueOf(reader.get("status")))) {
                System.out.println("Unregistration failed: " + reader.get("message"));
            } else {
                System.out.println("Unregistration successful");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void list(String type) {
        JSONBuilder builder = new JSONBuilder();
        builder.putString("request", "unregister");
        builder.putString("type", type);
        try {
            CommunicationInterface.sendRequest(clientSocket, builder.toString());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
