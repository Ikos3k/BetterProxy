package me.Ikos3k.proxy.commands.bots;

import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.protocol.packet.impl.client.play.ClientChatPacket;
import me.Ikos3k.proxy.utils.ChatUtil;

public class CommandBroadcast extends Command {
    public CommandBroadcast() {
        super("bc", "boradcast", "send message or command with bots", "[message]", ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        if (sender.getBots().isEmpty()) {
            ChatUtil.sendChatMessage("&cYou don't have any connected bots", sender, true);
            return;
        }

        StringBuilder text = new StringBuilder(args[1]);
        for (int i = 2; i < args.length; ++i) {
            text.append(" ").append(args[i]);
        }

        sender.getBots().forEach(bot -> bot.getSession().sendPacket(new ClientChatPacket(text.toString())));
        ChatUtil.sendChatMessage("&7Successfully sent message !", sender, true);
    }
}