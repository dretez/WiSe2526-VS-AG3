package de.haw.vs.termin3.robot;

import de.haw.vs.termin3.common.network.CommunicationInterface;
import de.haw.vs.termin3.robot.request.RequestHandler;
import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class ClientHandler extends Thread {
    private final List<Socket> allClients;
    private final Socket client;
    private final ICaDSRoboticArm arm;

    public ClientHandler(List<Socket> allClients, Socket client, ICaDSRoboticArm arm) {
        this.allClients = allClients;
        this.client = client;
        this.arm = arm;
    }

    @Override
    public void run() {
        while (true) {
            try {
                handleNextRequest();
            } catch (IOException _) {
                allClients.remove(client);
                return;
            }
        }
    }

    private void handleNextRequest() throws IOException {
        String request = CommunicationInterface.awaitReply(client);
        if (request == null) throw new IOException("Connection closed");
        RequestHandler.handle(request, client, arm);
    }
}
