package de.haw.vs.termin3.robot.request;

import de.haw.vs.termin3.common.json.JSONReader;
import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;

import java.net.Socket;

public abstract sealed class RequestHandler permits HomeRequest, LeftRightRequest {
    protected final ICaDSRoboticArm arm;

    protected RequestHandler(ICaDSRoboticArm arm) {
        this.arm = arm;
    }

    public static void handle(String request, Socket client, ICaDSRoboticArm arm) {
        JSONReader reader = new JSONReader(request);
        RequestHandler handler = switch ((String) reader.get("request")) {
            case "leftRight" -> new LeftRightRequest(arm);
            case "home" -> new HomeRequest(arm);
            default -> throw new IllegalStateException("Unexpected value: " + reader.get("request"));
        };
        handler.handle(reader, client);
    }

    protected abstract void handle(JSONReader reader, Socket client);
}
