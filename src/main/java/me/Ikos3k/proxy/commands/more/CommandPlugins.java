package me.Ikos3k.proxy.commands.more;

import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.protocol.packet.impl.client.play.ClientTabCompletePacket;
import me.Ikos3k.proxy.utils.ChatUtil;

public class CommandPlugins extends Command {
    public CommandPlugins() {
        super("plugins", "pl", null, "[tabcomplete]", ConnectedType.CONNECTED);
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        if (args[1].equals("tabcomplete")) {
            ChatUtil.sendChatMessage("Try to get plugins! &7(" + sender.getThemeType().getColor(1) + "TabComplete&7)", sender, true);
            sender.setOptionState("pluginsState", true);
            sender.getRemoteSession().sendPacket(new ClientTabCompletePacket("/"));
        }
    }
}