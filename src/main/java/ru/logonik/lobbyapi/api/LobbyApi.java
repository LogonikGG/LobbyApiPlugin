package ru.logonik.lobbyapi.api;

import org.bukkit.plugin.Plugin;

public interface LobbyApi {
    void registerPlugin(PluginInfo plugin) throws LobbyApiException;
    void unregisterPlugin(Plugin plugin);
}
