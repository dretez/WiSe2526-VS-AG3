package de.haw.vs.termin3.client.exceptions;

public class RobotUnavailableException extends Exception {
    public RobotUnavailableException(String message) {
        super(message);
    }

    public RobotUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
