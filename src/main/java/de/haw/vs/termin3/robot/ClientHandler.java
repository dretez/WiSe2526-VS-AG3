package de.haw.vs.termin3.robot;

import de.haw.vs.termin3.common.network.CommunicationInterface;
import de.haw.vs.termin3.robot.request.RequestHandler;
import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;

import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {
    private final Socket client;
    private final ICaDSRoboticArm arm;

    public ClientHandler(Socket client, ICaDSRoboticArm arm) {
        this.client = client;
        this.arm = arm;
    }

    @Override
    public void run() {
        while (true) {
            try {
                handleNextRequest();
            } catch (IOException _) {
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
