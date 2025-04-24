package ru.logonik.lobbyapi.models;

import org.bukkit.entity.Player;

public record PlayerState(Player player, LobbyCommonAsksHandler lobbyCommonAsksHandler) {
}
