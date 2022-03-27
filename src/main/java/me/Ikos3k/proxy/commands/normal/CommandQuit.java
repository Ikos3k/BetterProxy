package me.Ikos3k.proxy.commands.normal;

import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.utils.ChatUtil;
import me.Ikos3k.proxy.utils.WorldUtil;

public class CommandQuit extends Command {
    public CommandQuit() {
        super("quit", "q", "Disconnecting from server", null, ConnectedType.CONNECTED);
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        ChatUtil.sendChatMessage(sender.getThemeType().getColor(1) + ">> &cDisconnected!", sender, false);
        sender.getRemoteSession().getChannel().close();
        sender.setConnectedType(ConnectedType.DISCONNECTED);
        WorldUtil.lobby(sender, true);
    }
}