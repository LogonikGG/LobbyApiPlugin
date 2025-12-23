package ru.logonik.lobbyapi.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Dependency;
import org.bukkit.entity.Player;
import ru.logonik.lobbyapi.api.InnerLobbyPlayers;

// todo: set spawn positions

@CommandAlias("spawn")
@CommandPermission("command.spawn")
public class LobbyCommand extends BaseCommand {

    @Dependency
    private InnerLobbyPlayers lobbyPlayersApi;


    @Default
    public void spawn(Player player) {
        lobbyPlayersApi.returnToLobby(player);
    }
}
