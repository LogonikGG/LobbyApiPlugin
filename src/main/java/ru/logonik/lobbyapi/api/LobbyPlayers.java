package ru.logonik.lobbyapi.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ru.logonik.lobbyapi.models.LobbyAsksGameHandler;
import ru.logonik.lobbyapi.models.LobbyAsksInfoHandler;
import ru.logonik.lobbyapi.models.LobbyCommonAsksHandler;
import ru.logonik.lobbyapi.models.PlayerState;

import java.util.List;
import java.util.UUID;

public interface LobbyPlayers {
    void returnToLobby(Player player);
    void returnToLobbyByGameEnd(Player player);
    void teleport(Player player);
    void registerInGame(Player player, LobbyAsksInfoHandler info, LobbyAsksGameHandler game);
    void registerInGame(Player player, LobbyCommonAsksHandler handler);
    void removeFromRejoin(UUID player);
    void removeFromRejoin(Iterable<UUID> players);
    boolean isFree(Player player);
    List<PlayerState> createPlayersStateList();
    @Nullable Location getLobbyLocation();
}

