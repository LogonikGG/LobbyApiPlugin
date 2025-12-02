package ru.logonik.lobbyapi.models;

import org.bukkit.entity.Player;

public interface GameSession {
    String getCommonGameName();
    String getMapLobbyGameName();
    String getDescriptionOfCurrentState();

    void onStartReturnToLobby(Player player);
    void onEndReturnToLobby(Player player);
    boolean tryRejoin(Player player);
}
