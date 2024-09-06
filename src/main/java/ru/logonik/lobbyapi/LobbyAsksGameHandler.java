package ru.logonik.lobbyapi;

import org.bukkit.entity.Player;

/**
 * Presents methods for rejoin and notice if player write /lobby or leave the server
 *
 */
public interface LobbyAsksGameHandler {
    void onLeave(Player player, Reason reason);
    boolean tryRejoin(Player player);
}
