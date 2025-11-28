package ru.logonik.lobbyapi.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import ru.logonik.lobbyapi.api.InnerLobbyPlayers;
import ru.logonik.lobbyapi.models.GameSession;
import ru.logonik.lobbyapi.models.PlayerState;
import ru.logonik.lobbyapi.utils.ItemBuilder;
import ru.logonik.spigui.SpiGUI;
import ru.logonik.spigui.buttons.SGButton;
import ru.logonik.spigui.menu.SGMenu;

import java.util.ArrayList;
import java.util.List;

public class AllPlayersGui implements LogoGui {
    private SGMenu sgMenu;
    private final InnerLobbyPlayers lobbyPlayers;

    public AllPlayersGui(SpiGUI spiGUI, InnerLobbyPlayers lobbyPlayers) {
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
            ArrayList<String> lore = getInfoList(state);
            itemBuilder.setLore(lore);
            sgMenu.addButton(new SGButton(itemBuilder.toItemStack()));
        }
    }

    private static @NotNull ArrayList<String> getInfoList(PlayerState state) {
        GameSession info = state.gameSession();
        ArrayList<String> lore = new ArrayList<>();
        if(info != null) {
            lore.add(info.getCommonGameName());
            if(info.getMapLobbyGameName() != null) {
                lore.add(info.getMapLobbyGameName());
            }
            if(info.getDescriptionOfCurrentState() != null) {
                lore.add(info.getDescriptionOfCurrentState());
            }
        } else {
            lore.add("Не в игре");
        }
        return lore;
    }

    public void open(Player player) {
        update(sgMenu, player);
        Inventory inventory = sgMenu.getInventory();
        player.openInventory(inventory);
    }
}
