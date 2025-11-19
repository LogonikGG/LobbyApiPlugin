package ru.logonik.lobbyapi;

import java.util.HashMap;
import java.util.Map;

public class ServiceLocator {
    private final Map<Class<?>, Object> services = new HashMap<>();

    public <T> void registerService(Class<T> serviceClass, T serviceInstance) {
        services.put(serviceClass, serviceInstance);
    }

    public <T> T getService(Class<T> serviceClass) {
        return serviceClass.cast(services.get(serviceClass));
    }

    public void onPluginEnable() {
        for (Object value : services.values()) {
            if (value instanceof PluginStartListener startListener) {
                try {
                    startListener.start();
                } catch (Exception e) {
                    Logger.error("Error while start", e);
                }
            }
        }
    }

    public void onPluginDisable() {
        for (Object value : services.values()) {
            if (value instanceof PluginDisableListener disableListener) {
                try {
                    disableListener.disable();
                } catch (Exception e) {
                    Logger.error("Error while disable", e);
                }
            }
        }
    }
}
