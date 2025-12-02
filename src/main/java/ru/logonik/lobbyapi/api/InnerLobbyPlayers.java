package ru.logonik.lobbyapi.api;

import org.bukkit.entity.Player;
import ru.logonik.lobbyapi.models.GameSession;
import ru.logonik.lobbyapi.models.PlayerState;

import java.util.List;
import java.util.UUID;

public interface InnerLobbyPlayers {
    boolean isFree(UUID player);
    boolean forbiddenTransfer(PluginInfo pluginInfo, UUID player, GameSession handler);
    void processJoin(PluginInfo pluginInfo, UUID player, GameSession handler);
    void processLeave(PluginInfo pluginInfo, UUID player, GameSession handler);
    void removeFromRejoin(PluginInfo pluginInfo, UUID player);
    void removeFromRejoin(PluginInfo pluginInfo, Iterable<UUID> players);

    List<PlayerState> createPlayersStateList();
    void returnToLobby(Player player);
}

