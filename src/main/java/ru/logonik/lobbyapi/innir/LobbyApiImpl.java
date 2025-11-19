package ru.logonik.lobbyapi.innir;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import ru.logonik.lobbyapi.Logger;
import ru.logonik.lobbyapi.api.*;
import ru.logonik.lobbyapi.models.PlayerState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LobbyApiImpl implements LobbyApi, Listener {
    private final HashMap<String, PluginInfo> plugins = new HashMap<>();
    private final LobbyPlayersImpl lobbyPlayers;

    public LobbyApiImpl(LobbyPlayersImpl lobbyPlayers) {
        this.lobbyPlayers = lobbyPlayers;
    }

    @Override
    public LobbyPlayers registerPlugin(PluginInfo plugin) throws LobbyApiException {
        String pluginKey = getPluginKey(plugin);
        if(plugins.containsKey(pluginKey)) throw new LobbyApiException("Plugin already registered");
        plugins.put(pluginKey, plugin);
        Logger.info(pluginKey + " is registered");
        return new LobbyPlayersMediator(lobbyPlayers, plugin);
    }

    @Override
    public void unregisterPlugin(Plugin plugin) {
        String pluginKey = getPluginKey(plugin);
        PluginInfo remove = plugins.remove(pluginKey);
        if(remove != null) {
            Logger.info(pluginKey + " is unregistered");
            List<PlayerState> playersStateList = lobbyPlayers.createPlayersStateList();

            for (PlayerState playerState : playersStateList) {
                if (playerState.leavedGameSession() != null && getPluginKey(playerState).equals(pluginKey)) {
                    playerState.setLeavedGameSession(null, null);
                }
            }

            if(remove.shouldAutoReturnToLobbyOnPluginDisable()) {
                for (PlayerState playerState : playersStateList) {
                    if(playerState.isInLobby() || playerState.player() == null) continue;
                    lobbyPlayers.returnToLobby(playerState.player());
                }
            }
        }
    }

    public String getPluginKey(PlayerState playerState) {
        return getPluginKey(playerState.pluginInfo().plugin());
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
