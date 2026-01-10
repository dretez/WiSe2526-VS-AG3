package de.haw.vs.termin3.server;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Registry {
    private final Map<String, Socket> registry;

    public Registry() {
        registry = new HashMap<>();
    }

    public void register(String name, String ip, int port) {
    }

    public void unregister(String name) {
    }

    public void list() {
    }
}
