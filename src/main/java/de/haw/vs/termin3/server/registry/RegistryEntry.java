package de.haw.vs.termin3.server.registry;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.concurrent.atomic.AtomicInteger;

public final class RegistryEntry {
    private static final AtomicInteger NEXT_ID = new AtomicInteger(0);

    @JsonProperty
    private final int id;
    @JsonProperty
    private final String name;
    @JsonProperty
    private final String ip;
    @JsonProperty
    private final int port;
    @JsonProperty
    private final EntryType type;

    public RegistryEntry(String name, String ip, int port, EntryType type) {
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
    public EntryType getType() { return type; }
}