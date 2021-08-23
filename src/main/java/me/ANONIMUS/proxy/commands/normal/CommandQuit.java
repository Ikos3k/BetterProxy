package me.ANONIMUS.proxy.commands.normal;

import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.objects.Command;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.utils.ChatUtil;
import me.ANONIMUS.proxy.utils.WorldUtil;

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