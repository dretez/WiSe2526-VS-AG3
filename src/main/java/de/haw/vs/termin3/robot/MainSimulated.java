package de.haw.vs.termin3.robot;

import de.haw.vs.termin3.common.network.Port;
import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;
import org.cads.vs.roboticArm.hal.simulation.CaDSRoboticArmSimulation;

import java.util.List;

public class MainSimulated {
    private static final String defaultName = "robot-simulated";

    public static void main(String[] args) {
        List<String> argv = List.of(args);
        String name = argv.isEmpty() ? defaultName : argv.getFirst();
        try {
            ICaDSRoboticArm sim = new CaDSRoboticArmSimulation();
            new RobotNode(name, "127.0.0.1", Port.DEFAULT.port(), sim).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
