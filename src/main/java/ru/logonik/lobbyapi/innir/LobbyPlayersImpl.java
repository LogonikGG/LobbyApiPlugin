package ru.logonik.lobbyapi.innir;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.Nullable;
import ru.logonik.lobbyapi.LobbyPlugin;
import ru.logonik.lobbyapi.api.InnerLobbyPlayers;
import ru.logonik.lobbyapi.api.PluginInfo;
import ru.logonik.lobbyapi.models.*;

import java.util.*;

public class LobbyPlayersImpl implements InnerLobbyPlayers, Listener {
    private final Map<UUID, PlayerState> players = new HashMap<>();
    private final LobbyPlugin plugin;
    private @Nullable Location lobbyLocation;

    public LobbyPlayersImpl(LobbyPlugin plugin) {
        this.plugin = plugin;
        boolean enableLobbyLocation = plugin.getConfig().getBoolean("enable_lobby_location", true);
        if (enableLobbyLocation) {
            this.lobbyLocation = plugin.getConfig().getLocation("lobby_location");
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent e) {
        handleJoin(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLeave(PlayerQuitEvent e) {
        handleQuit(e.getPlayer());
    }

    private void handleJoin(Player player) {
        players.merge(player.getUniqueId(), new PlayerState(player), (oldPlayerState, newPlayerState) -> {
            oldPlayerState.setPlayer(player);
            return oldPlayerState;
        });
    }

    private void handleQuit(Player player) {
        PlayerState playerState = players.get(player.getUniqueId());
        if (playerState != null) {
            playerState.handleQuit();
        }
    }

    @Override
    public void returnToLobby(Player player) {
        PlayerState playerState = players.get(player.getUniqueId());
        Objects.requireNonNull(playerState);
        if (playerState.isInLobby()) {
            return;
        }
        GameSession leavedGameSession = playerState.gameSession();
        PluginInfo pluginInfo = playerState.pluginInfo();
        playerState.setInLobby(true);
        playerState.setGameSession(null, null);
        playerState.setLeavedGameSession(pluginInfo, leavedGameSession);

        try {
            leavedGameSession.onStartReturnToLobby(player);
        } finally {
            playerReturnToLobbyInternal(player);
            leavedGameSession.onEndReturnToLobby(player);
        }
    }

    @Override
    public void returnToLobbyByGameEnd(PluginInfo pluginInfo, Player player) {
        PlayerState playerState = players.get(player.getUniqueId());
        Objects.requireNonNull(playerState);

    }

    private void playerReturnToLobbyInternal(Player player) {
        teleport(player);
    }

    @Override
    public void teleport(Player player) {
        if (lobbyLocation != null) {
            player.teleport(lobbyLocation);
        }
    }

    @Override
    public void registerInGame(PluginInfo pluginInfo, Player player, GameSessionInfoHandler info, GameSessionGameHandler game) {
        registerInGame(pluginInfo, player, new GameSessionContainer(info, game));
    }

    @Override
    public void registerInGame(PluginInfo pluginInfo, Player player, GameSession handler) {
        PlayerState playerState = players.get(player.getUniqueId());
        Objects.requireNonNull(playerState);
        if (!playerState.isInLobby() && playerState.gameSession() != null) {
            throw new IllegalStateException("Player `%s` already in game `%s` but trying to add to new one: `%s`"
                    .formatted(player.getName(), playerState.gameSession().getCommonGameName(), handler.getCommonGameName()));
        }
        playerState.setInLobby(false);
        playerState.setGameSession(pluginInfo, handler);
        playerState.setLeavedGameSession(null, null);
    }

    @Override
    public void removeFromRejoin(PluginInfo pluginInfo, UUID player) {
        PlayerState playerState = players.get(player);
        if (playerState != null) {
            playerState.setLeavedGameSession(null, null);
        }
    }

    @Override
    public void removeFromRejoin(PluginInfo pluginInfo, Iterable<UUID> players) {
        for (UUID player : players) {
            PlayerState playerState = this.players.get(player);
            if (playerState != null) {
                playerState.setLeavedGameSession(null, null);
            }
        }
    }

    @Override
    public boolean isFree(PluginInfo pluginInfo, Player player) {
        PlayerState playerState = players.get(player.getUniqueId());
        Objects.requireNonNull(playerState);
        return playerState.isInLobby();
    }

    @Override
    public List<PlayerState> createPlayersStateList() {
        return new ArrayList<>(players.values());
    }

    @Override
    public @Nullable Location getLobbyLocation() {
        return lobbyLocation;
    }

    public void setLobbyLocation(@Nullable Location location) {
        plugin.getConfig().set("lobby_location", location);
        boolean enableLobbyLocation = plugin.getConfig().getBoolean("enable_lobby_location", true);
        if (enableLobbyLocation) {
            this.lobbyLocation = location;
        }
    }

    public void onLobbyPluginDisable() {
        boolean shouldReturnGamers = plugin.getConfig().getBoolean("return_gamers_to_lobby_on_disable", true);
        if (shouldReturnGamers) {
            for (PlayerState playerStates : this.players.values()) {
                if (playerStates.player() != null) {
                    returnToLobby(playerStates.player());
                }
            }
        }
    }
}
