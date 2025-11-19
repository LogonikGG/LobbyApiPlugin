package ru.logonik.lobbyapi.models;

import org.bukkit.entity.Player;

/**
 * Presents methods for rejoin and notice if player write /lobby
 *
 */
public interface GameSessionGameHandler {
    void onStartReturnToLobby(Player player);
    void onEndReturnToLobby(Player player);
    boolean tryRejoin(Player player);
}
