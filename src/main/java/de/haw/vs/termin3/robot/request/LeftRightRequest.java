package de.haw.vs.termin3.robot.request;

import de.haw.vs.termin3.common.json.JSONReader;
import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;

import java.net.Socket;

public final class LeftRightRequest extends RequestHandler {
    LeftRightRequest(ICaDSRoboticArm arm) {
        super(arm);
    }

    @Override
    protected void handle(JSONReader reader, Socket client) {
        int i = (int) reader.get("i");
        arm.setLeftRightPercentageTo(i);
    }
}
