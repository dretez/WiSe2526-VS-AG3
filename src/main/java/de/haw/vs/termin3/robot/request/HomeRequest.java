package de.haw.vs.termin3.robot.request;

import de.haw.vs.termin3.common.json.JSONReader;
import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;

import java.net.Socket;

public final class HomeRequest extends RequestHandler {
    HomeRequest(ICaDSRoboticArm arm) {
        super(arm);
    }

    @Override
    protected void handle(JSONReader reader, Socket client) {
        arm.setLeftRightPercentageTo(0);
        arm.setUpDownPercentageTo(0);
        arm.setBackForthPercentageTo(0);
        arm.setOpenClosePercentageTo(0);
    }
}
