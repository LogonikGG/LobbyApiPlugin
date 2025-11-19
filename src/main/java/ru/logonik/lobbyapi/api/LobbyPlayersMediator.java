package ru.logonik.lobbyapi.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ru.logonik.lobbyapi.innir.LobbyApiImpl;
import ru.logonik.lobbyapi.innir.LobbyPlayersImpl;
import ru.logonik.lobbyapi.models.LobbyAsksGameHandler;
import ru.logonik.lobbyapi.models.LobbyAsksInfoHandler;
import ru.logonik.lobbyapi.models.LobbyCommonAsksHandler;
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

    public void registerInGame( Player player, LobbyAsksInfoHandler info, LobbyAsksGameHandler game) {
        lobbyPlayers.registerInGame(pluginInfo, player, info, game);
    }

    public void registerInGame( Player player, LobbyCommonAsksHandler handler) {
        lobbyPlayers.registerInGame(pluginInfo, player, handler);
    }

    public void removeFromRejoin( UUID player) {
        lobbyPlayers.removeFromRejoin(pluginInfo, player);
    }

    public void removeFromRejoin( Iterable<UUID> players) {
        lobbyPlayers.removeFromRejoin(pluginInfo, players);
    }

    public boolean isFree( Player player) {
        return lobbyPlayers.isFree(pluginInfo, player);
    }

    @Override
    public void returnToLobby(Player player) {
        lobbyPlayers.returnToLobby(player);
    }

    @Override
    public void returnToLobbyByGameEnd(Player player) {
        lobbyPlayers.returnToLobbyByGameEnd(pluginInfo, player);
    }

    @Override
    public void teleport(Player player) {
        lobbyPlayers.teleport(player);
    }

    @Override
    public List<PlayerState> createPlayersStateList() {
        return lobbyPlayers.createPlayersStateList();
    }

    @Override
    public @Nullable Location getLobbyLocation() {
        return lobbyPlayers.getLobbyLocation();
    }
}
