package ru.logonik.lobbyapi.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ru.logonik.lobbyapi.models.GameSessionGameHandler;
import ru.logonik.lobbyapi.models.GameSessionInfoHandler;
import ru.logonik.lobbyapi.models.GameSession;
import ru.logonik.lobbyapi.models.PlayerState;

import java.util.List;
import java.util.UUID;

public class LobbyPlayersMediator implements LobbyPlayers {

    private final InnerLobbyPlayers lobbyPlayers;
    private final PluginInfo pluginInfo;

    public LobbyPlayersMediator(InnerLobbyPlayers lobbyPlayers, PluginInfo pluginInfo) {
        this.lobbyPlayers = lobbyPlayers;
        this.pluginInfo = pluginInfo;
    }

    public void registerInGame(Player player, GameSessionInfoHandler info, GameSessionGameHandler game) {
        throwIfPluginDisabled();
        lobbyPlayers.registerInGame(pluginInfo, player, info, game);
    }

    public void registerInGame( Player player, GameSession handler) {
        throwIfPluginDisabled();
        lobbyPlayers.registerInGame(pluginInfo, player, handler);
    }

    public void removeFromRejoin(UUID player) {
        throwIfPluginDisabled();
        lobbyPlayers.removeFromRejoin(pluginInfo, player);
    }

    public void removeFromRejoin( Iterable<UUID> players) {
        throwIfPluginDisabled();
        lobbyPlayers.removeFromRejoin(pluginInfo, players);
    }

    public boolean isFree(Player player) {
        throwIfPluginDisabled();
        return lobbyPlayers.isFree(pluginInfo, player);
    }

    @Override
    public void returnToLobby(Player player) {
        throwIfPluginDisabled();
        lobbyPlayers.returnToLobby(player);
    }

    @Override
    public void returnToLobbyByGameEnd(Player player) {
        throwIfPluginDisabled();
        lobbyPlayers.returnToLobbyByGameEnd(pluginInfo, player);
    }

    @Override
    public void teleport(Player player) {
        throwIfPluginDisabled();
        lobbyPlayers.teleport(player);
    }

    @Override
    public List<PlayerState> createPlayersStateList() {
        throwIfPluginDisabled();
        return lobbyPlayers.createPlayersStateList();
    }

    @Override
    public @Nullable Location getLobbyLocation() {
        throwIfPluginDisabled();
        return lobbyPlayers.getLobbyLocation();
    }

    private void throwIfPluginDisabled() {
        if (!pluginInfo.plugin().isEnabled()) {
            throw new IllegalStateException("Plugin is not enabled");
        }
    }
}
