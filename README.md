# LobbyPluginApi
**LobbyPluginApi** is a plugin designed to manage player sessions between different plugins.
It is especially useful on minigame servers **without a proxy** (like Velocity or BungeeCord).

## Content:
- [Purpose](#purpose)
- [Usage](#usage)

## Purpose
This plugin provides a shared container for player states on the server, allowing minigame plugins to:
  -  Check if a player is already participating in another game.
  -  Force a player to leave another game if they try to join a new one.

## Usage
It's important to ensure that LobbyPluginApi is loaded before game plugins.
### Add in plugin.yml:
```yaml
depend: [LobbyPluginApi]    # guarantees that API is loaded before your plugin
# OR
softdepend: [LobbyPluginApi] # optional API; your plugin will work even if it's missing
```

            // TODO Logonik

### Hook plugin:
You can get `LobbyPluginApi` like this
```java
public final class WoolWarPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        LobbyPlugin lobbyPlugin = (LobbyPlugin) Bukkit.getPluginManager().getPlugin("LobbyPluginApi");
        if (lobbyPlugin != null) {
            lobbyPlugin.getLobbyApi().registerPlugin(new PluginInfo(this, onLobbyPluginGoDisable)); // onLobbyPluginGoDisable - is Runnable
            LobbyPlayers players = lobbyPlugin.getLobbyPlayersApi();

            //players.registerInGame(player, handler);
        }
    }
}
```