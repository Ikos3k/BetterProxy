package me.ANONIMUS.proxy.commands.bots;

import me.ANONIMUS.proxy.enums.CommandType;
import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.objects.Command;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.impl.client.play.ClientStatusPacket;
import me.ANONIMUS.proxy.utils.ChatUtil;

public class CommandRespawn extends Command {
    public CommandRespawn() {
        super("respawn", null, "respawn bots", "", CommandType.BOTS, ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String cmd, String[] args) throws Exception {
        if(sender.getBots().size() == 0) {
            ChatUtil.sendChatMessage("&cYou don't have any connected bots", sender, true);
            return;
        }
        sender.getBots().forEach(bot -> bot.getSession().sendPacket(new ClientStatusPacket(0)));
        ChatUtil.sendChatMessage("&cSuccessfully respawns bots!", sender, true);
    }
}