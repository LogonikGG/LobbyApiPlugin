package ru.logonik.lobbyapi;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveJoinEventsHandler implements Listener {

    private LobbyPlayersApi lobbyPlayersApi;

    public LeaveJoinEventsHandler(LobbyPlayersApi lobbyPlayersApi) {

        this.lobbyPlayersApi = lobbyPlayersApi;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        lobbyPlayersApi.onJoin(e.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        lobbyPlayersApi.onQuit(e.getPlayer());
    }
}
