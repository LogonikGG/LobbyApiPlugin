package ru.logonik.lobbyapi;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Main api class that handle players in lobby,
 * contains method for check that player not already in another game {@link LobbyPlayersApi#isFree},
 * teleport to spawn {@link LobbyPlayersApi#teleport(Player)},
 * or mark player as joined to game {@link LobbyPlayersApi#leaveLobby(Player, LobbyAsksInfoHandler, LobbyAsksGameHandler)}.
 */
public class LobbyPlayersApi {
    private final HashMap<UUID, LobbyCommonAsksHandler> inGames;
    private final HashMap<UUID, LobbyCommonAsksHandler> leavedGamers;
    private final Set<UUID> inLobby;
    private final LobbyPlugin lobbyPlugin;
    private Location lobbyLocation;


    /**
     * Create a Lobby players Api
     *
     * @param lobbyLocation is location where players will be returned at games stop or just spawned
     */
    public LobbyPlayersApi(LobbyPlugin lobbyPlugin, Location lobbyLocation) {
        this.lobbyPlugin = lobbyPlugin;
        inGames = new HashMap<>();
        inLobby = new HashSet<>();
        leavedGamers = new HashMap<>();
        this.lobbyLocation = lobbyLocation;
    }

    /**
     * Should be use when game is over.
     * Teleport player and add to list of lobby players
     * Will not call handlers that player is leave
     *
     * @param player that should be returned and marked as free
     */
    public void joinLobby(Player player) {
        inLobby.add(player.getUniqueId());
        inGames.remove(player.getUniqueId());
        leavedGamers.remove(player.getUniqueId());
        teleport(player);
    }

    /**
     * Only teleport specific player to lobby, not mark he as free player
     *
     * @param player that will be just teleport
     */
    public void teleport(Player player) {
        player.teleport(lobbyLocation);
    }

    public Location getLobbyLocation() {
        return lobbyLocation;
    }

    /**
     * Should be use when game is over.
     * Add to list of lobby players
     * Will not call handlers that player is leave
     *
     * @param player that should be returned and marked as free
     */
    public void joinLobbyWithoutTeleport(Player player) {
        inLobby.add(player.getUniqueId());
        inGames.remove(player.getUniqueId());
        leavedGamers.remove(player.getUniqueId());
    }

    /**
     * Mark player as not free
     *
     * @param player      that join to game lobby, or game, and should not join to another game
     * @param gameHandler presents class that handle game
     * @param infoHandler presents info of game
     */
    public void leaveLobby(Player player, LobbyAsksInfoHandler infoHandler, LobbyAsksGameHandler gameHandler) {
        inLobby.remove(player.getUniqueId());
        inGames.put(player.getUniqueId(), new LobbyGameHandler(infoHandler, gameHandler));
    }

    /**
     * Mark player as not free
     *
     * @param player        that join to game lobby, or game, and should not join to another game
     * @param commonHandler presents class that handle game and present info of game
     */
    public void leaveLobby(Player player, LobbyCommonAsksHandler commonHandler) {
        inLobby.remove(player.getUniqueId());
        inGames.put(player.getUniqueId(), commonHandler);
    }

    public void leaveLobby(Iterable<Player> players, LobbyAsksInfoHandler infoHandler, LobbyAsksGameHandler gameHandler) {
        for (Player player : players) {
            inLobby.remove(player.getUniqueId());
            inGames.put(player.getUniqueId(), new LobbyGameHandler(infoHandler, gameHandler));
        }
    }

    public void leaveLobby(Iterable<Player> players, LobbyCommonAsksHandler commonHandler) {
        for (Player player : players) {
            inLobby.remove(player.getUniqueId());
            inGames.put(player.getUniqueId(), commonHandler);
        }
    }

    /**
     * Check if player is not join to one of game, and staying in lobby
     *
     * @param player specific player that should be checked
     * @return True - if player is in main lobby, and not already playing in game,
     * False - if player already in game
     */
    public boolean isFree(Player player) {
        return inLobby.contains(player.getUniqueId()) || !inLobby.contains(player.getUniqueId()) && !inGames.containsKey(player.getUniqueId());
    }

    /**
     * Set lobby Location
     *
     * @param lobbyLocation location where player will be teleported at game stop
     */
    public void setLobbyLocation(Location lobbyLocation) {
        this.lobbyLocation = lobbyLocation;
    }

    /**
     * Probably will be used only by another lobby plugin. Games plugins should never call this method
     *
     * @param player player that should be forced to lobby
     */
    public void forceLeavePlayer(Player player) {
        onLobbyCommand(player);
    }

    /**
     * Should call by game plugin when game is over and player can not rejoin game that he played
     *
     * @param player specific player that can't rejoin game anymore
     */
    public void removeFromCanRejoinList(Player player) {
        leavedGamers.remove(player.getUniqueId());
    }


    /**
     * Should call by game plugin when game is over and players can not rejoin game that he played
     *
     * @param players specific players that can't rejoin game anymore
     */
    public void removeFromCanRejoinList(Iterable<Player> players) {
        for (Player player : players) {
            leavedGamers.remove(player.getUniqueId());
        }
    }

    /**
     * Should call by game plugin when game is over and players can not rejoin game that he played
     *
     * @param players specific players that can't rejoin game anymore
     */
    public void removeFromCanRejoinList(Player... players) {
        for (Player player : players) {
            leavedGamers.remove(player.getUniqueId());
        }
    }

    protected void onLobbyCommand(Player player) {
        onLeave(player, Reason.BY_SELF);
        inLobby.add(player.getUniqueId());
        teleport(player);
    }

    protected void onQuit(Player player) {
        onLeave(player, Reason.DISCONNECT);
    }

    public void onLeave(Player player, Reason reason) {
        LobbyCommonAsksHandler lobbyGameHandler = inGames.remove(player.getUniqueId());
        if (lobbyGameHandler != null) {
            lobbyGameHandler.onLeave(player, reason);
            leavedGamers.put(player.getUniqueId(), lobbyGameHandler);
        }
    }

    protected void onJoin(Player player) {
        Bukkit.getScheduler().runTaskLater(lobbyPlugin, () -> {
            LobbyCommonAsksHandler lobbyGameHandler = leavedGamers.remove(player.getUniqueId());
            if (lobbyGameHandler != null) {
                boolean success = lobbyGameHandler.tryRejoin(player);
                if (success) {
                    player.sendMessage("Вы были перепресоеденены к игре");
                    inGames.put(player.getUniqueId(), lobbyGameHandler);
                    return;
                } else {
                    player.sendMessage("Неудалось переподключить Вас к игре");
                }
            }
            inLobby.add(player.getUniqueId());
        }, 1);
    }
}
