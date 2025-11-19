package ru.logonik.lobbyapi.api;

import org.bukkit.plugin.Plugin;

public record PluginInfo(Plugin plugin, Runnable onLobbyPluginGoDisable) {
    public PluginInfo(Plugin plugin) {
        this(plugin, null);
    }

    public PluginInfo(Plugin plugin, Runnable onLobbyPluginGoDisable) {
        this.plugin = plugin;
        this.onLobbyPluginGoDisable = onLobbyPluginGoDisable;
    }
}
