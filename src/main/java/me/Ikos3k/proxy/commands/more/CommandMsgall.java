package me.Ikos3k.proxy.commands.more;

import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.protocol.packet.impl.client.play.ClientChatPacket;
import me.Ikos3k.proxy.utils.ChatUtil;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CommandMsgall extends Command {
    public CommandMsgall() {
        super("msgall", "msgspam", "spamming with private messages", "[delay] [text]", ConnectedType.CONNECTED);
    }

    @Override
    public void onCommand(Player sender, String[] args) throws Exception {
        int delay = Integer.parseInt(args[1]);

        StringBuilder text = new StringBuilder(args[1]);
        for (int i = 2; i < args.length; ++i) {
            text.append(" ").append(args[i]);
        }

        if(sender.getPlayers().isEmpty()) {
            ChatUtil.sendChatMessage("&cYou need to download players first " + sender.getThemeType().getColor(1) + sender.getPrefixCMD() + "players", sender, true);
            return;
        }

        sendMSG(sender, delay, text.toString());
    }

    private void sendMSG(final Player player, final int delay, final String text) {
        Executors.newSingleThreadExecutor().submit(() -> {
            ChatUtil.sendChatMessage(player.getThemeType().getColor(1) + "Message sending has started &4" + text + player.getThemeType().getColor(1) + " to &c" + player.getPlayers().size() + player.getThemeType().getColor(1) + " players", player, true);
            player.getPlayers().forEach(s -> {
                try {
                    TimeUnit.SECONDS.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                player.getRemoteSession().sendPacket(new ClientChatPacket("/msg " + s + " " + text));
            });

            ChatUtil.sendChatMessage(player.getThemeType().getColor(1) + "Message sent successfully &4" + text + player.getThemeType().getColor(1) + " to &c" + player.getPlayers().size() + player.getThemeType().getColor(1) + " players", player, true);
            player.getPlayers().clear();
        });
    }
}