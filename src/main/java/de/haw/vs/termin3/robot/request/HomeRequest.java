package de.haw.vs.termin3.robot.request;

import com.fasterxml.jackson.databind.JsonNode;
import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;

import java.net.Socket;

public final class HomeRequest extends RequestHandler {
    HomeRequest(ICaDSRoboticArm arm) {
        super(arm);
    }

    @Override
    protected void handle(JsonNode json, Socket client) {
        arm.setLeftRightPercentageTo(50);
        arm.setUpDownPercentageTo(50);
        arm.setBackForthPercentageTo(50);
        arm.setOpenClosePercentageTo(50);
    }
}
