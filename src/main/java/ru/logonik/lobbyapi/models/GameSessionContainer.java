package ru.logonik.lobbyapi.models;

import org.bukkit.entity.Player;

public class GameSessionContainer implements GameSession {

    private final GameSessionInfoHandler info;
    private final GameSessionGameHandler game;

    public GameSessionContainer(GameSessionInfoHandler info, GameSessionGameHandler game) {
        this.info = info;
        this.game = game;
    }

    @Override
    public void onStartReturnToLobby(Player player) {
        game.onStartReturnToLobby(player);
    }

    @Override
    public void onEndReturnToLobby(Player player) {
        game.onEndReturnToLobby(player);
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
