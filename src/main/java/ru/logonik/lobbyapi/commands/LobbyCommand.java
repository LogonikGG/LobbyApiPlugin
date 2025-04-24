package ru.logonik.lobbyapi.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Dependency;
import org.bukkit.entity.Player;
import ru.logonik.lobbyapi.LobbyPlayers;

@CommandAlias("lobby")
@CommandPermission("command.lobby")
public class LobbyCommand extends BaseCommand {

    @Dependency
    private LobbyPlayers lobbyPlayersApi;


    @Default
    public void lobby(Player player) {
        lobbyPlayersApi.returnToLobbyByPlayer(player);
    }
}
