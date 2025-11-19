package ru.logonik.lobbyapi;

import co.aikar.commands.PaperCommandManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import ru.logonik.lobbyapi.api.LobbyApi;
import ru.logonik.lobbyapi.api.LobbyApiException;
import ru.logonik.lobbyapi.api.LobbyPlayers;
import ru.logonik.lobbyapi.api.PluginInfo;
import ru.logonik.lobbyapi.commands.LobbyCommand;
import ru.logonik.lobbyapi.commands.PlayersCommand;
import ru.logonik.lobbyapi.gui.AllPlayersGui;
import ru.logonik.lobbyapi.innir.LeaveJoinEventsHandler;
import ru.logonik.lobbyapi.innir.LobbyApiImpl;
import ru.logonik.lobbyapi.innir.LobbyPlayersImpl;
import ru.logonik.spigui.SpiGUI;

import java.util.Locale;

public class LobbyPlugin extends JavaPlugin {

    private LobbyPlayersImpl lobbyPlayersApi;
    private LobbyApiImpl lobbyApi;
    private AllPlayersGui allPlayersGui;

    @Override
    public void onLoad() {
        Logger.setLogger(getLogger());
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        lobbyPlayersApi = new LobbyPlayersImpl(this);
        LeaveJoinEventsHandler leaveJoinEventsHandler = new LeaveJoinEventsHandler(lobbyPlayersApi);
        getServer().getPluginManager().registerEvents(leaveJoinEventsHandler, this);

        lobbyApi = new LobbyApiImpl(lobbyPlayersApi);
        getServer().getPluginManager().registerEvents(lobbyApi, this);

        initGui(lobbyPlayersApi);

        initCommandManager();
    }

    private void initGui(LobbyPlayersImpl lobbyPlayersApi) {
        SpiGUI spiGUI = new SpiGUI(this);
        allPlayersGui = new AllPlayersGui(spiGUI, lobbyPlayersApi);
    }


    private void initCommandManager() {
        PaperCommandManager manager = new PaperCommandManager(this);
        Locale ruLocale = Locale.of("ru");
        manager.addSupportedLanguage(ruLocale);
        manager.getLocales().setDefaultLocale(ruLocale);
        manager.registerDependency(LobbyPlayersImpl.class, lobbyPlayersApi);
        manager.registerDependency(LobbyPlayers.class, lobbyPlayersApi);
        manager.registerCommand(new LobbyCommand());

        if(allPlayersGui != null) {
            manager.registerDependency(AllPlayersGui.class, allPlayersGui);
            manager.registerCommand(new PlayersCommand());
        }
    }

    public LobbyPlayers registerPlugin(PluginInfo plugin) throws LobbyApiException {
        return lobbyApi.registerPlugin(plugin);
    }

    public void unregisterPlugin(Plugin plugin) {
        lobbyApi.unregisterPlugin(plugin);
    }

    @Override
    public void onDisable() {
        saveConfig();
        lobbyApi.onLobbyPluginDisable();
        lobbyPlayersApi.onLobbyPluginDisable();
    }
}
