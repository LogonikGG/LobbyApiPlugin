package ru.logonik.lobbyapi.api;

import ru.logonik.lobbyapi.models.GameSession;

import java.util.UUID;

public interface LobbyPlayers {
    boolean isFree(UUID player);
    void leaveIfAllowedForAnotherSession(UUID player, GameSession requereGameSession);
    boolean forbiddenTransfer(UUID player, GameSession handler);
    void processJoin(UUID player, GameSession handler);
    void processLeave(UUID player, GameSession handler);
    void removeFromRejoin(UUID player);
    void removeFromRejoin(Iterable<UUID> players);
}

