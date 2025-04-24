package ru.logonik.lobbyapi.gui;

import org.bukkit.entity.Player;
import ru.logonik.spigui.menu.SGMenu;

public interface LogoGui {
    void update(SGMenu sgMenu, Player player);
    void open(Player player);
}
