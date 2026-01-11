package de.haw.vs.termin3.server.registry;

public class RegistryException extends Exception  {
    private final String code;

    public RegistryException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() { return code; }
}
