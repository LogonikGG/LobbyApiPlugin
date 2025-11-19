package ru.logonik.lobbyapi.models;

import org.bukkit.entity.Player;

public class GameSesssionHandlerImpl implements GameSession {
    @Override
    public void onStartReturnToLobby(Player player) {

    }

    @Override
    public void onEndReturnToLobby(Player player) {

    }

    @Override
    public boolean tryRejoin(Player player) {
        return false;
    }

    @Override
    public String getCommonGameName() {
        return "Лобби";
    }

    @Override
    public String getMapLobbyGameName() {
        return null;
    }

    @Override
    public String getDescriptionOfCurrentState() {
        return null;
    }
}
