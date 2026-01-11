package de.haw.vs.termin3.server;
import java.util.concurrent.atomic.AtomicInteger;

public final class RegistryEntry {
    private static final AtomicInteger NEXT_ID = new AtomicInteger(0);

    private final int id;
    private final String name;
    private final String ip;
    private final int port;
    private final String type;

    public RegistryEntry(String name, String ip, int port, String type) {
        this.id= NEXT_ID.incrementAndGet();
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.type = type;
    }

    public Integer getId() { return id; }
    public String getName() { return name; }
    public String getIp() { return ip; }
    public int getPort() { return port; }
    public String getType() { return type; }
}