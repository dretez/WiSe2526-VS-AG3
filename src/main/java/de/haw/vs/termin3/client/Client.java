package de.haw.vs.termin3.client;

import de.haw.vs.termin3.common.network.CommunicationInterface;
import de.haw.vs.termin3.common.network.Port;

import java.io.IOException;
import java.net.Socket;

public class Client {
    private static final String DEFAULT_HOST = "127.0.0.1";

    private final Socket clientSocket;
    private int port;
    private String ip;

    public Client(String host, int port) throws IOException {
        this.ip = host;
        this.port = port;
        this.clientSocket = new Socket(host, port);
    }
    public Client(String host) throws IOException {
        this(host, Port.DEFAULT.port());
        this.port=Port.DEFAULT.port();
        this.ip = host;
    }
    public Client() throws IOException {
        this(DEFAULT_HOST);
        this.ip = DEFAULT_HOST;
    }

    public void stop() throws IOException {
        clientSocket.close();
    }

    public void regist(String name,String type) {
        String request =
                "{"
                        + "\"request\":\"regist\","
                        + "\"name\":\"" + name + "\","
                        + "\"ip\":\"" + ip + "\","
                        + "\"port\":" + 6000 + ","
                        + "\"type\":\"" + type + "\""
                        + "}";
        try {
            CommunicationInterface.sendRequest(clientSocket, request);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void unregist(String name) {
        String request =
                "{"
                        + "\"request\":\"unregist\","
                        + "\"name\":\"" + name + "\""
                        + "}";
        try {
            CommunicationInterface.sendRequest(clientSocket, request);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void list(String type) {
        String request =
                "{"
                        + "\"request\":\"list\","
                        + "\"type\":\"" + type + "\""
                        + "}";
        try {
            CommunicationInterface.sendRequest(clientSocket, request);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
