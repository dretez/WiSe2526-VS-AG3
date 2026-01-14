package de.haw.vs.termin3.robot;

import de.haw.vs.termin3.common.network.Port;
import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;
import org.cads.vs.roboticArm.hal.real.CaDSRoboticArmReal;

import java.util.List;

public class MainReal {
    private static final String defaultName = "robot-real";

    public static void main(String[] args) {
        List<String> argv = List.of(args);
        String name = argv.isEmpty() ? defaultName : argv.getFirst();
        try {
            ICaDSRoboticArm sim = new CaDSRoboticArmReal("127.0.0.1", 50051);
            new RobotNode(name, "127.0.0.1", Port.DEFAULT.port(), sim).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
