package de.haw.vs.termin3.client;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.haw.vs.termin3.common.json.JSON;
import de.haw.vs.termin3.common.network.CommunicationInterface;

import java.io.IOException;
import java.net.Socket;

public class RobotInterface {
    private final Socket robot;
    private final String name;

    public RobotInterface(Socket robot, String name) {
        this.robot = robot;
        this.name = name;
    }

    public void leftRight(int i) throws IOException {
        ObjectNode builder = JSON.getEmptyObject();
        builder.put("request", "leftRight");
        builder.put("i", i);
        CommunicationInterface.sendRequest(robot, JSON.toString(builder));
    }

    public void goHome() throws IOException {
        ObjectNode builder = JSON.getEmptyObject();
        builder.put("request", "home");
        CommunicationInterface.sendRequest(robot, JSON.toString(builder));
    }

    public void release() throws IOException {
        ObjectNode builder = JSON.getEmptyObject();
        builder.put("request", "release");
        CommunicationInterface.sendRequest(robot, JSON.toString(builder));
    }
}
