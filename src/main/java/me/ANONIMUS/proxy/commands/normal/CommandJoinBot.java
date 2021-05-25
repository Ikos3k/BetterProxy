package me.ANONIMUS.proxy.commands.normal;

import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.objects.Command;
import me.ANONIMUS.proxy.protocol.connection.BotConnection;
import me.ANONIMUS.proxy.protocol.connection.ServerPinger;
import me.ANONIMUS.proxy.protocol.objects.Bot;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.utils.ChatUtil;
import me.ANONIMUS.proxy.utils.SRVResolver;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CommandJoinBot extends Command {
    public CommandJoinBot() {
        super("joinbot", "connectbot", "Connecting bots to server", "[host:port] [usernames] [amount] [delay] [resolver] [ping]", ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        String host = args[1];
        int port = 25565;
        if (host.contains(":")) {
            final String[] sp = host.split(":", 2);
            host = sp[0];
            port = Integer.parseInt(sp[1]);
        }

        if (Boolean.parseBoolean(args[5])) {
            final String[] resolved = SRVResolver.getServerAddress(host);
            host = resolved[0];
            port = Integer.parseInt(resolved[1]);
        }

        try {
            final Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), 500);
            socket.close();
        } catch (Exception e) {
            ChatUtil.sendChatMessage("&cThe server has a connection problem or is down!", sender, true);
            return;
        }

        connect(sender, Integer.parseInt(args[4]), host, port, args[2], Integer.parseInt(args[3]), Boolean.parseBoolean(args[6]));
    }

    private void connect(final Player sender, final int delay, final String host, final int port, final String username, final int amount, final boolean ping) {
        Executors.newSingleThreadExecutor().submit(() -> {
            ChatUtil.sendChatMessage(sender.getThemeType().getColor(2) + "Sending!", sender, true);
            for (int i = 0; i < amount; i++) {
                if (ping) {
                    new ServerPinger(sender, false).connect(host, port, Proxy.NO_PROXY);
                }
                new BotConnection().connect(host, port, Proxy.NO_PROXY, new Bot(sender), username + i);
                try {
                    TimeUnit.MILLISECONDS.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ChatUtil.sendChatMessage(sender.getThemeType().getColor(2) + "Sent all bots!", sender, true);
        });
    }
}