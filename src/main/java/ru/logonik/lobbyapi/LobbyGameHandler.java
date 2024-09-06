package ru.logonik.lobbyapi;

import org.bukkit.entity.Player;

public class LobbyGameHandler implements LobbyCommonAsksHandler{

    protected LobbyAsksInfoHandler lobbyAsksInfoHandler;
    protected LobbyAsksGameHandler lobbyAsksGameHandler;

    public LobbyGameHandler(LobbyAsksInfoHandler lobbyAsksInfoHandler, LobbyAsksGameHandler lobbyAsksGameHandler) {
        this.lobbyAsksInfoHandler = lobbyAsksInfoHandler;
        this.lobbyAsksGameHandler = lobbyAsksGameHandler;
    }

    public void onLeave(Player player, Reason reason) {
        lobbyAsksGameHandler.onLeave(player, reason);
    }

    public boolean tryRejoin(Player player) {
        return lobbyAsksGameHandler.tryRejoin(player);
    }

    public String getCommonGameName() {
        return lobbyAsksInfoHandler.getCommonGameName();
    }

    public String getMapLobbyGameName() {
        return lobbyAsksInfoHandler.getMapLobbyGameName();
    }

    public String getDescriptionOfCurrentState() {
        return lobbyAsksInfoHandler.getDescriptionOfCurrentState();
    }
}
