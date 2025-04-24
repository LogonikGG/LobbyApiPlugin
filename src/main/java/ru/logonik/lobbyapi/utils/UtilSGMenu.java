package ru.logonik.lobbyapi.utils;

import org.bukkit.Material;
import ru.logonik.spigui.buttons.SGButton;
import ru.logonik.spigui.menu.SGMenu;

import java.util.List;

public class UtilSGMenu {
    public static SGButton button(Material material, String name) {
        return new SGButton(new ItemBuilder(material).setName(name == null ? "" : name).toItemStack());
    }

    public static SGButton button(Material material, String name, List<String> lore) {
        return new SGButton(new ItemBuilder(material).setName(name == null ? "" : name).setLore(lore).toItemStack());
    }

    public static SGButton button(Material material, String name, String... lore) {
        return new SGButton(new ItemBuilder(material).setName(name == null ? "" : name).setLore(lore).toItemStack());
    }

    public static int getNextRowSlotId(SGMenu sgMenu) {
        int highestFilledSlot = sgMenu.getHighestFilledSlot();
        return highestFilledSlot % 9 + highestFilledSlot;
    }

    public static void fillToNextRow(SGMenu sgMenu) {
        int untilNextRow = sgMenu.getHighestFilledSlot() % 9;
        for (int i = 0; i < 8 - untilNextRow; i++) {
            sgMenu.addButton(new SGButton(new ItemBuilder(Material.AIR).toItemStack()));
        }
    }
}
