package me.ANONIMUS.proxy.commands.normal;

import me.ANONIMUS.proxy.enums.CommandType;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CommandJoinBot extends Command {
    public CommandJoinBot() {
        super("joinbot","connectbot","Connecting bots to server","[host:port] [usernames] [amount] [delay] [resolver] [ping]", CommandType.NORMAL, ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String cmd, String[] args) throws Exception {
        String host = args[1];
        int port = 25565;
        if(host.contains(":")){
            final String[] sp = host.split(":",2);
            host = sp[0];
            port = Integer.parseInt(sp[1]);
        }

        final boolean resolver = Boolean.parseBoolean(args[5]);
        if(resolver) {
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

        final String usernames = args[2];
        final int amount = Integer.parseInt(args[3]);
        final int delay = Integer.parseInt(args[4]);
        final boolean ping = Boolean.parseBoolean(args[6]);
        connect(sender, delay, host, port, usernames, amount, ping);
    }

    private void connect(final Player sender, final int delay, final String host, final int port, final String usernames, final int amount, final boolean ping) {
        final ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(() -> {
            ChatUtil.sendChatMessage("&aSending!", sender, true);
            for (int i = 0; i < amount; i++) {
                final String username = (usernames + i);

                if(ping){
                   new ServerPinger(sender,false).connect(host, port, Proxy.NO_PROXY);
                }
                new BotConnection().connect(host, port, Proxy.NO_PROXY, new Bot(username, sender));
                try {
                    TimeUnit.MILLISECONDS.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ChatUtil.sendChatMessage("&aSent all bots!", sender, true);
        });
    }
}