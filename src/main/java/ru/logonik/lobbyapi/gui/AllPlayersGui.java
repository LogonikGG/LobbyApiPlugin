package ru.logonik.lobbyapi.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import ru.logonik.lobbyapi.api.LobbyPlayers;
import ru.logonik.lobbyapi.models.LobbyCommonAsksHandler;
import ru.logonik.lobbyapi.models.PlayerState;
import ru.logonik.lobbyapi.utils.ItemBuilder;
import ru.logonik.spigui.SpiGUI;
import ru.logonik.spigui.buttons.SGButton;
import ru.logonik.spigui.menu.SGMenu;

import java.util.ArrayList;
import java.util.List;

public class AllPlayersGui implements LogoGui {
    private SGMenu sgMenu;
    private final LobbyPlayers lobbyPlayers;

    public AllPlayersGui(SpiGUI spiGUI, LobbyPlayers lobbyPlayers) {
        this.sgMenu = spiGUI.create("§6§lЛ(ог)ичности", 5);
        this.sgMenu.setAutomaticPaginationEnabled(true);
        this.lobbyPlayers = lobbyPlayers;
    }

    @Override
    public void update(SGMenu sgMenu, Player player) {
        sgMenu.clearAllButStickiedSlots();
        List<PlayerState> playersStateList = lobbyPlayers.createPlayersStateList();
        for (PlayerState state : playersStateList) {
            if(state.player() == null) return;
            ItemBuilder itemBuilder = new ItemBuilder(Material.PLAYER_HEAD).setName(state.player().getName());
            LobbyCommonAsksHandler info = state.lobbyCommonAsksHandler();
            ArrayList<String> lore = new ArrayList<>();
            lore.add(info.getCommonGameName());
            if(info.getMapLobbyGameName() != null) {
                lore.add(info.getMapLobbyGameName());
            }
            if(info.getDescriptionOfCurrentState() != null) {
                lore.add(info.getDescriptionOfCurrentState());
            }
            itemBuilder.setLore(lore);
            sgMenu.addButton(new SGButton(itemBuilder.toItemStack()));
        }
    }

    public void open(Player player) {
        update(sgMenu, player);
        Inventory inventory = sgMenu.getInventory();
        player.openInventory(inventory);
    }
}
