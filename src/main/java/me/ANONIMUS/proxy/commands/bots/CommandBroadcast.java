package me.ANONIMUS.proxy.commands.bots;

import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.objects.Command;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.impl.client.play.ClientChatPacket;
import me.ANONIMUS.proxy.utils.ChatUtil;

public class CommandBroadcast extends Command {
    public CommandBroadcast() {
        super("bc", "boradcast", "send message or command with bots", "[message]", ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String[] args) throws Exception {
        if (sender.getBots().isEmpty()) {
            ChatUtil.sendChatMessage("&cYou don't have any connected bots", sender, true);
            return;
        }
        sender.getBots().forEach(bot -> bot.getSession().sendPacket(new ClientChatPacket(args[1])));
        ChatUtil.sendChatMessage("&7Successfully sent message !", sender, true);
    }
}