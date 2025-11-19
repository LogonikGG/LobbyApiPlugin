package ru.logonik.lobbyapi.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ru.logonik.lobbyapi.models.GameSessionGameHandler;
import ru.logonik.lobbyapi.models.GameSessionInfoHandler;
import ru.logonik.lobbyapi.models.GameSession;
import ru.logonik.lobbyapi.models.PlayerState;

import java.util.List;
import java.util.UUID;

public interface LobbyPlayers {
    void returnToLobby(Player player);
    void returnToLobbyByGameEnd(Player player);
    void teleport(Player player);
    void registerInGame(Player player, GameSessionInfoHandler info, GameSessionGameHandler game);
    void registerInGame(Player player, GameSession handler);
    void removeFromRejoin(UUID player);
    void removeFromRejoin(Iterable<UUID> players);
    boolean isFree(Player player);
    List<PlayerState> createPlayersStateList();
    @Nullable Location getLobbyLocation();
}

