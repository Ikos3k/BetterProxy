package me.ANONIMUS.proxy;

import lombok.Getter;
import me.ANONIMUS.proxy.managers.CommandManager;
import me.ANONIMUS.proxy.managers.ConfigManager;
import me.ANONIMUS.proxy.managers.ExploitManager;
import me.ANONIMUS.proxy.managers.PlayerManager;
import me.ANONIMUS.proxy.objects.Account;
import me.ANONIMUS.proxy.protocol.ProxyServer;
import me.ANONIMUS.proxy.protocol.packet.PacketRegistry;
import me.ANONIMUS.proxy.threads.*;
import me.ANONIMUS.proxy.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

@Getter
public class BetterProxy {
    private final CommandManager commandManager;
    private final PacketRegistry packetRegistry;
    private final ExploitManager exploitManager;
    private final ConfigManager configManager;
    private final PlayerManager playerManager;
    private static BetterProxy instance;
    private final List<Account> accounts;
    private final ProxyServer server;
    private final File dirFolder;
    private final String icon;

    public BetterProxy() {
        instance = this;
        dirFolder = new File("BetterProxy");
        commandManager = new CommandManager();
        packetRegistry = new PacketRegistry();
        exploitManager = new ExploitManager();
        configManager = new ConfigManager(new File(dirFolder + "/config.json"));
        icon = FileUtil.loadIconFile(configManager.getConfig().icon);
        playerManager = new PlayerManager();
        accounts = new ArrayList<>();
        server = new ProxyServer();
    }

    public void run() {
        System.out.println("[ ------------------------------------- ]");
        System.out.println("|         Starting BetterProxy          |");
        System.out.println("|       Proxy by ANONIMUS(Ikos3k)       |");
        System.out.println("| https://github.com/Ikos3k/BetterProxy |");
        System.out.println("[ ------------------------------------- ]");
        System.out.println();
        System.out.println("> Creating files...");
        FileUtil.createMissing();
        System.out.println("> Loading packets...");
        packetRegistry.init();
        System.out.println("> Loading exploits...");
        FileUtil.loadExploits();
        System.out.println("> Loading commands...");
        commandManager.init();
        System.out.println("> Loading accounts...");
        FileUtil.loadAccounts();
        System.out.println("> Starting the server...");
        server.bind();
        System.out.println();

        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TitleLagThread(), 1000L, 1000L);
        timer.scheduleAtFixedRate(new ScoreboardThread(), 1000L, 1000L);
        timer.scheduleAtFixedRate(new MessageThread(), 80000L, 80000L);
        timer.scheduleAtFixedRate(new MemoryFreeThread(), 60000L, 60000L);
        timer.scheduleAtFixedRate(new TabThread(), 1000L, 1000L);
    }

    public static BetterProxy getInstance() {
        return instance;
    }
}