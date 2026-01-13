package de.haw.vs.termin3.robot.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import de.haw.vs.termin3.common.json.JSON;
import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;

import java.net.Socket;

public abstract sealed class RequestHandler permits HomeRequest, LeftRightRequest {
    protected final ICaDSRoboticArm arm;

    protected RequestHandler(ICaDSRoboticArm arm) {
        this.arm = arm;
    }

    public static void handle(String request, Socket client, ICaDSRoboticArm arm) throws JsonProcessingException {
        JsonNode json = JSON.parse(request);
        RequestHandler handler = switch (json.get("request").asText()) {
            case "leftRight" -> new LeftRightRequest(arm);
            case "home" -> new HomeRequest(arm);
            default -> throw new IllegalStateException("Unexpected value: " + json.get("request").asText());
        };
        handler.handle(json, client);
    }

    protected abstract void handle(JsonNode json, Socket client);
}
