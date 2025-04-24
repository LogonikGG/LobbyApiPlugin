package ru.logonik.lobbyapi.models;

import org.bukkit.entity.Player;

/**
 * Presents methods for rejoin and notice if player write /lobby or leave the server
 *
 */
public interface LobbyAsksGameHandler {
    void onLeave(Player player);
    boolean tryRejoin(Player player);
}
