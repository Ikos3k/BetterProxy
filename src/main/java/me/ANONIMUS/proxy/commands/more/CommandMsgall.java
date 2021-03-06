package me.ANONIMUS.proxy.commands.more;

import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.objects.Command;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.impl.client.play.ClientChatPacket;
import me.ANONIMUS.proxy.utils.ChatUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CommandMsgall extends Command {
    public CommandMsgall() { super("msgall", "msgspam", "spamming with private messages", "[delay] [text]", ConnectedType.CONNECTED); }

    @Override
    public void onCommand(Player sender, String[] args) throws Exception {
        int delay = Integer.parseInt(args[1]);
        String text = "";
        for (int i = 2; i < args.length; ++i) { text = (i != 2 ? text + " " : "") + args[i]; }

        final String out = sender.getPlayers().toString();
        if (out.equals("[]")) {
            ChatUtil.sendChatMessage("&cYou need to download players first &6" + sender.getPrefixCMD() + "players", sender, true);
            return;
        }
        sendMSG(sender, delay, text);
    }

    private void sendMSG(final Player player, final int delay, final String text) {
        final ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(() -> {
            ChatUtil.sendChatMessage("&6Message sending has started &4" + text + " &6to &c" + player.getPlayers().size() + " &6players", player, true);
            player.getPlayers().forEach(s -> {
                try {
                    TimeUnit.SECONDS.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                player.getRemoteSession().sendPacket(new ClientChatPacket("/msg " + s + " " + text));
            });

            ChatUtil.sendChatMessage("&6Message sent successfully &4" + text + " &6to &c" + player.getPlayers().size() + " &6players", player, true);
            player.getPlayers().clear();
        });
    }
}