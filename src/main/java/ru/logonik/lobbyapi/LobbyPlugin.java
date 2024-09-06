package ru.logonik.lobbyapi;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class LobbyPlugin extends JavaPlugin {

    private LobbyPlayersApi lobbyPlayersApi;

    @Override
    public void onEnable() {
        lobbyPlayersApi = new LobbyPlayersApi(this, Bukkit.getWorlds().get(0).getSpawnLocation());
        LeaveJoinEventsHandler leaveJoinEventsHandler = new LeaveJoinEventsHandler(lobbyPlayersApi);
        getServer().getPluginManager().registerEvents(leaveJoinEventsHandler, this);
        getCommand("lobby").setExecutor(new LobbyCommand(lobbyPlayersApi));
    }

    public LobbyPlayersApi getLobbyPlayersApi() {
        return lobbyPlayersApi;
    }
}
