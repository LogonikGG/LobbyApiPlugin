package ru.logonik.lobbyapi.api;

import org.bukkit.plugin.Plugin;

import java.util.Objects;

public record PluginInfo(Plugin plugin, Runnable onLobbyPluginGoDisable, Boolean shouldAutoReturnToLobbyOnPluginDisable) {
    public PluginInfo(Plugin plugin) {
        this(plugin, null, true);
    }

    public PluginInfo(Plugin plugin, Runnable onLobbyPluginGoDisable, Boolean shouldAutoReturnToLobbyOnPluginDisable) {
        this.plugin = plugin;
        this.onLobbyPluginGoDisable = onLobbyPluginGoDisable;
        this.shouldAutoReturnToLobbyOnPluginDisable = shouldAutoReturnToLobbyOnPluginDisable;
    }

    public PluginInfo(Plugin plugin, Boolean shouldAutoReturnToLobbyOnPluginDisable) {
        this(plugin, null, shouldAutoReturnToLobbyOnPluginDisable);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        PluginInfo that = (PluginInfo) object;
        return Objects.equals(plugin.getName(), that.plugin.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(plugin.getName());
    }
}
