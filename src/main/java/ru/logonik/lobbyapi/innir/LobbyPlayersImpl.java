package ru.logonik.lobbyapi.innir;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ru.logonik.lobbyapi.*;
import ru.logonik.lobbyapi.LobbyPlugin;
import ru.logonik.lobbyapi.api.LobbyPlayers;
import ru.logonik.lobbyapi.models.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

// todo problems: 1) if mini game plugin disable and not run return, we still think player inside game; 2) bootstrap of leave

public class LobbyPlayersImpl implements LobbyPlayers, PluginDisableListener {
    private final LobbyCommonAsksHandler lobbyCommonAsksHandler = new LobbyAsksHandlerImpl();
    private final Set<UUID> inLobby = new HashSet<>();
    private final Map<UUID, LobbyCommonAsksHandler> inGames = new HashMap<>();
    private final Map<UUID, LobbyCommonAsksHandler> leavedGamers = new HashMap<>();
    private final LobbyPlugin plugin;
    private @Nullable Location lobbyLocation;
    private LobbyApiImpl lobbyApi;

    public LobbyPlayersImpl(LobbyPlugin plugin) {
        this.plugin = plugin;
        boolean enableLobbyLocation = plugin.getConfig().getBoolean("enable_lobby_location", true);
        if (enableLobbyLocation) {
            this.lobbyLocation = plugin.getConfig().getLocation("lobby_location");
            ;
        }
    }

    public void setLobbyApiImlp(LobbyApiImpl lobbyApi) {
        this.lobbyApi = lobbyApi;
    }

    @Override
    public void returnToLobby(Player player) {
        UUID uuid = player.getUniqueId();
        if (inLobby.contains(uuid)) {
            return;
        }
        LobbyCommonAsksHandler gameLeaved = inGames.remove(uuid);
        inLobby.add(uuid);
        leavedGamers.put(uuid, gameLeaved);
        if (gameLeaved == null) {
            playerReturnToLobbyInternal(player);
            return;
        }
        try {
            gameLeaved.onStartReturnToLobby(player);
        } finally {
            playerReturnToLobbyInternal(player);
            gameLeaved.onEndReturnToLobby(player);
        }
    }

    @Override
    public void returnToLobbyByGameEnd(Player player) {
        UUID uuid = player.getUniqueId();
        inLobby.add(uuid);
        leavedGamers.remove(uuid);
        inGames.remove(uuid);
        playerReturnToLobbyInternal(player);
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
    public void registerInGame(Player player, LobbyAsksInfoHandler info, LobbyAsksGameHandler game) {
        registerInGame(player, new ContainerHandler(info, game));
    }

    @Override
    public void registerInGame(Player player, LobbyCommonAsksHandler handler) {
        UUID uuid = player.getUniqueId();
        inLobby.remove(uuid);
        inGames.put(uuid, handler);
        leavedGamers.remove(uuid);
    }

    @Override
    public void removeFromRejoin(UUID player) {
        leavedGamers.remove(player);
    }

    @Override
    public void removeFromRejoin(Iterable<UUID> players) {
        for (UUID player : players) {
            leavedGamers.remove(player);
        }
    }

    @Override
    public boolean isFree(Player player) {
        UUID uuid = player.getUniqueId();
        return inLobby.contains(uuid);
    }

    @Override
    public List<PlayerState> createPlayersStateList() {
        Map<UUID, Player> onlinePlayers = Bukkit.getOnlinePlayers()
                .stream()
                .collect(Collectors.toMap(Player::getUniqueId, Function.identity()));

        ArrayList<PlayerState> playerStates = new ArrayList<>();
        for (UUID uuid : inLobby) {
            Player player = onlinePlayers.get(uuid);
            playerStates.add(new PlayerState(player, lobbyCommonAsksHandler));
        }
        for (Map.Entry<UUID, LobbyCommonAsksHandler> entry : inGames.entrySet()) {
            Player player = onlinePlayers.get(entry.getKey());
            playerStates.add(new PlayerState(player, entry.getValue()));
        }
        return playerStates;
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

    void handleJoin(Player player) {
        UUID uuid = player.getUniqueId();
        inLobby.add(uuid);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            LobbyCommonAsksHandler handler = leavedGamers.remove(uuid);
            if (handler != null && handler.tryRejoin(player)) {
                inLobby.remove(uuid);
                inGames.put(uuid, handler);
            }
        }, 1L);
    }

    void handleQuit(Player player) {
        UUID uuid = player.getUniqueId();
        LobbyCommonAsksHandler handler = inGames.remove(uuid);
        if (handler != null) {
            leavedGamers.put(uuid, handler);
        }
    }

    @Override
    public void disable() throws Exception {
        boolean shouldReturnGamers = plugin.getConfig().getBoolean("return_gamers_to_lobby_on_disable", true);
        if (shouldReturnGamers) {
            Map<UUID, Player> onlinePlayers = Bukkit.getOnlinePlayers()
                    .stream()
                    .collect(Collectors.toMap(Player::getUniqueId, Function.identity()));

            ArrayList<Player> returnPlayers = new ArrayList<>();
            for (Map.Entry<UUID, LobbyCommonAsksHandler> entry : inGames.entrySet()) {
                Player player = onlinePlayers.get(entry.getKey());
                returnPlayers.add(player);
            }
            for (Player returnPlayer : returnPlayers) {
                returnToLobby(returnPlayer);
            }
        }
    }
}
