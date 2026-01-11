package de.haw.vs.termin3.server.registry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Registry {
    public final Map<String, RegistryEntry> registry;

    public Registry() {
        registry = new HashMap<>();
    }

    public void register(String name, String ip, int port, EntryType type) throws RegistryException {
        if (name == null || name.isBlank()) throw new RegistryException("INVALID_NAME", "name must not be empty");
        if(registry.containsKey(name))throw new RegistryException("DUPLICATED_NAME", "name is already registered");
        if (ip == null || ip.isBlank()) throw new RegistryException("INVALID_IP", "ip must not be empty");
        if (port < 1 || port > 65535) throw new RegistryException("INVALID_PORT", "port must be 1..65535");
        if (type == null) throw new RegistryException("INVALID_TYPE", "type must be 'client' or 'robot'");

        RegistryEntry registry = new RegistryEntry(name,ip,port,type);
        this.registry.put(name, registry);
        System.out.println("hey");
    }

    public void unregister(String name) throws RegistryException {
        if (name == null || name.isBlank()) throw new RegistryException("INVALID_NAME", "name must not be empty");
        if(!registry.containsKey(name))throw new RegistryException("INVALID_NAME", "name is not registered");
        registry.remove(name);

    }

    public List<RegistryEntry> list(EntryType type) {
        if (type == null)
            return list();
        return registry.values().stream().filter(entry -> entry.getType() == type).toList();
    }

    public List<RegistryEntry> list() {
        return registry.values().stream().toList();
    }
}
