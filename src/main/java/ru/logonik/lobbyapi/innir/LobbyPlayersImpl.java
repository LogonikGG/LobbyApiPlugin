package ru.logonik.lobbyapi.innir;

import org.bukkit.Bukkit;
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
import ru.logonik.lobbyapi.models.GameSession;
import ru.logonik.lobbyapi.models.PlayerState;

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
        Player player = e.getPlayer();
        players.merge(player.getUniqueId(), new PlayerState(player), (oldPlayerState, newPlayerState) -> {
            oldPlayerState.setPlayer(player);
            tryRejoinInNextTick(oldPlayerState);
            return oldPlayerState;
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        PlayerState playerState = players.get(player.getUniqueId());
        if (playerState != null) {
            playerState.handleQuit();
            if(playerState.gameSession() != null) {
                playerState.setLeavedGameSession(playerState.gameSessionPluginInfo(), playerState.gameSession());
            }
        }
    }

    private void tryRejoinInNextTick(PlayerState playerState) {
        if (playerState == null || playerState.leavedGameSession() == null) return;
        Bukkit.getScheduler().runTask(plugin, () -> {
            if (playerState.leavedGameSession() == null || playerState.leavedGameSessionPluginInfo() == null) return;
            if (playerState.leavedGameSessionPluginInfo().plugin().isEnabled()
                    && playerState.player() != null) {
                playerState.leavedGameSession().tryRejoin(playerState.player());
            }
        });
    }

    @Override
    public boolean isFree(UUID player) {
        PlayerState playerState = players.get(player);
        if (playerState == null) return true;
        return playerState.isInLobby();
    }

    @Override
    public boolean forbiddenTransfer(PluginInfo pluginInfo, UUID player, GameSession newGameSession) {
        Objects.requireNonNull(pluginInfo);
        Objects.requireNonNull(newGameSession);
        PlayerState playerState = players.get(player);
        if (playerState == null) return true;
        GameSession gameSession = playerState.gameSession();
        return !newGameSession.equals(gameSession) && !gameSession.canBeForceLeft(player);
    }

    @Override
    public void processJoin(PluginInfo pluginInfo, UUID player, GameSession newGameSession) {
        Objects.requireNonNull(pluginInfo);
        Objects.requireNonNull(newGameSession);
        PlayerState playerState = getPlayerState(player);
        GameSession currentGameSession = playerState.gameSession();

        if (!currentGameSession.equals(newGameSession)) {
            if (!currentGameSession.canBeForceLeft(player)) {
                throw new IllegalStateException("Player `" + player + "` in session `" + playerState.gameSession().getCommonGameName() + "` but trying to leaved by : `" + newGameSession.getCommonGameName() + "`");
            }
            currentGameSession.forceLeave(player);
        }
        playerState.setGameSession(pluginInfo, newGameSession);
        playerState.setLeavedGameSession(null, null);
    }

    @Override
    public void processLeave(PluginInfo pluginInfo, UUID player, GameSession requereGameSession) {
        Objects.requireNonNull(pluginInfo);
        Objects.requireNonNull(requereGameSession);
        PlayerState playerState = getPlayerState(player);
        Objects.requireNonNull(playerState);
        GameSession currentGameSession = playerState.gameSession();
        if (currentGameSession.equals(requereGameSession)) {
            playerState.setGameSession(null, null);
            playerState.setLeavedGameSession(null, null);
        } else if (currentGameSession.canBeForceLeft(player)) {
            currentGameSession.forceLeave(player);
            playerState.setGameSession(null, null);
            playerState.setLeavedGameSession(null, null);
        } else {
            throw new IllegalStateException("Player `" + player + "` in session `" + playerState.gameSession().getCommonGameName() + "` but trying to leaved by : `" + requereGameSession.getCommonGameName() + "`");
        }
    }

    private PlayerState getPlayerState(UUID player) {
        PlayerState playerState = players.get(player);
        if(playerState != null) return playerState;
        Player onlinePlayer = Bukkit.getPlayer(player);
        if(onlinePlayer == null) throw new IllegalArgumentException(player + " is not online player");
        PlayerState newPlayerState = new PlayerState(onlinePlayer);
        players.put(player, newPlayerState);
        return newPlayerState;
    }

    @Override
    public void removeFromRejoin(PluginInfo pluginInfo, UUID player) {
        PlayerState playerState = players.get(player);
        if (playerState != null) {
            removeFromRejoinLogic(player, playerState);
        }
    }

    @Override
    public void removeFromRejoin(PluginInfo pluginInfo, Iterable<UUID> players) {
        for (UUID player : players) {
            PlayerState playerState = this.players.get(player);
            if (playerState != null) {
                removeFromRejoinLogic(player, playerState);
            }
        }
    }

    private void removeFromRejoinLogic(UUID player, PlayerState playerState) {
        if(playerState.leavedGameSession() != null) {
            playerState.setLeavedGameSession(null, null);
            if(playerState.player() == null && playerState.gameSession() == null) {
                players.remove(player);
            }
        }
    }

    @Override
    public List<PlayerState> createPlayersStateList() {
        return new ArrayList<>(players.values());
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

    @Override
    public void returnToLobby(Player player) {
        if (lobbyLocation != null) {
            player.teleport(lobbyLocation);
        }
    }
}
