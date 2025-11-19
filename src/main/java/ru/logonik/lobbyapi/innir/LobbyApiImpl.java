package ru.logonik.lobbyapi.innir;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import ru.logonik.lobbyapi.Logger;
import ru.logonik.lobbyapi.api.LobbyApi;
import ru.logonik.lobbyapi.api.LobbyApiException;
import ru.logonik.lobbyapi.api.PluginInfo;

import java.util.ArrayList;
import java.util.HashMap;

public class LobbyApiImpl implements LobbyApi, Listener {
    private final HashMap<String, PluginInfo> plugins = new HashMap<>();

    public LobbyApiImpl(LobbyPlayersImpl lobbyPlayers) {
        lobbyPlayers.setLobbyApiImlp(this);
    }

    @Override
    public void registerPlugin(PluginInfo plugin) throws LobbyApiException {
        String pluginKey = getPluginKey(plugin);
        if(plugins.containsKey(pluginKey)) throw new LobbyApiException("Plugin already registered");
        plugins.put(pluginKey, plugin);
        Logger.info(pluginKey + " is registered");
    }

    @Override
    public void unregisterPlugin(Plugin plugin) {
        String pluginKey = getPluginKey(plugin);
        plugins.remove(pluginKey);
        Logger.info(pluginKey + " is unregistered");
    }

    public String getPluginKey(PluginInfo plugin) {
        return getPluginKey(plugin.plugin());
    }

    public String getPluginKey(Plugin plugin) {
        return plugin.getName();
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent disableEvent) {
        unregisterPlugin(disableEvent.getPlugin());
    }

    public void onLobbyPluginDisable() {
        ArrayList<PluginInfo> pluginInfos = new ArrayList<>(plugins.values());
        plugins.clear();
        for (PluginInfo pluginInfo : pluginInfos) {
            if(pluginInfo.onLobbyPluginGoDisable() != null) {
                pluginInfo.onLobbyPluginGoDisable().run();
            }
        }
    }
}
