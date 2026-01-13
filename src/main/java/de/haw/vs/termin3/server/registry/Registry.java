package de.haw.vs.termin3.server.registry;

import java.util.*;

public class Registry {
    private final Map<String, RegistryEntry> registry;
    private final Map<String, Object> mutexes;

    public Registry() {
        registry = new HashMap<>();
        mutexes = Collections.synchronizedMap(new WeakHashMap<>());
        mutexes.put("write", new Object());
        mutexes.put("read", new Object());
    }

    public void register(String name, String ip, int port, EntryType type) throws RegistryException {
        synchronized (mutexes.get("write")) {
            if (name == null || name.isBlank()) throw new RegistryException("INVALID_NAME", "name must not be empty");
            if(registry.containsKey(name))throw new RegistryException("DUPLICATED_NAME", "name is already registered");
            if (ip == null || ip.isBlank()) throw new RegistryException("INVALID_IP", "ip must not be empty");
            if (port < 1 || port > 65535) throw new RegistryException("INVALID_PORT", "port must be 1..65535");
            if (type == null) throw new RegistryException("INVALID_TYPE", "type must be 'client' or 'robot'");

            synchronized (mutexes.get("read")) {
                RegistryEntry registry = new RegistryEntry(name, ip, port, type);
                this.registry.put(name, registry);
            }
        }
    }

    public void unregister(String name) throws RegistryException {
        synchronized (mutexes.get("write")) {
            if (name == null || name.isBlank()) throw new RegistryException("INVALID_NAME", "name must not be empty");
            if (!registry.containsKey(name)) throw new RegistryException("INVALID_NAME", "name is not registered");
            synchronized (mutexes.get("read")) {
                registry.remove(name);
            }
        }
    }

    public List<RegistryEntry> list(EntryType type) {
        synchronized (mutexes.get("read")) {
            if (type == null)
                return list();
            return registry.values().stream().filter(entry -> entry.getType() == type).toList();
        }
    }

    private List<RegistryEntry> list() {
        return registry.values().stream().toList();
    }
}
