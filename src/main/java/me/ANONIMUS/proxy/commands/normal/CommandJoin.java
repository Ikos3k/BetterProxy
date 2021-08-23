package me.ANONIMUS.proxy.commands.normal;

import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.objects.Command;
import me.ANONIMUS.proxy.protocol.connection.PlayerConnection;
import me.ANONIMUS.proxy.protocol.connection.ServerPinger;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.utils.ChatUtil;
import me.ANONIMUS.proxy.utils.SRVResolver;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

public class CommandJoin extends Command {
    public CommandJoin() {
        super("join", "connect", "Connecting to server", "[host:port] [username] [resolver] [ping]", ConnectedType.DISCONNECTED);
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        String ip = args[1];
        int port = 25565;

        if (ip.contains(":")) {
            final String[] sp = ip.split(":", 2);
            ip = sp[0];
            port = Integer.parseInt(sp[1]);
        }

        if (Boolean.parseBoolean(args[3])) {
            final String[] resolved = SRVResolver.getServerAddress(ip);
            ip = resolved[0];
            port = Integer.parseInt(resolved[1]);
        }

        try {
            final Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), 500);
            socket.close();
        } catch (Exception e) {
            ChatUtil.sendChatMessage("&cThe server has a connection problem or is down!", sender, true);
            return;
        }

        if (Boolean.parseBoolean(args[4])) {
            new ServerPinger(sender, true).connect(ip, port, Proxy.NO_PROXY);
        }

        new PlayerConnection(sender, args[2]).connect(ip, port, Proxy.NO_PROXY);
    }
}