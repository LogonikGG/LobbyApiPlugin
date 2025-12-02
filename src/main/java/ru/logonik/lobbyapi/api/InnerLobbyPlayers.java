package ru.logonik.lobbyapi.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ru.logonik.lobbyapi.models.GameSession;
import ru.logonik.lobbyapi.models.PlayerState;

import java.util.List;
import java.util.UUID;

public interface InnerLobbyPlayers {
    void returnToLobby(Player player);
    void returnToLobbyByGameEnd(PluginInfo pluginInfo, Player player);
    void teleport(Player player);
    void registerInGame(PluginInfo pluginInfo, Player player, GameSession handler);
    void removeFromRejoin(PluginInfo pluginInfo, UUID player);
    void removeFromRejoin(PluginInfo pluginInfo, Iterable<UUID> players);
    boolean isFree(PluginInfo pluginInfo, Player player);
    List<PlayerState> createPlayersStateList();
    @Nullable Location getLobbyLocation();
}

