package ru.logonik.lobbyapi.innir;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import ru.logonik.lobbyapi.*;
import ru.logonik.lobbyapi.LobbyPlugin;
import ru.logonik.lobbyapi.models.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LobbyPlayersImpl implements LobbyPlayers {
    private final LobbyCommonAsksHandler lobbyCommonAsksHandler = new LobbyAsksHandlerImpl();
    private final Set<UUID> inLobby = new HashSet<>();
    private final Map<UUID, LobbyCommonAsksHandler> inGames = new HashMap<>();
    private final Map<UUID, LobbyCommonAsksHandler> leavedGamers = new HashMap<>();
    private final LobbyPlugin plugin;
    private Location lobbyLocation;

    public LobbyPlayersImpl(LobbyPlugin plugin, Location lobbyLocation) {
        this.plugin = plugin;
        this.lobbyLocation = lobbyLocation;

        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        for (Player player : players) {
            if(Objects.equals(lobbyLocation.getWorld(), player.getWorld())) {
                inLobby.add(player.getUniqueId());
            }
        }
    }

    @Override
    public void returnToLobbyByPlayer(Player player) {
        internalReturnToLobby(player);
        player.sendMessage("Вы вернулись в лобби.");
    }

    @Override
    public void returnToLobbyByAdmin(Player player) {
        internalReturnToLobby(player);
        player.sendMessage("Администратор вернул вас в лобби.");
    }

    @Override
    public void returnToLobbyByGameEnd(Player player) {
        internalReturnToLobby(player);
    }

    @Override
    public void returnToLobbyByKick(Player player) {
        internalReturnToLobby(player);
        player.sendMessage("Вы были исключены из игры.");
    }

    protected void internalReturnToLobby(Player player) {
        UUID uuid = player.getUniqueId();
        inLobby.add(uuid);
        leavedGamers.remove(uuid);
        teleport(player);
        LobbyCommonAsksHandler handler = inGames.remove(uuid);
        if (handler != null) {
            handler.onLeave(player);
        }
    }

    @Override
    public void teleport(Player player) {
        player.teleport(lobbyLocation);
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
    }

    @Override
    public void removeFromRejoin(Player player) {
        leavedGamers.remove(player.getUniqueId());
    }

    @Override
    public void removeFromRejoin(Iterable<Player> players) {
        for (Player player : players) {
            leavedGamers.remove(player.getUniqueId());
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
    public Location getLobbyLocation() {
        return lobbyLocation;
    }

    public void setLobbyLocation(Location location) {
        this.lobbyLocation = location;
    }

    void handleJoin(Player player) {
        UUID uuid = player.getUniqueId();
        inLobby.add(uuid);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            LobbyCommonAsksHandler handler = leavedGamers.remove(uuid);
            if (handler != null && handler.tryRejoin(player)) {
                inGames.put(uuid, handler);
                player.sendMessage("Вы были переподключены к игре.");
            } else {
                inLobby.add(uuid);
                player.sendMessage("Не удалось вернуться в игру.");
            }
        }, 1L);
    }

    void handleQuit(Player player) {
        UUID uuid = player.getUniqueId();
        LobbyCommonAsksHandler handler = inGames.remove(uuid);
        if (handler != null) {
            handler.onLeave(player);
            leavedGamers.put(uuid, handler);
        }
    }
}
