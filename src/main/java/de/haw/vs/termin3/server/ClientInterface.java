package de.haw.vs.termin3.server;

import de.haw.vs.termin3.server.registry.RegistryEntry;

import java.net.Socket;

public class ClientInterface {
    private final Socket socket;
    private RegistryEntry entry;

    ClientInterface(Socket socket) {
        this.socket = socket;
        this.entry = null;
    }

    public Socket socket() {
        return socket;
    }

    public RegistryEntry entry() {
        return entry;
    }

    public void setEntry(RegistryEntry entry) {
        this.entry = entry;
    }
}
