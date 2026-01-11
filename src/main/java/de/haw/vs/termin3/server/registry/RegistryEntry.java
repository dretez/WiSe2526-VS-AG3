package de.haw.vs.termin3.server.registry;

import de.haw.vs.termin3.common.json.JSON;
import de.haw.vs.termin3.common.json.JSONBuilder;

import java.util.concurrent.atomic.AtomicInteger;

public final class RegistryEntry implements JSON {
    private static final AtomicInteger NEXT_ID = new AtomicInteger(0);

    private final int id;
    private final String name;
    private final String ip;
    private final int port;
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

    @Override
    public String toJSON() {
        JSONBuilder builder = new JSONBuilder();
        builder.putNumber("id", id);
        builder.putString("name", name);
        builder.putString("ip", ip);
        builder.putNumber("port", port);
        builder.putString("type", type.toString().toLowerCase());
        return builder.toString();
    }
}