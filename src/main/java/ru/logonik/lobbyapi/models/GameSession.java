package ru.logonik.lobbyapi.models;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface GameSession {
    String getCommonGameName();
    String getMapLobbyGameName();
    String getDescriptionOfCurrentState();

    boolean canBeForceLeft(UUID player);
    void forceLeave(UUID player);
    void tryRejoin(Player player);
}
