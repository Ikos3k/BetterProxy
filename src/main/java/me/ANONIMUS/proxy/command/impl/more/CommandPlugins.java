package me.ANONIMUS.proxy.command.impl.more;

import me.ANONIMUS.proxy.command.Command;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.impl.client.play.ClientTabCompletePacket;
import me.ANONIMUS.proxy.utils.proxy.ChatUtil;
import me.ANONIMUS.proxy.enums.CommandType;
import me.ANONIMUS.proxy.enums.ConnectedType;

public class CommandPlugins extends Command {
    public CommandPlugins() { super("plugins", "pl", null, "[tabcomplete]", CommandType.MORE, ConnectedType.CONNECTED); }

    @Override
    public void onCommand(Player sender, String cmd, String[] args) throws Exception {
        if (args[1].equals("tabcomplete")) {
            ChatUtil.sendChatMessage("Try to get plugins! &7(&6TabComplete&7)", sender, true);
            sender.setPluginsState(true);
            sender.getRemoteSession().sendPacket(new ClientTabCompletePacket("/"));
        }
    }
}