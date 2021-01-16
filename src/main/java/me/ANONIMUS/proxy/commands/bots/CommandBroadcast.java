package me.ANONIMUS.proxy.commands.bots;

import me.ANONIMUS.proxy.objects.Command;
import me.ANONIMUS.proxy.enums.CommandType;
import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.protocol.objects.Bot;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.impl.client.play.ClientChatPacket;
import me.ANONIMUS.proxy.utils.ChatUtil;

public class CommandBroadcast extends Command {
    public CommandBroadcast() {
        super("bc", "boradcast", "send message or command with bots", "[message]", CommandType.BOTS, ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String cmd, String[] args) throws Exception {
        if(sender.getBots().size() == 0){
            ChatUtil.sendChatMessage("&cYou don't have any connected bots", sender, true);
            return;
        }else {
            String[] message = cmd.split("#bc ", 2);
            for (Bot bot : sender.getBots()) {
                bot.getSession().sendPacket(new ClientChatPacket(message[1]));
            }
            ChatUtil.sendChatMessage("&7Successfully sent message !", sender, true);
        }
    }
}
