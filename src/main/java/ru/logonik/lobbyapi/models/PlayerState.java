package ru.logonik.lobbyapi.models;

import org.bukkit.entity.Player;
import ru.logonik.lobbyapi.api.PluginInfo;

import java.util.Objects;

public final class PlayerState {
    private Player player;
    private GameSession gameSession;
    private PluginInfo gameSessionPluginInfo;
    private GameSession leavedGameSession;
    private PluginInfo leavedGameSessionPluginInfo;

    public PlayerState(Player player) {
        this.player = player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player player() {
        return player;
    }

    public void handleQuit() {
        this.player = null;
    }

    public boolean isInLobby() {
        return gameSession == null;
    }

    public GameSession gameSession() {
        return gameSession;
    }

    public PluginInfo gameSessionPluginInfo() {
        return gameSessionPluginInfo;
    }

    public PluginInfo leavedGameSessionPluginInfo() {
        return leavedGameSessionPluginInfo;
    }

    public GameSession leavedGameSession() {
        return leavedGameSession;
    }

    public void setLeavedGameSession(PluginInfo pluginInfo, GameSession leavedGameSession) {
        if(leavedGameSession == null) {
            this.leavedGameSessionPluginInfo = null;
            this.leavedGameSession = null;
        } else {
            this.leavedGameSessionPluginInfo = Objects.requireNonNull(pluginInfo);
            this.leavedGameSession = leavedGameSession;
        }
    }

    public void setGameSession(PluginInfo pluginInfo, GameSession gameSession) {
        if(gameSession == null) {
            this.gameSessionPluginInfo = null;
            this.gameSession = null;
        } else {
            this.gameSessionPluginInfo = Objects.requireNonNull(pluginInfo);
            this.gameSession = gameSession;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (PlayerState) obj;
        return Objects.equals(this.player.getUniqueId(), that.player.getUniqueId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(player.getUniqueId());
    }
}
