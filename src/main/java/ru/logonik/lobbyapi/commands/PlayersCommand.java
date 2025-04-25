package ru.logonik.lobbyapi.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Dependency;
import org.bukkit.entity.Player;
import ru.logonik.lobbyapi.LobbyPlayers;
import ru.logonik.lobbyapi.gui.AllPlayersGui;

@CommandAlias("players")
@CommandPermission("command.players")
public class PlayersCommand extends BaseCommand {

    @Dependency
    private AllPlayersGui allPlayersGui;


    @Default
    public void players(Player player) {
        allPlayersGui.open(player);
    }
}
