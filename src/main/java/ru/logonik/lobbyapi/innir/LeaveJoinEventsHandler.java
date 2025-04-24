package ru.logonik.lobbyapi.innir;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveJoinEventsHandler implements Listener {

    private LobbyPlayersImpl lobbyPlayersApi;

    public LeaveJoinEventsHandler(LobbyPlayersImpl lobbyPlayersApi) {

        this.lobbyPlayersApi = lobbyPlayersApi;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        lobbyPlayersApi.handleJoin(e.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        lobbyPlayersApi.handleQuit(e.getPlayer());
    }
}
