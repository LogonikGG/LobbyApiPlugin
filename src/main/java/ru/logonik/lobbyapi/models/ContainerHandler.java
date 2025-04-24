package ru.logonik.lobbyapi.models;

import org.bukkit.entity.Player;

public class ContainerHandler implements LobbyCommonAsksHandler {

    private final LobbyAsksInfoHandler info;
    private final LobbyAsksGameHandler game;

    public ContainerHandler(LobbyAsksInfoHandler info, LobbyAsksGameHandler game) {
        this.info = info;
        this.game = game;
    }

    @Override
    public void onLeave(Player player) {
        game.onLeave(player);
    }

    @Override
    public boolean tryRejoin(Player player) {
        return game.tryRejoin(player);
    }

    @Override
    public String getCommonGameName() {
        return info.getCommonGameName();
    }

    @Override
    public String getMapLobbyGameName() {
        return info.getMapLobbyGameName();
    }

    @Override
    public String getDescriptionOfCurrentState() {
        return info.getDescriptionOfCurrentState();
    }
}
