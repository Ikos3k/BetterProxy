package me.Ikos3k.proxy.commands.bots;

import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.protocol.packet.impl.client.play.ClientStatusPacket;
import me.Ikos3k.proxy.utils.ChatUtil;

public class CommandRespawn extends Command {
    public CommandRespawn() {
        super("respawn", null, "respawn bots", null, ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        if (sender.getBots().isEmpty()) {
            ChatUtil.sendChatMessage("&cYou don't have any connected bots", sender, true);
            return;
        }

        sender.getBots().forEach(bot -> bot.getSession().sendPacket(new ClientStatusPacket(0)));
        ChatUtil.sendChatMessage("&cSuccessfully respawns bots!", sender, true);
    }
}