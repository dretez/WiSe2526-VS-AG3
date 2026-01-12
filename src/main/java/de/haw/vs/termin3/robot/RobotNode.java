package de.haw.vs.termin3.robot;

import de.haw.vs.termin3.common.json.JSONBuilder;
import de.haw.vs.termin3.common.json.JSONReader;
import de.haw.vs.termin3.common.network.CommunicationInterface;
import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RobotNode {
    private final ICaDSRoboticArm arm;
    private final Socket registry;
    private final ServerSocket server;

    public RobotNode(String name, String registryIP, int registryPort, ICaDSRoboticArm arm) throws IOException {
        this.arm = arm;
        this.server = new ServerSocket();
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

    public void setLeftRightPercentageTo(int i) {
        arm.setLeftRightPercentageTo(i);
    }
}
