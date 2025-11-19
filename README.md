# LobbyApiPlugin
**LobbyApiPlugin** is a plugin designed to manage player sessions between different plugins.
It is especially useful on minigame servers **without a proxy** (like Velocity or BungeeCord).

## Content:
- [Purpose](#purpose)
- [Usage](#usage)

## Purpose
This plugin provides a shared container for player states on the server, allowing minigame plugins to:
  -  Check if a player is already participating in another game.
  -  Force a player to leave another game if they try to join a new one.

## Usage
It's important to ensure that game plugins are loaded before `LobbyApiPlugin`.
This prevents issues such as broken references to **disabled**  plugins in case of reloads (e.g. via Plugman or /reload).
### Add in plugin.yml:
```yaml
loadbefore:
  - LobbyPluginApi
```
### Hook plugin:
Implement the `OnLobbyPluginLoadListener` interface.
This method will be called when LobbyApiPlugin is enabled:
```java
public final class WoolWarPlugin extends JavaPlugin implements OnLobbyPluginLoadListener {
    @Override
    public void onLobbyPluginEnable(lobbyPlugin lobbyPlugin) {
        reInitLobby(lobbyPlugin);
    }
}
```