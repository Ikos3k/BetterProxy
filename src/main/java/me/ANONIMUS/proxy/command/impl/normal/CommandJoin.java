package me.ANONIMUS.proxy.command.impl.normal;

import me.ANONIMUS.proxy.command.Command;
import me.ANONIMUS.proxy.enums.CommandType;
import me.ANONIMUS.proxy.enums.ConnectedType;
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
        super("join", "connect", "Connecting to server", "[host:port] [username] [resolver] [ping]", CommandType.NORMAL, ConnectedType.DISCONNECTED);
    }

    @Override
    public void onCommand(Player sender, String cmd, String[] args) throws Exception {
        String host = args[1];
        int port = 25565;
        if (host.contains(":")) {
            final String[] sp = host.split(":", 2);
            host = sp[0];
            port = Integer.parseInt(sp[1]);
        }

        if(Boolean.parseBoolean(args[3])) {
            final String[] resolved = SRVResolver.getServerAddress(host);
            host = resolved[0];
            port = Integer.parseInt(resolved[1]);
        }
        try {
            final Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), 500);
            socket.close();
        } catch(Exception e) {
            ChatUtil.sendChatMessage("&cThe server has a connection problem or is down!", sender, true);
            return;
        }

        final Proxy proxy = Proxy.NO_PROXY;
        if (Boolean.parseBoolean(args[4])) {
            new ServerPinger(sender, true).connect(host, port, proxy);
        }
        new PlayerConnection(sender, args[2]).connect(host, port, proxy);
    }
}
