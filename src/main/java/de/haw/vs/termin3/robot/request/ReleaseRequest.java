package de.haw.vs.termin3.robot.request;

import com.fasterxml.jackson.databind.JsonNode;
import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;

import java.net.Socket;

final class ReleaseRequest extends RequestHandler {
    ReleaseRequest(ICaDSRoboticArm arm) {
        super(arm);
    }

    @Override
    protected void handle(JsonNode json, Socket client) {

    }
}
