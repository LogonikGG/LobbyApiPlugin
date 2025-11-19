package ru.logonik.lobbyapi;

import co.aikar.commands.PaperCommandManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.logonik.lobbyapi.api.LobbyApi;
import ru.logonik.lobbyapi.api.LobbyPlayers;
import ru.logonik.lobbyapi.commands.LobbyCommand;
import ru.logonik.lobbyapi.commands.PlayersCommand;
import ru.logonik.lobbyapi.gui.AllPlayersGui;
import ru.logonik.lobbyapi.innir.LeaveJoinEventsHandler;
import ru.logonik.lobbyapi.innir.LobbyApiImpl;
import ru.logonik.lobbyapi.innir.LobbyPlayersImpl;
import ru.logonik.spigui.SpiGUI;

import java.util.Locale;

public class LobbyPlugin extends JavaPlugin {

    private ServiceLocator services;

    @Override
    public void onLoad() {
        Logger.setLogger(getLogger());
    }

    @Override
    public void onEnable() {
        services = new ServiceLocator();
        saveDefaultConfig();

        LobbyPlayersImpl lobbyPlayersApi = new LobbyPlayersImpl(this);
        LeaveJoinEventsHandler leaveJoinEventsHandler = new LeaveJoinEventsHandler(lobbyPlayersApi);
        getServer().getPluginManager().registerEvents(leaveJoinEventsHandler, this);
        services.registerService(LobbyPlayersImpl.class, lobbyPlayersApi);

        LobbyApiImpl lobbyApi = new LobbyApiImpl(this, lobbyPlayersApi);
        getServer().getPluginManager().registerEvents(lobbyApi, this);
        services.registerService(LobbyApiImpl.class, lobbyApi);

        initGui(lobbyPlayersApi);

        initCommandManager(services);

        services.onPluginEnable();
    }

    private void initGui(LobbyPlayersImpl lobbyPlayersApi) {
        SpiGUI spiGUI = new SpiGUI(this);
        AllPlayersGui allPlayersGui = new AllPlayersGui(spiGUI, lobbyPlayersApi);
        services.registerService(AllPlayersGui.class, allPlayersGui);
    }


    private void initCommandManager(ServiceLocator services) {
        PaperCommandManager manager = new PaperCommandManager(this);
        Locale ruLocale = Locale.of("ru");
        manager.addSupportedLanguage(ruLocale);
        manager.getLocales().setDefaultLocale(ruLocale);
        manager.registerDependency(LobbyPlayersImpl.class, services.getService(LobbyPlayersImpl.class));
        manager.registerDependency(LobbyPlayers.class, services.getService(LobbyPlayersImpl.class));
        manager.registerCommand(new LobbyCommand());

        AllPlayersGui allPlayersGui = services.getService(AllPlayersGui.class);
        if(allPlayersGui != null) {
            manager.registerDependency(AllPlayersGui.class, allPlayersGui);
            manager.registerCommand(new PlayersCommand());
        }
    }

    public LobbyApi getLobbyApi() {
        return services.getService(LobbyApiImpl.class);
    }

    public LobbyPlayers getLobbyPlayersApi() {
        return services.getService(LobbyPlayersImpl.class);
    }

    @Override
    public void onDisable() {
        saveConfig();
        services.onPluginDisable();
    }
}
