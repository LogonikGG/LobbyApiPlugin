package ru.logonik.lobbyapi.api;

import org.bukkit.plugin.Plugin;

import java.util.Objects;

public final class PluginInfo {
    private final Plugin plugin;
    private final Runnable onLobbyPluginGoDisable;
    private final boolean shouldAutoReturnToLobbyOnPluginDisable;

    public PluginInfo(Plugin plugin) {
        this(plugin, null, true);
    }

    public PluginInfo(Plugin plugin, Runnable onLobbyPluginGoDisable, boolean shouldAutoReturnToLobbyOnPluginDisable) {
        this.plugin = plugin;
        this.onLobbyPluginGoDisable = onLobbyPluginGoDisable;
        this.shouldAutoReturnToLobbyOnPluginDisable = shouldAutoReturnToLobbyOnPluginDisable;
    }

    public PluginInfo(Plugin plugin, boolean shouldAutoReturnToLobbyOnPluginDisable) {
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

    public Plugin plugin() {
        return plugin;
    }

    public Runnable onLobbyPluginGoDisable() {
        return onLobbyPluginGoDisable;
    }

    public Boolean shouldAutoReturnToLobbyOnPluginDisable() {
        return shouldAutoReturnToLobbyOnPluginDisable;
    }

    @Override
    public String toString() {
        return "PluginInfo[" +
                "plugin=" + plugin + ", " +
                "onLobbyPluginGoDisable=" + onLobbyPluginGoDisable + ", " +
                "shouldAutoReturnToLobbyOnPluginDisable=" + shouldAutoReturnToLobbyOnPluginDisable + ']';
    }

}
