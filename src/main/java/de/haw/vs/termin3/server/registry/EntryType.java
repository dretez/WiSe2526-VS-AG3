package de.haw.vs.termin3.server.registry;

public enum EntryType {
    CLIENT,
    ROBOT;
    
    public static EntryType fromString(String string) {
        return switch (string.toLowerCase()) {
            case "client" -> CLIENT;
            case "robot" -> ROBOT;
            default -> throw new IllegalArgumentException("Unexpected value: " + string);
        };
    }
}
