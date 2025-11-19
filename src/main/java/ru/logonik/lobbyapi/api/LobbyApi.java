package ru.logonik.lobbyapi.api;

import org.bukkit.plugin.Plugin;

public interface LobbyApi {
    LobbyPlayers registerPlugin(PluginInfo plugin) throws LobbyApiException;
    void unregisterPlugin(Plugin plugin);
}
