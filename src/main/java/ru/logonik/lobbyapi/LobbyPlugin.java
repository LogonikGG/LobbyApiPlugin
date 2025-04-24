package ru.logonik.lobbyapi;

import co.aikar.commands.PaperCommandManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.logonik.lobbyapi.commands.LobbyCommand;
import ru.logonik.lobbyapi.commands.PlayersCommand;
import ru.logonik.lobbyapi.gui.AllPlayersGui;
import ru.logonik.lobbyapi.innir.LeaveJoinEventsHandler;
import ru.logonik.lobbyapi.innir.LobbyPlayersImpl;
import ru.logonik.spigui.SpiGUI;

import java.util.Locale;

public class LobbyPlugin extends JavaPlugin {

    private ServiceLocator services;

    @Override
    public void onEnable() {
        services = new ServiceLocator();
        LobbyPlayersImpl lobbyPlayersApi = new LobbyPlayersImpl(this, Bukkit.getWorlds().get(0).getSpawnLocation());
        services.registerService(LobbyPlayersImpl.class, lobbyPlayersApi);
        services.registerService(LobbyPlayers.class, lobbyPlayersApi);
        SpiGUI spiGUI = new SpiGUI(this);
        AllPlayersGui allPlayersGui = new AllPlayersGui(spiGUI, lobbyPlayersApi);
        services.registerService(AllPlayersGui.class, allPlayersGui);

        initEventsHandlers(services);
        initCommandManager(services);
    }

    private void initEventsHandlers(ServiceLocator services) {
        LeaveJoinEventsHandler leaveJoinEventsHandler = new LeaveJoinEventsHandler(services.getService(LobbyPlayersImpl.class));
        getServer().getPluginManager().registerEvents(leaveJoinEventsHandler, this);
    }

    private void initCommandManager(ServiceLocator services) {
        PaperCommandManager manager = new PaperCommandManager(this);
        Locale ruLocale = Locale.of("ru");
        manager.addSupportedLanguage(ruLocale);
        manager.getLocales().setDefaultLocale(ruLocale);
        manager.registerDependency(LobbyPlayersImpl.class, services.getService(LobbyPlayersImpl.class));
        manager.registerDependency(AllPlayersGui.class, services.getService(AllPlayersGui.class));

        manager.registerCommand(new LobbyCommand());
        manager.registerCommand(new PlayersCommand());
    }

    public LobbyPlayers getLobbyPlayersApi() {
        return services.getService(LobbyPlayers.class);
    }
}
