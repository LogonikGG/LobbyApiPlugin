package ru.logonik.lobbyapi.models;

import org.bukkit.entity.Player;

public class LobbyAsksHandlerImpl implements LobbyCommonAsksHandler {
    @Override
    public void onLeave(Player player) {

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
