package de.haw.vs.termin3.robot.request;

import com.fasterxml.jackson.databind.JsonNode;
import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;

import java.net.Socket;

public final class LeftRightRequest extends RequestHandler {
    LeftRightRequest(ICaDSRoboticArm arm) {
        super(arm);
    }

    @Override
    protected void handle(JsonNode json, Socket client) {
        int i = json.get("i").asInt();
        arm.setLeftRightPercentageTo(i);
    }
}
