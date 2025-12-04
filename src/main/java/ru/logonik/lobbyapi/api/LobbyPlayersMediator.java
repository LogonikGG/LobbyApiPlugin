package ru.logonik.lobbyapi.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ru.logonik.lobbyapi.models.GameSession;
import ru.logonik.lobbyapi.models.PlayerState;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class LobbyPlayersMediator implements LobbyPlayers {

    private final InnerLobbyPlayers lobbyPlayers;
    private final PluginInfo pluginInfo;

    public LobbyPlayersMediator(InnerLobbyPlayers lobbyPlayers, PluginInfo pluginInfo) {
        this.lobbyPlayers = lobbyPlayers;
        this.pluginInfo = pluginInfo;
    }

    @Override
    public boolean isFree(UUID player) {
        return lobbyPlayers.isFree(player);
    }

    @Override
    public void leaveIfAllowedForAnotherSession(UUID player, GameSession requereGameSession) {
        lobbyPlayers.leaveIfAllowedForAnotherSession(pluginInfo, player, requereGameSession);
    }

    public boolean forbiddenTransfer(UUID player, GameSession handler) {
        return lobbyPlayers.forbiddenTransfer(pluginInfo, player, handler);
    }

    public void processJoin( UUID player, GameSession handler) {
        lobbyPlayers.processJoin(pluginInfo, player, handler);
    }

    public void processLeave(UUID player, GameSession handler) {
        lobbyPlayers.processLeave(pluginInfo, player, handler);
    }

    public void removeFromRejoin(UUID player) {
        lobbyPlayers.removeFromRejoin(pluginInfo, player);
    }

    public void removeFromRejoin(Iterable<UUID> players) {
        lobbyPlayers.removeFromRejoin(pluginInfo, players);
    }

    private void throwIfPluginDisabled() {
        if (!pluginInfo.plugin().isEnabled()) {
            throw new IllegalStateException("Plugin is not enabled");
        }
    }
}
