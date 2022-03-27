package me.Ikos3k.proxy.commands.more;

import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.utils.ChatUtil;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class CommandPortScanner extends Command {
    public CommandPortScanner() {
        super("portscanner", null, null, "[ip] [start port] [end port]", ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        String host = args[1];
        if (host.contains(":")) {
            host = host.split(":", 2)[0];
        }

        ChatUtil.sendChatMessage("&7Port scan started " + sender.getThemeType().getColor(1) + host, sender, true);
        ChatUtil.sendChatMessage("&7Started port &c" + args[2], sender, true);
        List<Integer> ports = new ArrayList<>();
        String finalHost = host;
        Executors.newSingleThreadExecutor().submit(() -> {
            for (int i = Integer.parseInt(args[2]); i < Integer.parseInt(args[3]); ++i) {
                try {
                    final Socket socket = new Socket();
                    socket.connect(new InetSocketAddress(finalHost, i), 500);
                    ports.add(i);
                    ChatUtil.sendChatMessage("&7A working port has been found &a" + i, sender, true);
                } catch (Exception ignored) {}
            }
            ChatUtil.sendChatMessage("&7Scanning is complete &8(" + sender.getThemeType().getColor(2) + ports.size() + "&8)", sender, true);
            ChatUtil.sendChatMessage("&7Open ports: " + sender.getThemeType().getColor(1) + ports, sender, true);
            ports.clear();
        });
    }
}