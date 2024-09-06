package ru.logonik.lobbyapi;

/**
 * Presents info of game that Player join
 *
 */
public interface LobbyAsksInfoHandler {
    String getCommonGameName();
    String getMapLobbyGameName();
    String getDescriptionOfCurrentState();
}
