package ru.logonik.lobbyapi;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import ru.logonik.lobbyapi.models.LobbyAsksGameHandler;
import ru.logonik.lobbyapi.models.LobbyAsksInfoHandler;
import ru.logonik.lobbyapi.models.LobbyCommonAsksHandler;
import ru.logonik.lobbyapi.models.PlayerState;

import java.util.List;

public interface LobbyPlayers {
    void returnToLobbyByPlayer(Player player);
    void returnToLobbyByAdmin(Player player);
    void returnToLobbyByGameEnd(Player player);
    void returnToLobbyByKick(Player player);
    void teleport(Player player);
    void registerInGame(Player player, LobbyAsksInfoHandler info, LobbyAsksGameHandler game);
    void registerInGame(Player player, LobbyCommonAsksHandler handler);
    void removeFromRejoin(Player player);
    void removeFromRejoin(Iterable<Player> players);
    boolean isFree(Player player);
    List<PlayerState> createPlayersStateList();
    Location getLobbyLocation();
}

