package me.ANONIMUS.proxy;

import lombok.Data;
import me.ANONIMUS.proxy.protocol.ProxyServer;
import me.ANONIMUS.proxy.threads.MemoryFreeThread;
import me.ANONIMUS.proxy.threads.MessageThread;
import me.ANONIMUS.proxy.command.CommandManager;
import me.ANONIMUS.proxy.config.ConfigManager;
import me.ANONIMUS.proxy.objects.Account;
import me.ANONIMUS.proxy.protocol.packet.PacketRegistry;
import me.ANONIMUS.proxy.threads.TitleLagThread;
import me.ANONIMUS.proxy.utils.java.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

@Data
public class BetterProxy {
    private final CommandManager commandManager;
    private final PacketRegistry packetRegistry;
    private final ConfigManager configManager;
    private static BetterProxy instance;
    private final List<Account> accounts;
    private final ProxyServer server;
    private final File dirFolder;

    public BetterProxy() {
        instance = this;
        dirFolder = new File("BetterProxy");
        commandManager = new CommandManager();
        packetRegistry = new PacketRegistry();
        configManager = new ConfigManager(new File(dirFolder + "/config.json"));
        configManager.read();
        accounts = new ArrayList<>();
        server = new ProxyServer(configManager.getConfig().port);
    }

    public void run() {
        System.out.println("[ ------------------------------- ]");
        System.out.println("|      Starting BetterProxy       |");
        System.out.println("|        Proxy by ANONIMUS        |");
        System.out.println("[ ------------------------------- ]");
        System.out.println();
        System.out.println("> Creating files...");
        FileUtil.createMissing();
        System.out.println("> Loading packets...");
        packetRegistry.load();
        System.out.println("> Loading commands...");
        commandManager.init();
        System.out.println("> Loading accounts...");
        FileUtil.loadAccounts();
        System.out.println("> Starting the server...");
        server.bind();

        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TitleLagThread(), 1000L, 1000L);
        timer.scheduleAtFixedRate(new MessageThread(), 80000L, 80000L);
        timer.scheduleAtFixedRate(new MemoryFreeThread(), 60000L, 60000L);
    }

    public static BetterProxy getInstance() {
        return instance;
    }
}