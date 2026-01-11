package de.haw.vs.termin3.server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Registry {
    public final Map<String, RegistryEntry> registry;

    public Registry() {
        registry = new HashMap<>();
    }

    public void register(String name, String ip, int port,String type) throws RegistryException {
        if (name == null || name.isBlank()) throw new RegistryException("INVALID_NAME", "name must not be empty");
        if(registry.containsKey(name))throw new RegistryException("DUPLICATED_NAME", "name is already registered");
        if (ip == null || ip.isBlank()) throw new RegistryException("INVALID_IP", "ip must not be empty");
        if (port < 1 || port > 65535) throw new RegistryException("INVALID_PORT", "port must be 1..65535");
        if (type == null || type.isBlank()) throw new RegistryException("INVALID_TYPE", "type must be 'client' or 'robot'");

        RegistryEntry regist = new RegistryEntry(name,ip,port,type);
        registry.put(name, regist);

    }

    public void unregister(String name) throws RegistryException {
        if (name == null || name.isBlank()) throw new RegistryException("INVALID_NAME", "name must not be empty");
        registry.remove(name);

    }

    public List<RegistryEntry> list(String type) throws RegistryException {

        if (type == null || type.isBlank()) {type = "all";}

        type = type.toLowerCase();

        if (!type.equals("all") && !type.equals("client") && !type.equals("robot")) {
            throw new RegistryException("INVALID_TYPE", "type must be 'client', 'robot', or 'all'.");
        }

        List<RegistryEntry> result = new ArrayList<>();
        for (RegistryEntry e : registry.values()) {
            if (type.equals("all") || e.getType().equalsIgnoreCase(type)) {
                result.add(e);
            }
        }

        return result;
    }
}
