package ru.logonik.lobbyapi;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbyCommand implements CommandExecutor {
    private final LobbyPlayersApi lobbyPlayersApi;

    public LobbyCommand(LobbyPlayersApi lobbyPlayersApi) {
        this.lobbyPlayersApi = lobbyPlayersApi;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Command only for players");
            return true;
        }
        Player player = (Player) sender;
        lobbyPlayersApi.onLobbyCommand(player);
        return true;
    }
}
